package com.clinic.appointment.scheduling.system.service.impl;

import com.clinic.appointment.scheduling.system.dto.FeedbackRequestDto;
import com.clinic.appointment.scheduling.system.dto.FeedBackResponseDto;
import com.clinic.appointment.scheduling.system.entity.Appointment;
import com.clinic.appointment.scheduling.system.entity.Feedback;
import com.clinic.appointment.scheduling.system.entity.User;
import com.clinic.appointment.scheduling.system.repository.AppointmentRepository;
import com.clinic.appointment.scheduling.system.repository.FeedbackRepository;
import com.clinic.appointment.scheduling.system.repository.UserRepository;
import com.clinic.appointment.scheduling.system.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Override
    public FeedBackResponseDto saveFeedback(FeedbackRequestDto feedbackRequestDto) {
        // Retrieve the patient
        User patient = userRepository.findByUsername(feedbackRequestDto.getPatientUsername())
                .orElseThrow(() -> new RuntimeException("Patient not found"));
                
        // Retrieve the doctor
        User doctor = userRepository.findByUsername(feedbackRequestDto.getDoctorUsername())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        
        // Retrieve appointment if appointment id is provided
        Appointment appointment = null;
        if (feedbackRequestDto.getAppointmentId() != null) {
            appointment = appointmentRepository.findById(feedbackRequestDto.getAppointmentId())
                    .orElseThrow(() -> new RuntimeException("Appointment not found"));
        }
        System.out.println(feedbackRequestDto.getDoctorUsername());
        
        // Build and save the feedback entity.
        Feedback feedback = Feedback.builder()
                .rating(feedbackRequestDto.getRating())
                .feedbackComment(feedbackRequestDto.getFeedbackComment())
                .patient(patient)
                .doctor(doctor)
                .appointment(appointment)
                .build();
        
        Feedback savedFeedback = feedbackRepository.save(feedback);
        
        // Building and return the response DTO.
        return FeedBackResponseDto.builder()
                .feedbackId(savedFeedback.getFeedbackId())
                .rating(savedFeedback.getRating())
                .feedbackComment(savedFeedback.getFeedbackComment())
                .patientUsername(savedFeedback.getPatient().getUsername())
                .doctorUsername(savedFeedback.getDoctor().getUsername())
                .appointmentId(savedFeedback.getAppointment() != null ? savedFeedback.getAppointment().getAppointmentId() : null)
                .build();
    }
}