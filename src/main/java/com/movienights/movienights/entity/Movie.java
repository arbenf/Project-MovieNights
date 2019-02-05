package com.movienights.movienights.entity;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Movie {
    @Id
    @GeneratedValue
    private int id;
    @JsonProperty("Title")
    private String title;
    private float imdbRating;
//    private String accessToken;
//    private String refreshToken;
//    private Long expiresAt;

    // Exception encountered during context initialization -
    // // cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException:
    // // Error creating bean with name 'entityManagerFactory' defined in class path resource
    // // [org/springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]:
    // // Invocation of init method failed; nested exception is org.hibernate.AnnotationException:
    // // No identifier specified for entity: com.movienights.movienights.entity.Movie
    //2019-01-07 10:05:21.005  INFO 16808 --- [           main] com.zaxxer.hikari.HikariDataSource       :

    // Useful annotation example, will rename when saving/loading from database
    // @Field("databaseFieldName")
    // private int yourVariableNameInJava;

    public Movie() {}
    public Movie(String title, float imdbRating) {
        this.title = title;
        this.imdbRating = imdbRating;
    }

    // getters and setters

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

//
//public String getAccessToken() {
//        return accessToken;
//    }
//
//    public void setAccessToken(String accessToken) {
//        this.accessToken = accessToken;
//    }
//
//    public String getRefreshToken() {
//        return refreshToken;
//    }
//
//    public void setRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }
//
//    public Long getExpiresAt() {
//        return expiresAt;
//    }
//
//    public void setExpiresAt(Long expiresAt) {
//        this.expiresAt = expiresAt;
//    }
    @Override
    public String toString() {
        return "Movie" +
                "title='" + title + '\'' +
                ", imdbRating=" + imdbRating +
                '}';
    }
}
