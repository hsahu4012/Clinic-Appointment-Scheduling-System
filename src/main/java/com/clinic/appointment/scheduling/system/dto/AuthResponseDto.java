package com.clinic.appointment.scheduling.system.dto;

import com.clinic.appointment.scheduling.system.entity.User.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuthResponseDto {
    private String username;
    private String message;
    private Role role;
    private String name;
}
