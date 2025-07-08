package com.clinic.appointment.scheduling.system.controller;

import com.clinic.appointment.scheduling.system.dto.*;
import com.clinic.appointment.scheduling.system.exception.AppointmentOverlapException;
import com.clinic.appointment.scheduling.system.service.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    //patientUsername is passed in body with AppointmentRequestDto for booking
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequestDto requestDto) {
        try {
            AppointmentResponseDto response = appointmentService.bookAppointment(requestDto);
            return ResponseEntity.ok(response);
        } catch (AppointmentOverlapException e) {
            return ResponseEntity.status(409).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Internal server error");
        }
    }


    @PutMapping("/cancel/{id}")
    public ResponseEntity<?> cancelAppointment(@PathVariable("id") Long appointmentId,
                                               @RequestParam String username,
                                               @RequestParam String role) {
        try {
            AppointmentResponseDto response = appointmentService.cancelAppointment(appointmentId, username, role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/reschedule")
    public ResponseEntity<?> rescheduleAppointment(@RequestBody RescheduleAppointmentDto requestDto,
                                                   @RequestParam String username,
                                                   @RequestParam String role) {
        try {
            AppointmentResponseDto response = appointmentService.rescheduleAppointment(requestDto, username, role);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/complete")
    public ResponseEntity<?> completeAppointment(@RequestBody CompleteAppointmentDto requestDto,
                                                 @RequestParam String doctorUsername) {
        try {
            AppointmentResponseDto response = appointmentService.completeAppointment(requestDto, doctorUsername);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/active")
    public ResponseEntity<?> getActiveAppointments(
            @RequestParam String username, @RequestParam String role ) {
        List<AppointmentResponseDto> appointments = appointmentService.getActiveAppointments(username, role);
        return ResponseEntity.ok(appointments);
    }


//    @GetMapping("/doctor/active")
//    public ResponseEntity<?> getActiveAppointmentsForDoctor(@RequestParam String doctorUsername) {
//        List<AppointmentResponseDto> activeList = appointmentService.getActiveAppointmentsForDoctor(doctorUsername);
//        return ResponseEntity.ok(activeList);
//    }

    @GetMapping("/history")
    public ResponseEntity<?> getAppointmentHistory(@RequestParam String username,
                                                   @RequestParam String role) {
        List<AppointmentResponseDto> history = appointmentService.getAppointmentHistoryForUser(username, role);
        System.out.println(history);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/doctor/search")
    public ResponseEntity<?> searchCompletedAppointmentsByPatient(@RequestParam String doctorUsername,
                                                                  @RequestParam String patientUsername) {
        List<AppointmentResponseDto> results = appointmentService.searchCompletedAppointmentsByPatient(doctorUsername, patientUsername);
        return ResponseEntity.ok(results);
    }
}
