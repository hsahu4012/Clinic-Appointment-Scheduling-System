package com.clinic.appointment.scheduling.system.controller;

import com.clinic.appointment.scheduling.system.dto.*;
import com.clinic.appointment.scheduling.system.exception.IncorrectPasswordException;
import com.clinic.appointment.scheduling.system.exception.UserAlreadyExistsException;
import com.clinic.appointment.scheduling.system.exception.UserNotFoundException;
import com.clinic.appointment.scheduling.system.service.AuthService;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;
    
    

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody @Valid SignupRequestDto signupRequestDto) { 
        logger.info("Signup request received for username: {}", signupRequestDto.getUsername());
        try {
            authService.signup(signupRequestDto);
            logger.info("Signup successful for username: {}", signupRequestDto.getUsername());
            return ResponseEntity.ok("Signup successful");
        } catch (UserAlreadyExistsException ex) {
            logger.warn("Signup failed: Username already exists - {}", signupRequestDto.getUsername());
            return ResponseEntity.status(409).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Signup failed due to internal error: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
    @GetMapping("/username-check")
    public ResponseEntity<String> checkExistanceOfUsername(@RequestParam String username) {
        logger.info("Username Received");
        try {
            String checkExistance = authService.checkExistanceOfUsername(username);
            return ResponseEntity.ok(checkExistance);
        } catch (Exception ex) {
            logger.error("Usename check failed due to internal error: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Internal server error");
        }
    }
    
    @GetMapping("/email-check")
    public ResponseEntity<String> checkExistanceOfEmail(@RequestParam String email) {
        logger.info("Username Received");
        try {
        	String checkExistance = authService.checkExistanceOfEmail(email);
            return ResponseEntity.ok(checkExistance);
        } catch (Exception ex) {
            logger.error("Email check failed due to internal error: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        logger.info("Login request received for username: {}", loginRequestDto.getUsername());
        try {
            AuthResponseDto authResponse = authService.login(loginRequestDto);
            logger.info("Login successful for username: {}", loginRequestDto.getUsername());
            return ResponseEntity.ok(authResponse);
        } catch (UserNotFoundException ex) {
            logger.warn("Login failed: Username not found - {}", loginRequestDto.getUsername());
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (IncorrectPasswordException ex) {
            logger.warn("Login failed: Incorrect password for username - {}", loginRequestDto.getUsername());
            return ResponseEntity.status(404).body(ex.getMessage());
        } catch (Exception ex) {
            logger.error("Login failed due to internal error: {}", ex.getMessage());
            return ResponseEntity.status(500).body("Internal server error");
        }
    }

    @PostMapping("/verify-security-question")
    public ResponseEntity<SecurityQuestionResponseDTO> verifySecurityQuestion(@RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        
        logger.info("Security question verification request received for username: {}", forgotPasswordDTO.getUsername());
        
        SecurityQuestionResponseDTO response = authService.verifySecurityQuestion(forgotPasswordDTO);
        
        logger.info("Security question verification result: {} for username: {}", response, forgotPasswordDTO.getUsername());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/update-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        logger.info("Password reset request received for username: {}", resetPasswordDTO.getUsername());
        authService.resetPassword(resetPasswordDTO);
        logger.info("Password reset successful for username: {}", resetPasswordDTO.getUsername());
        return ResponseEntity.ok("Password successfully updated");
    }
    
    @GetMapping("/top")
    public List<TopDoctorDto> getTopDoctors() {
        return authService.getTopThreeDoctors();
    }


    @GetMapping("/getdoctor")
    public List<DoctorsDetails> getDoctors() {
        logger.info("Fetching doctor details");
        List<DoctorsDetails> doctors = authService.getAllDoctors();
        logger.info("Doctor details fetched successfully, total doctors: {}", doctors.size());
        return doctors;
    }
}