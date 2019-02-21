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

        public static void main(String args[]) {
            RestTemplate restTemplate = new RestTemplate();
            Movie movie = restTemplate.getForObject("http://www.omdbapi.com/?apikey=7a3ecfb6&t=batman", Movie.class);
            System.out.println(movie.toString());
        }
}
