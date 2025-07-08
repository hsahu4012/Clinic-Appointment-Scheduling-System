package com.clinic.appointment.scheduling.system.service;


import java.util.List;

public interface SecurityQuestionService {

	// To get list in signup
    public List<String> getAllSecurityQuestions();
    // For fotgot password
    public List<String> getSecurityQuestionsOfUser(String username);
}