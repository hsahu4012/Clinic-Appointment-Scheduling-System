package com.clinic.appointment.scheduling.system.repository;

import com.clinic.appointment.scheduling.system.entity.Appointment;
import com.clinic.appointment.scheduling.system.entity.MedicalRecord;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
	@Query("SELECT mr FROM MedicalRecord mr WHERE mr.appointment.appointmentId = :appointmentId")
	List<MedicalRecord> findByAppointmentId(@Param("appointmentId") Long appointmentId);
	Optional<MedicalRecord> findByAppointment(Appointment appointment);
}
