package com.example.harborlabacme.repository;

import com.example.harborlabacme.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByRoomAndDate(String room, LocalDate date);

}
