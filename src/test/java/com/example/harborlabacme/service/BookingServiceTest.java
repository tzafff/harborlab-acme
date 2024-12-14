package com.example.harborlabacme.service;

import com.example.harborlabacme.entity.Booking;
import com.example.harborlabacme.exceptions.BookingValidationException;
import com.example.harborlabacme.model.BookingRequest;
import com.example.harborlabacme.repository.BookingRepository;
import com.example.harborlabacme.validations.ValidateBookingReqDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ValidateBookingReqDTO validateBookingReqDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateBookingSuccess() {

        // Set
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setRoom("ROOM_A");
        bookingRequest.setEmail("test@example.com");
        bookingRequest.setDate(LocalDate.now().plusDays(1));
        bookingRequest.setTimeFrom(LocalTime.of(10, 0));
        bookingRequest.setTimeTo(LocalTime.of(11, 0));


        doNothing().when(validateBookingReqDTO).validateBookingRequest(bookingRequest);


        when(bookingRepository.findByRoomAndDate("ROOM_A", bookingRequest.getDate())).thenReturn(List.of());


        Booking mockBooking = new Booking();
        mockBooking.setRoom("ROOM_A");
        mockBooking.setEmail("test@example.com");
        mockBooking.setDate(LocalDate.now().plusDays(1));
        mockBooking.setTimeFrom(LocalTime.of(10, 0));
        mockBooking.setTimeTo(LocalTime.of(11, 0));

        when(bookingRepository.save(any(Booking.class))).thenReturn(mockBooking);

        // When
        Booking createdBooking = bookingService.createBooking(bookingRequest);

        // Then
        assertNotNull(createdBooking);
        assertEquals("ROOM_A", createdBooking.getRoom());
        assertEquals("test@example.com", createdBooking.getEmail());
        assertEquals(LocalDate.now().plusDays(1), createdBooking.getDate());
        assertEquals(LocalTime.of(10, 0), createdBooking.getTimeFrom());
        assertEquals(LocalTime.of(11, 0), createdBooking.getTimeTo());
    }

    @Test
    void testCreateBookingInvalidRoom() {
        // Set
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setRoom("INVALID_ROOM");
        bookingRequest.setEmail("test@example.com");
        bookingRequest.setDate(LocalDate.now().plusDays(1));
        bookingRequest.setTimeFrom(LocalTime.of(10, 0));
        bookingRequest.setTimeTo(LocalTime.of(11, 0));

        // When
        BookingValidationException exception = assertThrows(BookingValidationException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });

        // Then
        assertEquals("Invalid room", exception.getMessage());
    }

    @Test
    void testCreateBookingPastDate() {
        // Set
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setRoom("ROOM_A");
        bookingRequest.setEmail("test@example.com");
        bookingRequest.setDate(LocalDate.now().minusDays(1)); // Past date
        bookingRequest.setTimeFrom(LocalTime.of(10, 0));
        bookingRequest.setTimeTo(LocalTime.of(11, 0));

        // When
        BookingValidationException exception = assertThrows(BookingValidationException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });

        // Then
        assertEquals("Cannot create a booking for a past date", exception.getMessage());
    }

    @Test
    void testCreateBookingConflict() {
        // Set
        BookingRequest bookingRequest = new BookingRequest();
        bookingRequest.setRoom("ROOM_A");
        bookingRequest.setEmail("test@example.com");
        bookingRequest.setDate(LocalDate.now().plusDays(1));
        bookingRequest.setTimeFrom(LocalTime.of(10, 0));
        bookingRequest.setTimeTo(LocalTime.of(11, 0));


        doNothing().when(validateBookingReqDTO).validateBookingRequest(bookingRequest);


        Booking existingBooking = new Booking();
        existingBooking.setRoom("ROOM_A");
        existingBooking.setDate(bookingRequest.getDate());
        existingBooking.setTimeFrom(LocalTime.of(9, 0));
        existingBooking.setTimeTo(LocalTime.of(11, 0));

        when(bookingRepository.findByRoomAndDate("ROOM_A", bookingRequest.getDate())).thenReturn(List.of(existingBooking));

        // When
        BookingValidationException exception = assertThrows(BookingValidationException.class, () -> {
            bookingService.createBooking(bookingRequest);
        });

        // Then
        assertEquals("Booking conflict", exception.getMessage());
    }

    @Test
    void testCancelBookingSuccess() {
        // Set
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setRoom("ROOM_A");
        booking.setEmail("test@example.com");
        booking.setDate(LocalDate.now().plusDays(1));
        booking.setTimeFrom(LocalTime.of(10, 0));
        booking.setTimeTo(LocalTime.of(11, 0));


        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // When
        bookingService.cancelBooking(bookingId);

        // Then
        verify(bookingRepository, times(1)).deleteById(bookingId);
    }

    @Test
    void testCancelBookingNotFound() {
        // Set
        Long bookingId = 1L;

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        // When
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            bookingService.cancelBooking(bookingId);
        });

        // Then
        assertEquals("Booking not found", exception.getMessage());
    }

    @Test
    void testCancelBookingPastDate() {
        // Set
        Long bookingId = 1L;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setRoom("ROOM_A");
        booking.setEmail("test@example.com");
        booking.setDate(LocalDate.now().minusDays(1)); // Past date
        booking.setTimeFrom(LocalTime.of(10, 0));
        booking.setTimeTo(LocalTime.of(11, 0));


        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            bookingService.cancelBooking(bookingId);
        });

        // Then
        assertEquals("Cannot cancel past booking", exception.getMessage());
    }
}