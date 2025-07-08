package com.clinic.appointment.scheduling.system.controller;

import com.clinic.appointment.scheduling.system.dto.FeedBackResponseDto;
import com.clinic.appointment.scheduling.system.dto.FeedbackRequestDto;
import com.clinic.appointment.scheduling.system.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;
    
    @PostMapping("/feedback")
    public ResponseEntity<?> saveFeedback(@RequestBody @Valid FeedbackRequestDto feedbackRequestDto) {
        try {
        	System.out.println(feedbackRequestDto);
            FeedBackResponseDto savedFeedback = feedbackService.saveFeedback(feedbackRequestDto);
            System.out.println(savedFeedback);
            
            return ResponseEntity.ok(savedFeedback);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}