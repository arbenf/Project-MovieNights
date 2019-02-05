package com.movienights.movienights.repository;

import com.movienights.movienights.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

public interface MovieRepository extends JpaRepository<Movie, Integer> {

    Movie findDistinctFirstByTitleIgnoreCase(String title);

}