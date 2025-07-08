package com.clinic.appointment.scheduling.system.dto;

import java.time.OffsetDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequestDto {
	private String patientUsername;
    private String doctorUsername;
    private OffsetDateTime newDateTime;
}
