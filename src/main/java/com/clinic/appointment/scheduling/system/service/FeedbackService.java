package com.clinic.appointment.scheduling.system.service;

import com.clinic.appointment.scheduling.system.dto.FeedBackResponseDto;
import com.clinic.appointment.scheduling.system.dto.FeedbackRequestDto;
import com.clinic.appointment.scheduling.system.entity.Feedback;

public interface FeedbackService {
	FeedBackResponseDto saveFeedback(FeedbackRequestDto feedbackRequestDto);

}
