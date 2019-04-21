package me.zohar.lottery.statisticalanalysis.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.statisticalanalysis.domain.TotalAccountReceiveOrderSituation;

public interface TotalAccountReceiveOrderSituationRepo
		extends JpaRepository<TotalAccountReceiveOrderSituation, String>, JpaSpecificationExecutor<TotalAccountReceiveOrderSituation> {

	List<TotalAccountReceiveOrderSituation> findTop10ByOrderByBountyDesc();
	
	TotalAccountReceiveOrderSituation findByReceivedAccountId(String receivedAccountId);
}
