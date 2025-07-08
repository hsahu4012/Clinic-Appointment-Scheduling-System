package com.clinic.appointment.scheduling.system.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TopDoctorDto {
    private String name;
    private String username;
    private Long completedAppointments;
    private Double averageRating;
}