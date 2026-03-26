package com.wolfgang.showcase.appointment.dto;

import com.wolfgang.showcase.appointment.model.Appointment;

import java.time.LocalDateTime;
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
        this(
                appointment.getId(),
                appointment.getCustomerName(),
                appointment.getCustomerEmail(),
                appointment.getSubject(),
                appointment.getStartsAt(),
                appointment.getEndsAt(),
                appointment.getStatus().name()
        );
    }

    private AppointmentResponse(
            UUID id,
            String customerName,
            String customerEmail,
            String subject,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            String status
    ) {
        this.id = id;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.subject = subject;
        this.startsAt = startsAt;
        this.endsAt = endsAt;
        this.status = status;
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
