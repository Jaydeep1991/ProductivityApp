package com.aroha.pet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.aroha.pet.model.LoginLogoutTime;
import com.aroha.pet.model.User;

@Repository
public interface LoginLogoutTimeRepository extends JpaRepository<LoginLogoutTime, Long> {
	
	public User findByuserId(Long userId);
}
