package com.clinic.appointment.scheduling.system.service;

import com.clinic.appointment.scheduling.system.dto.LoginRequestDto;
import com.clinic.appointment.scheduling.system.dto.ResetPasswordDTO;
import com.clinic.appointment.scheduling.system.dto.SecurityQuestionResponseDTO;
import com.clinic.appointment.scheduling.system.dto.SignupRequestDto;
import com.clinic.appointment.scheduling.system.dto.TopDoctorDto;

import java.util.List;

import com.clinic.appointment.scheduling.system.dto.AuthResponseDto;
import com.clinic.appointment.scheduling.system.dto.DoctorsDetails;
import com.clinic.appointment.scheduling.system.dto.ForgotPasswordDTO;

public interface AuthService {
	
	public String checkExistanceOfUsername(String username);
	
	public String checkExistanceOfEmail(String email);
	
    void signup(SignupRequestDto signupRequestDto) throws Exception;
    
    AuthResponseDto login(LoginRequestDto loginRequestDto) throws Exception;
    
    public SecurityQuestionResponseDTO verifySecurityQuestion(ForgotPasswordDTO forgotPasswordDTO);
    
    public void resetPassword(ResetPasswordDTO resetPasswordDTO); 
    
    public List<DoctorsDetails> getAllDoctors();
    public List<TopDoctorDto> getTopThreeDoctors();


}
