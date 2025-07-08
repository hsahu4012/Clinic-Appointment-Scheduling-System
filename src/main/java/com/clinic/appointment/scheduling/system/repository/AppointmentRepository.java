package com.clinic.appointment.scheduling.system.repository;

import com.clinic.appointment.scheduling.system.entity.Appointment;
import com.clinic.appointment.scheduling.system.entity.AppointmentStatus;
import com.clinic.appointment.scheduling.system.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Check overlapping scheduled appointment for a doctor at a given dateTime
    List<Appointment> findByDoctorAndDateTimeAndStatus(User doctor, OffsetDateTime dateTime, AppointmentStatus status);

    // Find appointments for doctor by status (SCHEDULED, COMPLETED, CANCELLED)
    List<Appointment> findByDoctorAndStatus(User doctor, AppointmentStatus status);

    // Find appointments for patient by status
    List<Appointment> findByPatientAndStatus(User patient, AppointmentStatus status);

    // Find completed appointments for given doctor and patient
    List<Appointment> findByDoctorAndPatientAndStatus(User doctor, User patient, AppointmentStatus status);
    
    // Find active appointments for given doctor and patient
    List<Appointment> findByPatientUsernameAndStatus(String patientUsername, AppointmentStatus status);
	List<Appointment> findByDoctorUsernameAndStatus(String username, AppointmentStatus status);

	 

}
