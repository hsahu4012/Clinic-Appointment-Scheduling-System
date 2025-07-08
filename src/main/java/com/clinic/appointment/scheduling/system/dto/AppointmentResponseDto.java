package com.clinic.appointment.scheduling.system.dto;

import com.clinic.appointment.scheduling.system.entity.AppointmentStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponseDto {
    private Long appointmentId;
    private String doctorUsername;
    private String doctorName;
    private String patientUsername;
    private String patientName;
    private OffsetDateTime dateTime;
    private AppointmentStatus status;
    private String diagnosis;
    private String comments;
    private Integer feedbackRating;
    private String feedbackComment;

}
