package com.movienights.movienights.omdb;


import com.movienights.movienights.entity.Movie;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class OmdbClientService {

//    public static final String SEARCH_URL = "http://www.omdbapi.com/?s=TITLE&apikey=APIKEY";
//    public static final String SEARCH_BY_IMDB_URL = "http://www.omdbapi.com/?i=IMDB&apikey=APIKEY";
//
//    public String sendGetRequest(String requestUrl) {
//        StringBuffer response = new StringBuffer();
//        try {
//            URL url = new URL(requestUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//            connection.setRequestProperty("Accept", "*/*");
//            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
//            InputStream stream = connection.getInputStream();
//            InputStreamReader reader = new InputStreamReader(stream);
//            BufferedReader buffer = new BufferedReader(reader);
//            String line;
//            while ((line = buffer.readLine()) != null) {
//                response.append(line);
//            }
//            buffer.lines();
//            connection.disconnect();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response.toString();
//    }
//
//    public String searchMovieByTitle(String title, String key) {
//        try {
//            title = URLEncoder.encode(title, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String requestUrl = SEARCH_URL.
//                replaceAll("TITLE", title)
//                .replaceAll("APIKEY", key);
//
//        return sendGetRequest(requestUrl);
//    }
//
//    public String searchMovieByImdb(String imdb, String key) {
//        String requestUrl = SEARCH_BY_IMDB_URL.
//                replaceAll("IMDB", imdb)
//                .replaceAll("APIKEY", key);
//
//        return sendGetRequest(requestUrl);
//    }
//
//    public JSONArray getMovieArray(String jsonResponse) throws JSONException {
//        JSONObject jsonObject = new JSONObject(jsonResponse);
//        JSONArray jsonArray = jsonObject.getJSONArray("Search");
//        return jsonArray;
//    }

        public static void main(String args[]) {
            RestTemplate restTemplate = new RestTemplate();
            Movie movie = restTemplate.getForObject("http://www.omdbapi.com/?apikey=7a3ecfb6&t=batman", Movie.class);
            System.out.println(movie.toString());
        }
}
