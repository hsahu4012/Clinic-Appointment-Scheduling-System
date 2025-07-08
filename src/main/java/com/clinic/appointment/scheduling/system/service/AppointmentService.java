package com.clinic.appointment.scheduling.system.service;

import com.clinic.appointment.scheduling.system.dto.*;

import java.util.List;

public interface AppointmentService {

	// POST AND PUT Call (It may Throws Exceptions)
    AppointmentResponseDto bookAppointment(AppointmentRequestDto requestDto) throws Exception;

    AppointmentResponseDto cancelAppointment(Long appointmentId, String username, String role) throws Exception;

    AppointmentResponseDto rescheduleAppointment(RescheduleAppointmentDto requestDto, String username, String role) throws Exception;

    AppointmentResponseDto completeAppointment(CompleteAppointmentDto requestDto, String doctorUsername) throws Exception;

    // GET CALL 
    List<AppointmentResponseDto> getActiveAppointments(String patientUsername, String role);
    
//    List<AppointmentResponseDto> getActiveAppointmentsForDoctor(String doctorUsername);

    List<AppointmentResponseDto> getAppointmentHistoryForUser(String username, String role);

    List<AppointmentResponseDto> searchCompletedAppointmentsByPatient(String doctorUsername, String patientUsername);

    
}
