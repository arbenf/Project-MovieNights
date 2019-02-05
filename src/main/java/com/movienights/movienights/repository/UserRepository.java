package com.movienights.movienights.repository;

import com.movienights.movienights.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
