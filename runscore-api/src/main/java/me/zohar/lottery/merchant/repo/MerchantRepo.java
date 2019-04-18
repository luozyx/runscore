package me.zohar.lottery.merchant.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.merchant.domain.Merchant;

public interface MerchantRepo extends JpaRepository<Merchant, String>, JpaSpecificationExecutor<Merchant> {
	
	Merchant findBySecretKey(String secretKey);

}
