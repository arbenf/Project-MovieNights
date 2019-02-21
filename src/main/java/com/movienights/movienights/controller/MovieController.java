package com.movienights.movienights.controller;

import com.movienights.movienights.entity.Movie;
import com.movienights.movienights.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
public class MovieController {

    @Autowired
    private MovieRepository movieRepository;

    RestTemplate restTemplate = new RestTemplate();


    @RequestMapping(method = RequestMethod.GET, value = "/movies")
    public HttpEntity<Movie> getMovie(@RequestParam(value="title") String title) {

        Movie movie = movieRepository.findDistinctFirstByTitleIgnoreCase(title);
        if (movie == null) {
            movie = restTemplate.getForObject("http://www.omdbapi.com/?apikey=7a3ecfb6&t=" + title, Movie.class);
            if (movie.getTitle() != null) {
                movieRepository.save(movie);
            } else {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping(path = "/all")
    public @ResponseBody
    Iterable<Movie> getAllMovies() {
        return movieRepository.findAll();
    }
}