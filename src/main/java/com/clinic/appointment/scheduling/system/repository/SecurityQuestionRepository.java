package com.clinic.appointment.scheduling.system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.clinic.appointment.scheduling.system.entity.SecurityQuestionEntity;
import com.clinic.appointment.scheduling.system.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SecurityQuestionRepository extends JpaRepository<SecurityQuestionEntity, Long> {
	
	List<SecurityQuestionEntity> findByUser(User user);

	@Query("SELECT sq FROM SecurityQuestionEntity sq WHERE sq.user.id = :userId")
    List<SecurityQuestionEntity> findByUserId(@Param("userId") Long userId);
	
	@Query("SELECT sq FROM SecurityQuestionEntity sq WHERE sq.user.id = (SELECT u.id FROM User u WHERE u.username = :username)")
	List<SecurityQuestionEntity> findByUsername(@Param("username") String username);

	// Find by user ID and security question
    @Query("SELECT sq FROM SecurityQuestionEntity sq WHERE sq.user.id = :userId AND sq.securityQuestion = :securityQuestion")
    Optional<SecurityQuestionEntity> findByUserIdAndSecurityQuestion(@Param("userId") Long userId, @Param("securityQuestion") String securityQuestion);
}
