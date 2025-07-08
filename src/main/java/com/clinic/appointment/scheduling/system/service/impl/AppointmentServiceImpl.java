package com.clinic.appointment.scheduling.system.service.impl;

import com.clinic.appointment.scheduling.system.dto.*;
import com.clinic.appointment.scheduling.system.entity.Appointment;
import com.clinic.appointment.scheduling.system.entity.AppointmentStatus;
import com.clinic.appointment.scheduling.system.entity.Feedback;
import com.clinic.appointment.scheduling.system.entity.MedicalRecord;
import com.clinic.appointment.scheduling.system.entity.User;
import com.clinic.appointment.scheduling.system.entity.User.Role;
import com.clinic.appointment.scheduling.system.exception.*;
import com.clinic.appointment.scheduling.system.repository.AppointmentRepository;
import com.clinic.appointment.scheduling.system.repository.FeedbackRepository;
import com.clinic.appointment.scheduling.system.repository.MedicalRecordRepository;
import com.clinic.appointment.scheduling.system.repository.UserRepository;
import com.clinic.appointment.scheduling.system.service.AppointmentService;
import com.clinic.appointment.scheduling.system.util.DateTimeValidator;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {

	@Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FeedbackRepository feedbackRepository;


    @Transactional
    @Override
    public AppointmentResponseDto bookAppointment(AppointmentRequestDto requestDto) throws Exception {
        // 1. Validating date/time
        DateTimeValidator.validateAppointmentDateTime(requestDto.getNewDateTime());

        // 2. Loading doctor and patient
        User doctor = userRepository.findByUsername(requestDto.getDoctorUsername())
                .orElseThrow(() -> new UserNotFoundException("Doctor not found"));
        User patient = userRepository.findByUsername(requestDto.getPatientUsername())
                .orElseThrow(() -> new UserNotFoundException("Patient not found"));

        // 3. Overlap checking (no existing SCHEDULED appointment at this time)
        validateNoOverlap(doctor, requestDto.getNewDateTime(), null);

        // 4. Create and save
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setPatient(patient);
        appointment.setDateTime(requestDto.getNewDateTime());
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointment = appointmentRepository.save(appointment);

        // 5. Mapping to DTO
        return mapToResponseDto(appointment);
    }

    @Override
    public AppointmentResponseDto cancelAppointment(Long appointmentId, String username, String role) throws Exception {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        if (!role.equals("DOCTOR") && !role.equals("PATIENT")) {
            throw new Exception("Invalid role");
        }

        // Only doctor or patient linked to appointment can cancel
        if (!appointment.getDoctor().getUsername().equals(username)
                && !appointment.getPatient().getUsername().equals(username)) {
            throw new Exception("User not authorized to cancel this appointment");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        return mapToResponseDto(appointment);
    }

    @Override
    public AppointmentResponseDto rescheduleAppointment(RescheduleAppointmentDto requestDto, String username, String role) throws Exception {
    	System.out.println(username);
    	DateTimeValidator.validateAppointmentDateTime(requestDto.getNewDateTime());
        Appointment appointment = appointmentRepository.findById(requestDto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        if (!role.equals("DOCTOR") && !role.equals("PATIENT")) {
            throw new Exception("Invalid role");
        }

        if (!appointment.getDoctor().getUsername().equals(username)
                && !appointment.getPatient().getUsername().equals(username)) {
            throw new Exception("User not authorized to reschedule this appointment");
        }

        // Only doctor can complete, but both can reschedule (so no restriction here)
        validateNoOverlap(appointment.getDoctor(), requestDto.getNewDateTime(), appointment.getAppointmentId());

        appointment.setDateTime(requestDto.getNewDateTime());
        appointmentRepository.save(appointment);

        return mapToResponseDto(appointment);
    }

    @Transactional
    @Override
    public AppointmentResponseDto completeAppointment(CompleteAppointmentDto requestDto, String doctorUsername) throws Exception {
        Appointment appointment = appointmentRepository.findById(requestDto.getAppointmentId())
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

        if (!appointment.getDoctor().getUsername().equals(doctorUsername)) {
            throw new Exception("Only assigned doctor can complete appointment");
        }
        

        if (requestDto.getDiagnosis() == null || requestDto.getDiagnosis().trim().isEmpty()
                || requestDto.getComments() == null || requestDto.getComments().trim().isEmpty()) {
            throw new Exception("Diagnosis and comments cannot be empty");
        }

        // Updating Appointment Status
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        
        // Updating Medical Record
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setComments(requestDto.getComments());
        medicalRecord.setDiagnosis(requestDto.getDiagnosis());
        medicalRecord.setAppointment(appointment);
        medicalRecordRepository.save(medicalRecord);
        return mapToResponseDto(appointment);
    }
    
    @Override
    public List<AppointmentResponseDto> getActiveAppointments(String username, String role) {
        List<Appointment> appointments;

        // Converting role to uppercase to handle case-insensitive inputs
        if (Role.PATIENT.name().equalsIgnoreCase(role)) {
            appointments = appointmentRepository
                    .findByPatientUsernameAndStatus(username, AppointmentStatus.SCHEDULED);
            
        } else if (Role.DOCTOR.name().equalsIgnoreCase(role)) {
            appointments = appointmentRepository
                    .findByDoctorUsernameAndStatus(username, AppointmentStatus.SCHEDULED);
            
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        return appointments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
    
    
   

    @Override
    public List<AppointmentResponseDto> getAppointmentHistoryForUser(String username, String role) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) return List.of();

        List<Appointment> completedAppointments;
        if ("DOCTOR".equals(role)) {
            completedAppointments = appointmentRepository.findByDoctorAndStatus(user, AppointmentStatus.COMPLETED);
        } else {
            completedAppointments = appointmentRepository.findByPatientAndStatus(user, AppointmentStatus.COMPLETED);
        }

        return completedAppointments.stream()
            .map(appointment -> {
            	
                AppointmentResponseDto dto = mapToResponseDto(appointment);

                // Finding the associated medical record.
                Optional<MedicalRecord> optMedRecord = medicalRecordRepository.findByAppointment(appointment);
                if (optMedRecord.isEmpty()) {
                    throw new UserNotFoundException("Medical Record not found with Appointment Id: " + appointment.getAppointmentId());
                }
                MedicalRecord medRecord = optMedRecord.get();
                dto.setDiagnosis(medRecord.getDiagnosis());
                dto.setComments(medRecord.getComments());

                // Retrieving the associated feedback.
                Optional<Feedback> optFeedback = feedbackRepository.findByAppointment(appointment);
                if (optFeedback.isPresent()) {
                    Feedback feedback = optFeedback.get();
                    // Setting feedback details onto the DTO.
                    dto.setFeedbackRating(feedback.getRating());
                    dto.setFeedbackComment(feedback.getFeedbackComment());
                }
                return dto;
            })
            .collect(Collectors.toList());
    }
    @Override
    public List<AppointmentResponseDto> searchCompletedAppointmentsByPatient(String doctorUsername, String patientUsername) {
        User doctor = userRepository.findByUsername(doctorUsername).orElse(null);
        User patient = userRepository.findByUsername(patientUsername).orElse(null);
        if (doctor == null || patient == null) return List.of();

        List<Appointment> appointments = appointmentRepository.findByDoctorAndPatientAndStatus(doctor, patient, AppointmentStatus.COMPLETED);

        return appointments.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }
    
 // Checking if doctor has overlapping scheduled appointment except for excludeAppointmentId (used in reschedule)
    private void validateNoOverlap(User doctor, OffsetDateTime dateTime, Long excludeAppointmentId) throws AppointmentOverlapException {
    	
        List<Appointment> overlapping = appointmentRepository.findByDoctorAndDateTimeAndStatus(doctor, dateTime, AppointmentStatus.SCHEDULED);
        for (Appointment a : overlapping) {
            if (excludeAppointmentId == null || !a.getAppointmentId().equals(excludeAppointmentId)) {
                throw new AppointmentOverlapException("Appointment time overlaps with an existing appointment");
            }
        }
    }

    private AppointmentResponseDto mapToResponseDto(Appointment appointment) {
        return AppointmentResponseDto.builder()
                .appointmentId(appointment.getAppointmentId())
                .doctorUsername(appointment.getDoctor().getUsername())
                .doctorName(appointment.getDoctor().getName())
                .patientUsername(appointment.getPatient().getUsername())
                .patientName(appointment.getPatient().getName())
                .dateTime(appointment.getDateTime())
                .status(appointment.getStatus())
                .build();
    }
}
