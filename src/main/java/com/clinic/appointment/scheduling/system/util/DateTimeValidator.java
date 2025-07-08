package com.clinic.appointment.scheduling.system.util;

import java.time.OffsetDateTime;

public class DateTimeValidator {
    public static void validateAppointmentDateTime(OffsetDateTime dateTime) {
        if (dateTime == null) {
            throw new IllegalArgumentException("Appointment date and time cannot be null");
        }
        if (!dateTime.isAfter(OffsetDateTime.now())) {
            throw new IllegalArgumentException("Appointment date and time must be in the future");
        }
    }
}
