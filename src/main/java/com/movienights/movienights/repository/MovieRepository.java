package com.movienights.movienights.repository;

import com.movienights.movienights.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findDistinctFirstByTitleIgnoreCase(String title);

    List<Movie> findAll();
}