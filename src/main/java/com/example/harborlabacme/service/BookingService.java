package com.example.harborlabacme.service;

import com.example.harborlabacme.entity.Booking;
import com.example.harborlabacme.enums.Rooms;
import com.example.harborlabacme.exceptions.BookingValidationException;
import com.example.harborlabacme.model.BookingRequest;
import com.example.harborlabacme.repository.BookingRepository;
import com.example.harborlabacme.validations.ValidateBookingReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired ValidateBookingReqDTO validateBookingReqDTO;

    public List<Booking> getBookingsByRoomAndDate(String room, LocalDate date) {
        return bookingRepository.findByRoomAndDate(room, date);
    }

    public Booking createBooking(BookingRequest bookingRequest) {

        validateBookingReqDTO.validateBookingRequest(bookingRequest);

        if (!isRoomValid(bookingRequest.getRoom())) {
            throw new BookingValidationException("Invalid room");
        }

        if (bookingRequest.getDate().isBefore(LocalDate.now())) {
            throw new BookingValidationException("Cannot create a booking for a past date");
        }

        long durationMinutes = java.time.Duration.between(bookingRequest.getTimeFrom(), bookingRequest.getTimeTo()).toMinutes();
        if (durationMinutes < 60 || durationMinutes % 60 != 0) {
            throw new BookingValidationException("Booking must be for at least 1 hour or multiples of 1 hour");
        }


        List<Booking> bookingsForRoomAndDate = bookingRepository.findByRoomAndDate(bookingRequest.getRoom(), bookingRequest.getDate());

        // Check for overlaps
        for (Booking existingBooking : bookingsForRoomAndDate) {
            if (timeSlotsOverlap(existingBooking.getTimeFrom(), existingBooking.getTimeTo(), bookingRequest.getTimeFrom(), bookingRequest.getTimeTo())) {
                throw new BookingValidationException("Booking conflict");
            }
        }

        Booking booking = new Booking();
        booking.setRoom(bookingRequest.getRoom());
        booking.setEmail(bookingRequest.getEmail());
        booking.setDate(bookingRequest.getDate());
        booking.setTimeFrom(bookingRequest.getTimeFrom());
        booking.setTimeTo(bookingRequest.getTimeTo());

        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);

        if (optionalBooking.isEmpty()) {
            throw new EntityNotFoundException("Booking not found");
        }

        Booking booking = optionalBooking.get();
        if (booking.getDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot cancel past booking");
        }

        bookingRepository.deleteById(id);
    }


    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }


    private boolean isRoomValid(String room) {
        try {
            Rooms.valueOf(room);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private boolean timeSlotsOverlap(LocalTime existingFrom, LocalTime existingTo, LocalTime newFrom, LocalTime newTo) {
        return !(newTo.equals(existingFrom) || newFrom.equals(existingTo) || newTo.isBefore(existingFrom) || newFrom.isAfter(existingTo));
    }
}

