package com.wolfgang.showcase.appointment.repository;

import com.wolfgang.showcase.appointment.model.Appointment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class InMemoryAppointmentRepository implements AppointmentRepository {

    private final ConcurrentMap<UUID, Appointment> storage = new ConcurrentHashMap<>();

    @Override
    public Appointment save(Appointment appointment) {
        storage.put(appointment.getId(), appointment);
        return appointment;
    }

    @Override
    public Optional<Appointment> findById(UUID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Appointment> findAll() {
        List<Appointment> appointments = new ArrayList<>(storage.values());
        appointments.sort(Comparator.comparing(Appointment::getStartsAt));
        return appointments;
    }
}
