package com.movienights.movienights.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.movienights.movienights.entity.EventBooking;
import com.movienights.movienights.entity.Period;
import com.movienights.movienights.entity.User;
import com.movienights.movienights.repository.EventBookingRepository;
import com.movienights.movienights.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
public class GoogleAcountController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventBookingRepository eventBookingRepository;

    final String CLIENT_ID = "126121553255-v7skv38gdish61psrj3uaugngkbh9tum.apps.googleusercontent.com";
    final String CLIENT_SECRET = "x2lm9fjlhrJQxINC_IGfUJ4m";


    public static <T> T firstNonNull(T... params) {
        for (T param : params)
            if (param != null)
                return param;
        return null;
    }

    @RequestMapping(value = "/storeauthcode", method = RequestMethod.POST)
    public String storeauthcode(@RequestBody String code, @RequestHeader("X-Requested-With") String encoding) {
        if (encoding == null || encoding.isEmpty()) {
            // Without the `X-Requested-With` header, this request could be forged. Aborts.
            return "Error, wrong headers";
        }

        GoogleTokenResponse tokenResponse = null;
        try {
            tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    "https://www.googleapis.com/oauth2/v4/token",
                    CLIENT_ID,
                    CLIENT_SECRET,
                    code,
                    "http://localhost:8080")
                    .execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store these 3in your DB
        String accessToken = tokenResponse.getAccessToken();
        String refreshToken = tokenResponse.getRefreshToken();
        Long expiresAt = System.currentTimeMillis() + (tokenResponse.getExpiresInSeconds() * 1000);

        // Debug purpose only
        System.out.println("accessToken: " + accessToken);
        System.out.println("refreshToken: " + refreshToken);
        System.out.println("expiresAt: " + expiresAt);

        // Get profile info from ID token (Obtained at the last step of OAuth2)
        GoogleIdToken idToken = null;
        try {
            idToken = tokenResponse.parseIdToken();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GoogleIdToken.Payload payload = idToken.getPayload();

        // Use THIS ID as a key to identify a google user-account.
        String userId = payload.getSubject();

        String email = payload.getEmail();
        boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
        String name = (String) payload.get("name");
        String pictureUrl = (String) payload.get("picture");
        String locale = (String) payload.get("locale");
        String familyName = (String) payload.get("family_name");
        String givenName = (String) payload.get("given_name");

        // Debugging purposes, should probably be stored in the database instead (At least "givenName").
        System.out.println("userId: " + userId);
        System.out.println("email: " + email);
        System.out.println("emailVerified: " + emailVerified);
        System.out.println("name: " + name);
        System.out.println("pictureUrl: " + pictureUrl);
        System.out.println("locale: " + locale);
        System.out.println("familyName: " + familyName);
        System.out.println("givenName: " + givenName);

        User user = new User(name,email,accessToken,refreshToken,expiresAt);
        userRepository.save(user);

        return "OK";
    }


    private GoogleCredential getRefreshedCredentials(String refreshCode) {
        try {
            GoogleTokenResponse response = new GoogleRefreshTokenRequest(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(),
                    refreshCode,
                    CLIENT_ID,
                    CLIENT_SECRET )
                    .execute();

            return new GoogleCredential().setAccessToken(response.getAccessToken());
        }
        catch( Exception ex ){
            ex.printStackTrace();
            return null;
        }
    }


    @RequestMapping(value = "/availableTimes", method = RequestMethod.GET)
    public List<Period> getAvailableTimes() {

        List<Event> allEvents = new ArrayList<>();

        List<Period> unreservedTimes = new ArrayList<>();

        for (User user : userRepository.findAll()) {

            Long expiredTime = user.getExpiresAt();
            Long currentTime = System.currentTimeMillis();

            GoogleCredential cred;

            if( expiredTime < currentTime ) {
                cred = getRefreshedCredentials(user.getRefreshToken());
                user.setAccessToken(cred.getAccessToken());
                user.setExpiresAt(System.currentTimeMillis() + 3600*1000);
                userRepository.save(user);
            } else {
                cred = new GoogleCredential().setAccessToken(user.getAccessToken());
            }

            Calendar calendar = getCalendar(cred);

            DateTime now = new DateTime(System.currentTimeMillis());
            Events events = null;

            try {
                events = calendar.events().list("primary")
                        .setMaxResults(10)
                        .setTimeMin(now)
                        .setOrderBy("startTime")
                        .setSingleEvents(true)
                        .execute();

            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Event> items = events.getItems();


            if (items.isEmpty()) {
                System.out.println("No upcoming events found.");
            } else {
                System.out.println("Upcoming events");
                for (Event event : items) {
                    allEvents.add(event);

                    DateTime start = event.getStart().getDateTime();
                    if (start == null) {
                        start = event.getStart().getDate();
                    }
                    DateTime end = event.getEnd().getDateTime();
                    if (end == null) {
                        end = event.getStart().getDate();
                    }
                    System.out.printf("%s (%s) -> (%s)\n", event.getSummary(), start, end);
                }
            }
        }

        for (Event event : allEvents) {
            DateTime start = firstNonNull(event.getStart().getDateTime(), event.getStart().getDate());
            System.out.printf("%s (%s)\n", event.getSummary(), start);
        }

        allEvents.sort(Comparator.comparing(event -> firstNonNull(event.getStart().getDateTime(), event.getStart().getDate()).getValue()));

        Period nowToNext = new Period(
                new DateTime(System.currentTimeMillis()),
                firstNonNull(allEvents.get(0).getStart().getDateTime(), allEvents.get(0).getStart().getDate())
        );
        unreservedTimes.add(nowToNext);

        for (int i = 0; i < allEvents.size() - 1; i++) {
            Period periodBetweenEvents = new Period(firstNonNull(allEvents.get(i).getEnd().getDateTime(), allEvents.get(i).getEnd().getDate()),
                    firstNonNull(allEvents.get(i + 1).getStart().getDateTime(), allEvents.get(i + 1).getStart().getDate()));
            unreservedTimes.add(periodBetweenEvents);
        }
        System.out.println(unreservedTimes);

        Period lastEventPlusAMonth = new Period(
                firstNonNull(allEvents.get(allEvents.size() -1).getEnd().getDateTime(), allEvents.get(allEvents.size() - 1).getEnd().getDate()),
                new DateTime(  firstNonNull(allEvents.get(allEvents.size() -1).getEnd().getDateTime(), allEvents.get(allEvents.size() - 1).getEnd().getDate()).getValue()
                        + 31*24*60*60*1000L)
        );
        unreservedTimes.add(lastEventPlusAMonth);

        return unreservedTimes;
    }

    @CrossOrigin(origins = "*")
    @JsonProperty("eventBooking")
    @PostMapping(value = "/booking")
    public void showBookingInfo(@RequestBody EventBooking eventBooking) throws IOException {

        for( User user : userRepository.findAll()) {
            GoogleCredential credential = getRefreshedCredentials(user.getRefreshToken());
            Calendar calendar = getCalendar(credential);

            Event event = new Event()
                    .setSummary("New Movie")
                    .setLocation("MovieNight")
                    .setDescription("");

            DateTime startDateTime = new DateTime( Long.parseLong(eventBooking.getEnd()) );
            EventDateTime start = new EventDateTime()
                    .setDateTime(startDateTime)
                    .setTimeZone("CET");
            event.setStart(start);

            DateTime endDateTime = new DateTime( Long.parseLong(eventBooking.getStart()) );
            EventDateTime end = new EventDateTime()
                    .setDateTime(endDateTime)
                    .setTimeZone("CET");
            event.setEnd(end);

            String calendarId = "primary";

            event = calendar.events().insert(calendarId, event).execute();
            System.out.printf("Event created: %s\n", event.getHtmlLink());
        }

        System.out.println("the post was a success!");
        System.out.println( Long.parseLong(eventBooking.getStart()) );

        eventBookingRepository.save(eventBooking);
    }

    public Calendar getCalendar(GoogleCredential credential) {
        return new Calendar.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("Movie Nights")
                .build();
    }

}
