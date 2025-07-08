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
public class RescheduleAppointmentDto {
    private Long appointmentId;
    private OffsetDateTime newDateTime;
}
