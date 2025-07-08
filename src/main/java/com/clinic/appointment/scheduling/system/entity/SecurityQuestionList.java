package com.clinic.appointment.scheduling.system.entity;

public enum SecurityQuestionList {
    FAVORITE_SPORTS("What is your favorite sports name?"),
    FIRST_PET("What was the name of your first pet?"),
    FIRST_SCHOOL("What was the name of your first school?"),
    FAVORITE_MOVIE("What is your favorite movie?"),
    BIRTH_CITY("In which city were you born?"),
    MOTHERS_MAIDEN_NAME("What is your mother's maiden name?"),
    CHILDHOOD_BEST_FRIEND("What was the name of your childhood best friend?"),
    FIRST_CAR_MODEL("What was your first car model?"),
    FAVORITE_TEACHER("What is the name of your favorite teacher?"),
    FIRST_JOB_TITLE("What was your first job title?");
	
	private final String question;

    SecurityQuestionList(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

}
