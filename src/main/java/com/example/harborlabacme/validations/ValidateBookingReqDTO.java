package com.example.harborlabacme.validations;

import com.example.harborlabacme.exceptions.BookingValidationException;
import com.example.harborlabacme.model.BookingRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Service
public class ValidateBookingReqDTO {

    public void validateBookingRequest(BookingRequest bookingRequest) {
        validateRoom(bookingRequest.getRoom());
        validateEmail(bookingRequest.getEmail());
        validateDate(bookingRequest.getDate());
        validateTime(bookingRequest.getTimeFrom(), bookingRequest.getTimeTo());
    }

    private void validateRoom(String room) {
        if (Objects.isNull(room) || room.isEmpty()) {
            throw new BookingValidationException("Room cannot be null or empty");
        }
    }

    private void validateEmail(String email) {
        if (Objects.isNull(email) || email.isEmpty()) {
            throw new BookingValidationException("Email cannot be null or empty");
        }
    }

    private void validateDate(LocalDate date) {
        if (Objects.isNull(date)) {
            throw new BookingValidationException("Date cannot be null");
        }
        if (date.isBefore(LocalDate.now())) {
            throw new BookingValidationException("Booking date cannot be in the past");
        }
    }

    private void validateTime(LocalTime timeFrom, LocalTime timeTo) {
        if (Objects.isNull(timeFrom) || Objects.isNull(timeTo)) {
            throw new BookingValidationException("Start and end time cannot be null");
        }
        if (timeFrom.isAfter(timeTo)) {
            throw new BookingValidationException("Start time cannot be after end time");
        }
    }
}
