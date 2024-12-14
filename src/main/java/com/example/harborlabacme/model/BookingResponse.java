package com.example.harborlabacme.model;

import java.time.LocalTime;

public class BookingResponse {

    private String email;
    private LocalTime timeFrom;
    private LocalTime timeTo;

    public BookingResponse(String email, LocalTime timeFrom, LocalTime timeTo) {
        this.email = email;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalTime getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(LocalTime timeFrom) {
        this.timeFrom = timeFrom;
    }

    public LocalTime getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(LocalTime timeTo) {
        this.timeTo = timeTo;
    }
}
