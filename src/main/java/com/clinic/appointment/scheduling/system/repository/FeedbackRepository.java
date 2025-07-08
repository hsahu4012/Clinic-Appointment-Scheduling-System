package com.clinic.appointment.scheduling.system.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.appointment.scheduling.system.entity.Appointment;
import com.clinic.appointment.scheduling.system.entity.Feedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
	Optional<Feedback> findByAppointment(Appointment appointment);

}
