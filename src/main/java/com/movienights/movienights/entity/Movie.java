package com.movienights.movienights.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private float imdbRating;

    public Movie() {}

    public Movie(String title, float imdbRating) {
        this.title = title;
        this.imdbRating = imdbRating;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(float imdbRating) {
        this.imdbRating = imdbRating;
    }

    @Override
    public String toString() {
        return "Movie" +
                "title='" + title + '\'' +
                ", imdbRating=" + imdbRating +
                '}';
    }
}
