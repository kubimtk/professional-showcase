package com.wolfgang.showcase.appointment.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Appointment {

    private final UUID id;
    private final String customerName;
    private final String customerEmail;
    private final String subject;
    private final LocalDateTime startsAt;
    private final LocalDateTime endsAt;
    private Status status;

    public Appointment(
            UUID id,
            String customerName,
            String customerEmail,
            String subject,
            LocalDateTime startsAt,
            LocalDateTime endsAt,
            Status status
    ) {
        this.id = Objects.requireNonNull(id);
        this.customerName = Objects.requireNonNull(customerName);
        this.customerEmail = Objects.requireNonNull(customerEmail);
        this.subject = Objects.requireNonNull(subject);
        this.startsAt = Objects.requireNonNull(startsAt);
        this.endsAt = Objects.requireNonNull(endsAt);
        this.status = Objects.requireNonNull(status);
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

    public Status getStatus() {
        return status;
    }

    public boolean overlaps(LocalDateTime otherStart, LocalDateTime otherEnd) {
        return startsAt.isBefore(otherEnd) && endsAt.isAfter(otherStart);
    }

    public boolean isActive() {
        return status == Status.SCHEDULED;
    }

    public void cancel() {
        this.status = Status.CANCELLED;
    }

    public enum Status {
        SCHEDULED,
        CANCELLED
    }
}
