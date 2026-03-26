package com.wolfgang.showcase.appointment.service;

import com.wolfgang.showcase.appointment.model.Appointment;
import com.wolfgang.showcase.appointment.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class AppointmentService {

    private static final LocalTime BUSINESS_DAY_START = LocalTime.of(9, 0);
    private static final LocalTime BUSINESS_DAY_END = LocalTime.of(17, 0);
    private static final Duration MAX_DURATION = Duration.ofHours(2);

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    public List<Appointment> listAppointments() {
        return appointmentRepository.findAll();
    }

    public Appointment getAppointment(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found: " + id));
    }

    public synchronized Appointment createAppointment(
            String customerName,
            String customerEmail,
            String subject,
            LocalDateTime startsAt,
            LocalDateTime endsAt
    ) {
        validateTimeRange(startsAt, endsAt);

        String normalizedEmail = normalizeEmail(customerEmail);

        boolean conflictingAppointmentExists = appointmentRepository.findAll().stream()
                .filter(Appointment::isActive)
                .filter(appointment -> appointment.getCustomerEmail().equals(normalizedEmail))
                .anyMatch(appointment -> appointment.overlaps(startsAt, endsAt));

        if (conflictingAppointmentExists) {
            throw new AppointmentConflictException(
                    "Customer already has an overlapping scheduled appointment."
            );
        }

        Appointment appointment = new Appointment(
                UUID.randomUUID(),
                customerName.trim(),
                normalizedEmail,
                subject.trim(),
                startsAt,
                endsAt,
                Appointment.Status.SCHEDULED
        );

        return appointmentRepository.save(appointment);
    }

    public synchronized Appointment cancelAppointment(UUID id) {
        Appointment appointment = getAppointment(id);

        if (!appointment.isActive()) {
            throw new AppointmentConflictException("Appointment is already cancelled.");
        }

        appointment.cancel();
        return appointmentRepository.save(appointment);
    }

    private void validateTimeRange(LocalDateTime startsAt, LocalDateTime endsAt) {
        if (!startsAt.isBefore(endsAt)) {
            throw new InvalidAppointmentException("Appointment start must be before appointment end.");
        }

        if (Duration.between(startsAt, endsAt).compareTo(MAX_DURATION) > 0) {
            throw new InvalidAppointmentException("Appointments may not be longer than two hours.");
        }

        if (startsAt.toLocalDate().isEqual(endsAt.toLocalDate()) == false) {
            throw new InvalidAppointmentException("Appointments must start and end on the same day.");
        }

        if (isWeekday(startsAt) == false || isWeekday(endsAt) == false) {
            throw new InvalidAppointmentException("Appointments are only available on weekdays.");
        }

        if (startsAt.toLocalTime().isBefore(BUSINESS_DAY_START) || endsAt.toLocalTime().isAfter(BUSINESS_DAY_END)) {
            throw new InvalidAppointmentException("Appointments must stay within business hours: 09:00-17:00.");
        }
    }

    private boolean isWeekday(LocalDateTime value) {
        DayOfWeek dayOfWeek = value.getDayOfWeek();
        return dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY;
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
