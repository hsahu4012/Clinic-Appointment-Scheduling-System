package com.clinic.appointment.scheduling.system.controller;

import com.clinic.appointment.scheduling.system.service.SecurityQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/security-questions")
public class SecurityQuestionController {

    private static final Logger logger = LoggerFactory.getLogger(SecurityQuestionController.class);

    @Autowired
    private SecurityQuestionService securityQuestionService;

    @GetMapping("/list")
    public ResponseEntity<List<String>> getSecurityQuestions() {
        logger.info("Received request to fetch security questions");

        List<String> securityQuestions = securityQuestionService.getAllSecurityQuestions();

        logger.info("Successfully fetched {} security questions", securityQuestions.size());
        return ResponseEntity.ok(securityQuestions);
    }
    
    @GetMapping("/{username}")
    public ResponseEntity<List<String>> getUserSecurityQuestions(@PathVariable String username) {
        List<String> questions = securityQuestionService.getSecurityQuestionsOfUser(username);

        if (questions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(questions); 
    }

}