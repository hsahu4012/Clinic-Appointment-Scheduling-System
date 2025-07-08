package com.clinic.appointment.scheduling.system.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordDTO {
	private String username;
    private List<SecurityQuestionAnswerDTO> questions;

}
