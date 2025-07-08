package com.clinic.appointment.scheduling.system.service;

import com.clinic.appointment.scheduling.system.entity.MedicalRecord;

public interface MedicalRecordService {
    // Medical Record save by doctor
    public MedicalRecord saveMedicalRecord(Long appointmentId, MedicalRecord medicalRecord);

}