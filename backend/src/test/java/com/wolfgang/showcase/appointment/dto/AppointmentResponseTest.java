package com.wolfgang.showcase.appointment.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentResponseTest {

    @Test
    void rejectsNullAppointment() {
        assertThrows(NullPointerException.class, () -> new AppointmentResponse(null));
    }
}
