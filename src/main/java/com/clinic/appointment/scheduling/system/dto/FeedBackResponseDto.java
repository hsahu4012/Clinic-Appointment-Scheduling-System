package com.clinic.appointment.scheduling.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedBackResponseDto {
    private Long feedbackId;
    private Integer rating;
    private String feedbackComment;
    private String patientUsername;
    private String doctorUsername;
    private Long appointmentId;
}
