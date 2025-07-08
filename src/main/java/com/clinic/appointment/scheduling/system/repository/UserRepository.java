package com.clinic.appointment.scheduling.system.repository;

import com.clinic.appointment.scheduling.system.dto.TopDoctorDto;
import com.clinic.appointment.scheduling.system.entity.User;
import com.clinic.appointment.scheduling.system.entity.User.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Find user by username (unique)
    Optional<User> findByUsername(String username);

    // Find user by email (unique)
    Optional<User> findByEmail(String email); 

    // Check existence by username
    boolean existsByUsername(String username);

    // Check existence by email
    boolean existsByEmail(String email);
    
    // to get list of doctor
    List<User> findByRole(Role role);
    
    @Query("SELECT new com.clinic.appointment.scheduling.system.dto.TopDoctorDto(d.name, d.username, COUNT(DISTINCT a), AVG(f.rating)) " +
    	       "FROM User d " +
    	       "JOIN Appointment a ON a.doctor = d " +
    	       "LEFT JOIN Feedback f ON f.doctor = d " +
    	       "WHERE d.role = 'DOCTOR' AND a.status = 'COMPLETED' " +
    	       "GROUP BY d.id " +
    	       "ORDER BY COUNT(DISTINCT a) DESC " +
    	       "LIMIT 3")

	    List<TopDoctorDto> findTopThreeDoctors();
    
}
