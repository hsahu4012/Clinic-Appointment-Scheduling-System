package com.clinic.appointment.scheduling.system.service.impl;

import com.clinic.appointment.scheduling.system.dto.LoginRequestDto;
import com.clinic.appointment.scheduling.system.dto.ResetPasswordDTO;
import com.clinic.appointment.scheduling.system.dto.SecurityQuestionAnswerDTO;
import com.clinic.appointment.scheduling.system.dto.SecurityQuestionResponseDTO;
import com.clinic.appointment.scheduling.system.dto.SignupRequestDto;
import com.clinic.appointment.scheduling.system.dto.TopDoctorDto;
import com.clinic.appointment.scheduling.system.config.BcryptConfig;
import com.clinic.appointment.scheduling.system.dto.AuthResponseDto;
import com.clinic.appointment.scheduling.system.dto.DoctorsDetails;
import com.clinic.appointment.scheduling.system.dto.ForgotPasswordDTO;
import com.clinic.appointment.scheduling.system.entity.SecurityQuestionEntity;
import com.clinic.appointment.scheduling.system.entity.User;
import com.clinic.appointment.scheduling.system.entity.User.Role;
import com.clinic.appointment.scheduling.system.exception.IncorrectPasswordException;
import com.clinic.appointment.scheduling.system.exception.UserAlreadyExistsException;
import com.clinic.appointment.scheduling.system.exception.UserNotFoundException;
import com.clinic.appointment.scheduling.system.repository.SecurityQuestionRepository;
import com.clinic.appointment.scheduling.system.repository.UserRepository;
import com.clinic.appointment.scheduling.system.service.AuthService;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
    private SecurityQuestionRepository securityQuestionRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    BcryptConfig bcryptConfig;
    
    @Override
    public String checkExistanceOfUsername(String username){
    	Optional<User> existingUserName = userRepository.findByUsername(username);
        
        if (existingUserName.isPresent()) {
            return "Username already exists";
        }
        else {
        	return "Username Available";
        }
    }
    
    @Override
    public String checkExistanceOfEmail(String email){
    	Optional<User> existingEmail = userRepository.findByEmail(email);
    	System.out.println("Email is checking " + email);
        if (existingEmail.isPresent()) {
            return "Email already registered";
        }
        else {
        	return "Email Available";
        }
    }

    @Transactional
    @Override
    public void signup(SignupRequestDto signupRequestDto) throws UserAlreadyExistsException {
        
        // Checking if username already exists
        Optional<User> existingUserName = userRepository.findByUsername(signupRequestDto.getUsername());
        
        if (existingUserName.isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
        }

        // Checking if email already exists
        Optional<User> existingUser = userRepository.findByEmail(signupRequestDto.getEmail());
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("Email already registered");
        }
        

        // Creating new user
        User user = new User();
        user.setUsername(signupRequestDto.getUsername());
        user.setName(signupRequestDto.getName());
        user.setEmail(signupRequestDto.getEmail());
        user.setPhoneNumber(signupRequestDto.getPhoneNumber());
        user.setRole(signupRequestDto.getRole());

        // Hash password before saving
        String hashedPassword = bcryptConfig.getBcrypt().encode(signupRequestDto.getPassword());
        user.setPassword(hashedPassword);
        System.out.println("Hello");
        System.out.println(user);
        try {
            User savedUser = userRepository.save(user);
            System.out.println("Security Questions Data: " + signupRequestDto.getSecurityQuestions());
            // Save Security Questions & Answers
            List<SecurityQuestionEntity> securityQuestionEntities = signupRequestDto.getSecurityQuestions().stream()
                    .map(dto -> SecurityQuestionEntity.builder()
                        .securityQuestion(dto.getSecurityQuestion())
                        .securityAnswer(dto.getSecurityAnswer() != null ? dto.getSecurityAnswer() : "")
                        .user(savedUser)
                        .build())
                    .toList();
            System.out.println("Hii"+securityQuestionEntities);

                securityQuestionRepository.saveAll(securityQuestionEntities);


        } catch (Exception e) {
            throw new RuntimeException("Error occurred while signing up: " + e.getMessage());
        }
    }
    
    @Transactional
    @Override
    public AuthResponseDto login(LoginRequestDto loginRequestDto) throws UserNotFoundException {
    	
        Optional<User> userOpt = userRepository.findByUsername(loginRequestDto.getUsername());
        if (userOpt.isEmpty()) {
            throw new UserNotFoundException("User not found with Username: " + loginRequestDto.getUsername());
        }
        
        User user = userOpt.get();

        BCryptPasswordEncoder encoder = bcryptConfig.getBcrypt();

        // to compare raw password with hashed password
        if (!encoder.matches(loginRequestDto.getPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Invalid password");
        }
        

        AuthResponseDto responseDto = new AuthResponseDto();
        responseDto.setUsername(user.getUsername());
        responseDto.setMessage("Login successful");
        responseDto.setRole(user.getRole());
        responseDto.setName(user.getName());
        
        System.out.println(responseDto);
        return responseDto;
    }
    
    @Override
    public SecurityQuestionResponseDTO verifySecurityQuestion(ForgotPasswordDTO forgotPasswordDTO) {
    	System.out.println(forgotPasswordDTO);
        // Check if username exists
        User user = userRepository.findByUsername(forgotPasswordDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Username not found"));

        // Retrieving the list of provided security questions and answers.
        List<SecurityQuestionAnswerDTO> providedQuestions = forgotPasswordDTO.getQuestions();
        
        // Verifying each provided security question and its corresponding answer.
        for (SecurityQuestionAnswerDTO provided : providedQuestions) {
            SecurityQuestionEntity securityQuestion = securityQuestionRepository
                    .findByUserIdAndSecurityQuestion(user.getId(), provided.getQuestion())
                    .orElseThrow(() -> new RuntimeException("Security question not linked to this user: " + provided.getQuestion()));

            // Case-insensitive comparison for the answer.
            if (!securityQuestion.getSecurityAnswer().equalsIgnoreCase(provided.getAnswer())) {
                throw new RuntimeException("Incorrect security answers");
            }
        }

        // If all answers are verified -success.
        SecurityQuestionResponseDTO response = new SecurityQuestionResponseDTO();
        response.setUsername(user.getUsername());
        response.setVerified(true);

        return response;
    }
    
    
    @Override
    public void resetPassword(ResetPasswordDTO resetPasswordDTO) {
        // Checking if verification is done
        if (!resetPasswordDTO.isVerified()) {
            throw new RuntimeException("Security question verification required before password reset");
        }

        // Checking if username exists
        User user = userRepository.findByUsername(resetPasswordDTO.getUsername())
                .orElseThrow(() -> new RuntimeException("Username not found"));

        BCryptPasswordEncoder encoder = bcryptConfig.getBcrypt();

        // to compare raw password with hashed password
        if (encoder.matches(resetPasswordDTO.getNewPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Please Don't use Old Password");
        }
        
        // Hash password before saving
        String hashedPassword = bcryptConfig.getBcrypt().encode(resetPasswordDTO.getNewPassword());
        user.setPassword(hashedPassword);

        // Save updated user
        userRepository.save(user);
    }

    public List<TopDoctorDto> getTopThreeDoctors() {
        return userRepository.findTopThreeDoctors();
    }


    
    // To Show doctor list when patient will try to book an appointment
    public List<DoctorsDetails> getAllDoctors() {
        List<User> doctors = userRepository.findByRole(Role.DOCTOR);

        // Mapping User entities to DoctorsDetails
        return doctors.stream()
                .map(user -> new DoctorsDetails(user.getUsername(), user.getName()))
                .collect(Collectors.toList());
    }

}
