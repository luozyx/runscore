package me.zohar.lottery.statisticalanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.statisticalanalysis.domain.TotalCashDeposit;

public interface TotalCashDepositRepo
		extends JpaRepository<TotalCashDeposit, String>, JpaSpecificationExecutor<TotalCashDeposit> {
	
	TotalCashDeposit findTopBy();

}
