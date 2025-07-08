package com.clinic.appointment.scheduling.system.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FeedbackRequestDto {
    
    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating cannot be more than 5")
    private Integer rating;
    
    // Optional comment field
    private String feedbackComment;
    
    @NotNull(message = "Patient username is required")
    private String patientUsername;
    
    @NotNull(message = "Doctor username is required")
    private String doctorUsername;
    
    private Long appointmentId;
}
