package com.clinic.appointment.scheduling.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SecurityQuestionResponseDTO {
	private String username;
	private boolean isVerified;
}
