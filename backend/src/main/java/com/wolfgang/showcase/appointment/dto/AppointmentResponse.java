package com.wolfgang.showcase.appointment.dto;

import com.wolfgang.showcase.appointment.model.Appointment;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class AppointmentResponse {

    private final UUID id;
    private final String customerName;
    private final String customerEmail;
    private final String subject;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private final String status;

    public AppointmentResponse(Appointment appointment) {
        Appointment source = Objects.requireNonNull(appointment, "appointment must not be null");

        this.id = source.getId();
        this.customerName = source.getCustomerName();
        this.customerEmail = source.getCustomerEmail();
        this.subject = source.getSubject();
        this.startsAt = source.getStartsAt();
        this.endsAt = source.getEndsAt();
        this.status = source.getStatus().name();
    }

    public UUID getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDateTime getStartsAt() {
        return startsAt;
    }

    public LocalDateTime getEndsAt() {
        return endsAt;
    }

    public String getStatus() {
        return status;
    }
}
