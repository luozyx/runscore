package me.zohar.lottery.platform.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.platform.domain.Platform;

public interface PlatformRepo extends JpaRepository<Platform, String>, JpaSpecificationExecutor<Platform> {
	
	Platform findBySecretKey(String secretKey);

}
