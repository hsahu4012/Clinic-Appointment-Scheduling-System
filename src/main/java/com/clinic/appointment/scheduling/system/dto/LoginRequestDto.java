package com.clinic.appointment.scheduling.system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

	 @NotBlank(message = "Username is required")
	 @Size(min = 3, max = 30, message = "Username must be between 3 and 30 characters")
	 private String username;

     @NotBlank(message = "Password is required")
     private String password;
}
