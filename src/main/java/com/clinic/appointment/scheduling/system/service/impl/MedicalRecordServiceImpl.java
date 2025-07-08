package com.clinic.appointment.scheduling.system.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.clinic.appointment.scheduling.system.entity.Appointment;
import com.clinic.appointment.scheduling.system.entity.MedicalRecord;
import com.clinic.appointment.scheduling.system.repository.AppointmentRepository;
import com.clinic.appointment.scheduling.system.repository.MedicalRecordRepository;
import com.clinic.appointment.scheduling.system.service.MedicalRecordService;

public class MedicalRecordServiceImpl implements MedicalRecordService {
	@Autowired
    private MedicalRecordRepository medicalRecordRepository;
	@Autowired
	private AppointmentRepository appointmentRepository;

	
    // Medical Record save by doctor
	@Override
    public MedicalRecord saveMedicalRecord(Long appointmentId, MedicalRecord medicalRecord) {
        Optional<Appointment> appointmentOpt = appointmentRepository.findById(appointmentId);
        if (appointmentOpt.isPresent()) {
            medicalRecord.setAppointment(appointmentOpt.get());
            return medicalRecordRepository.save(medicalRecord);
        } else {
            throw new RuntimeException("Appointment not found!");
        }
	}
}
