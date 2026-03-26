package com.wolfgang.showcase.appointment.service;

public class AppointmentConflictException extends RuntimeException {

    public AppointmentConflictException(String message) {
        super(message);
    }
}
