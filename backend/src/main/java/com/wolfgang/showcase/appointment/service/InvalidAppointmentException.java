package com.wolfgang.showcase.appointment.service;

public class InvalidAppointmentException extends RuntimeException {

    public InvalidAppointmentException(String message) {
        super(message);
    }
}
