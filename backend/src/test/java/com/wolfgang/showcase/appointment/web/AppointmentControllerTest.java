package com.wolfgang.showcase.appointment.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AppointmentControllerTest {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createsAppointmentSuccessfully() throws Exception {
        LocalDateTime startsAt = nextWeekdayAt(10, 0);
        LocalDateTime endsAt = startsAt.plusMinutes(30);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"customerName\":\"Wolfgang Kubisiak\","
                                + "\"customerEmail\":\"wolfgang+" + UUID.randomUUID() + "@example.org\","
                                + "\"subject\":\"Backend showcase walkthrough\","
                                + "\"startsAt\":\"" + startsAt.format(FORMATTER) + "\","
                                + "\"endsAt\":\"" + endsAt.format(FORMATTER) + "\""
                                + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SCHEDULED"))
                .andExpect(jsonPath("$.subject").value("Backend showcase walkthrough"));
    }

    @Test
    void returnsValidationErrorsForInvalidPayload() throws Exception {
        LocalDateTime startsAt = nextWeekdayAt(10, 0);
        LocalDateTime endsAt = startsAt.plusMinutes(30);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{"
                                + "\"customerName\":\"\","
                                + "\"customerEmail\":\"not-an-email\","
                                + "\"subject\":\"\","
                                + "\"startsAt\":\"" + startsAt.format(FORMATTER) + "\","
                                + "\"endsAt\":\"" + endsAt.format(FORMATTER) + "\""
                                + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed."))
                .andExpect(jsonPath("$.violations.length()").value(3));
    }

    @Test
    void returnsNotFoundForUnknownAppointmentCancellation() throws Exception {
        mockMvc.perform(delete("/api/appointments/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    private LocalDateTime nextWeekdayAt(int hour, int minute) {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return LocalDateTime.of(date, LocalTime.of(hour, minute));
    }
}
