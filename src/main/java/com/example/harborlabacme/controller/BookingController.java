package com.example.harborlabacme.controller;

import com.example.harborlabacme.entity.Booking;
import com.example.harborlabacme.model.BookingRequest;
import com.example.harborlabacme.model.BookingResponse;
import com.example.harborlabacme.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @GetMapping
    public List<BookingResponse> getBookings(@RequestParam String room, @RequestParam LocalDate date) {
        List<Booking> bookings = bookingService.getBookingsByRoomAndDate(room, date);
        return bookings.stream()
                .map(booking -> new BookingResponse(booking.getEmail(), booking.getTimeFrom(), booking.getTimeTo()))
                .collect(Collectors.toList());
    }

    @GetMapping("/all")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<Booking> bookings = bookingService.getAllBookings();
        if (bookings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ArrayList<BookingResponse>());
        }
        List<BookingResponse> response = bookings.stream()
                .map(booking -> new BookingResponse(booking.getEmail(), booking.getTimeFrom(), booking.getTimeTo()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @PostMapping("create")
    public BookingResponse createBooking( @RequestBody BookingRequest bookingRequest) {
        Booking booking = bookingService.createBooking(bookingRequest);
        return new BookingResponse(booking.getEmail(), booking.getTimeFrom(), booking.getTimeTo());
    }

    @DeleteMapping("/{id}")
    public void cancelBooking(@PathVariable Long id) {
        bookingService.cancelBooking(id);
    }


}
