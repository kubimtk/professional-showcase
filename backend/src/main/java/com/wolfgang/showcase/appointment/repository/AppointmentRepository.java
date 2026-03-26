package com.wolfgang.showcase.appointment.repository;

import com.wolfgang.showcase.appointment.model.Appointment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentRepository {

    Appointment save(Appointment appointment);

    Optional<Appointment> findById(UUID id);

    List<Appointment> findAll();
}
