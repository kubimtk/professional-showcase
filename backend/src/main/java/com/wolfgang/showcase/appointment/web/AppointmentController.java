package com.wolfgang.showcase.appointment.web;

import com.wolfgang.showcase.appointment.dto.AppointmentResponse;
import com.wolfgang.showcase.appointment.dto.CreateAppointmentRequest;
import com.wolfgang.showcase.appointment.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/appointments")
@Tag(name = "Appointments", description = "Appointment scheduling showcase API")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    @Operation(summary = "List appointments", description = "Returns all appointments sorted by start time.")
    public List<AppointmentResponse> listAppointments() {
        return appointmentService.listAppointments().stream()
                .map(AppointmentResponse::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get one appointment", description = "Returns a single appointment by identifier.")
    public AppointmentResponse getAppointment(@PathVariable UUID id) {
        return new AppointmentResponse(appointmentService.getAppointment(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create appointment", description = "Creates a new appointment if business rules are satisfied.")
    public AppointmentResponse createAppointment(@Valid @RequestBody CreateAppointmentRequest request) {
        return new AppointmentResponse(
                appointmentService.createAppointment(
                        request.getCustomerName(),
                        request.getCustomerEmail(),
                        request.getSubject(),
                        request.getStartsAt(),
                        request.getEndsAt()
                )
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Cancel appointment", description = "Cancels an existing appointment.")
    public AppointmentResponse cancelAppointment(@PathVariable UUID id) {
        return new AppointmentResponse(appointmentService.cancelAppointment(id));
    }
}
