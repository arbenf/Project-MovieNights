package com.movienights.movienights.repository;

import com.movienights.movienights.entity.EventBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventBookingRepository extends JpaRepository<EventBooking, Integer> {
}
