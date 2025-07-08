package com.clinic.appointment.scheduling.system.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.clinic.appointment.scheduling.system.entity.SecurityQuestionEntity;
import com.clinic.appointment.scheduling.system.entity.SecurityQuestionList;
import com.clinic.appointment.scheduling.system.repository.SecurityQuestionRepository;
import com.clinic.appointment.scheduling.system.service.SecurityQuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class SecurityQuestionServiceImpl implements SecurityQuestionService {
	
	@Autowired
	private SecurityQuestionRepository securityQuestionRepository;

    private static final Logger logger = LoggerFactory.getLogger(SecurityQuestionServiceImpl.class);

    @Override
    public List<String> getAllSecurityQuestions() {
        try {
            logger.info("Fetching security questions");

            List<String> securityQuestions = Arrays.stream(SecurityQuestionList.values())
                    .map(SecurityQuestionList::getQuestion)
                    .toList();

            if (securityQuestions.isEmpty()) {
                logger.warn("Security questions list is empty");
                throw new RuntimeException("No security questions available");
            }

            logger.info("Successfully fetched {} security questions", securityQuestions.size());
            return securityQuestions;

        } catch (Exception e) {
            logger.error("Error retrieving security questions: {}", e.getMessage());
            throw new RuntimeException("Internal server error while fetching security questions");
        }
    }
    @Override
    public List<String> getSecurityQuestionsOfUser(String username) {
        List<SecurityQuestionEntity> secQuesList = securityQuestionRepository.findByUsername(username);
        List<String> questions = secQuesList.stream()
                .map(SecurityQuestionEntity::getSecurityQuestion)
                .collect(Collectors.toList());

        return questions.isEmpty() ? Collections.emptyList() : questions;

    }
}