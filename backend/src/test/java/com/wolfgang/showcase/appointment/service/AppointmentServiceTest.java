package com.wolfgang.showcase.appointment.service;

import com.wolfgang.showcase.appointment.model.Appointment;
import com.wolfgang.showcase.appointment.repository.InMemoryAppointmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppointmentServiceTest {

    private AppointmentService appointmentService;

    @BeforeEach
    void setUp() {
        appointmentService = new AppointmentService(new InMemoryAppointmentRepository());
    }

    @Test
    void createsAppointmentAndNormalizesEmail() {
        LocalDateTime startsAt = nextWeekdayAt(10, 0);
        LocalDateTime endsAt = startsAt.plusMinutes(45);

        Appointment appointment = appointmentService.createAppointment(
                "Sample Customer",
                " sample.customer@example.org ",
                "Architecture review",
                startsAt,
                endsAt
        );

        assertEquals("sample.customer@example.org", appointment.getCustomerEmail());
        assertEquals(Appointment.Status.SCHEDULED, appointment.getStatus());
    }

    @Test
    void rejectsOverlappingAppointmentForSameCustomer() {
        LocalDateTime startsAt = nextWeekdayAt(11, 0);
        LocalDateTime endsAt = startsAt.plusMinutes(30);

        appointmentService.createAppointment(
                "Sample Customer",
                "wolfgang@example.org",
                "Initial meeting",
                startsAt,
                endsAt
        );

        assertThrows(
                AppointmentConflictException.class,
                () -> appointmentService.createAppointment(
                        "Sample Customer",
                        "wolfgang@example.org",
                        "Follow-up",
                        startsAt.plusMinutes(15),
                        endsAt.plusMinutes(15)
                )
        );
    }

    @Test
    void rejectsAppointmentOutsideBusinessHours() {
        LocalDateTime startsAt = nextWeekdayAt(8, 30);
        LocalDateTime endsAt = startsAt.plusMinutes(30);

        assertThrows(
                InvalidAppointmentException.class,
                () -> appointmentService.createAppointment(
                        "Sample Customer",
                        "wolfgang@example.org",
                        "Too early",
                        startsAt,
                        endsAt
                )
        );
    }

    private LocalDateTime nextWeekdayAt(int hour, int minute) {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return LocalDateTime.of(date, LocalTime.of(hour, minute));
    }
}
