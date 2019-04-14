package me.zohar.lottery.platform.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.platform.domain.ReceiveOrderSituation;

public interface ReceiveOrderSituationRepo
		extends JpaRepository<ReceiveOrderSituation, String>, JpaSpecificationExecutor<ReceiveOrderSituation> {

	List<ReceiveOrderSituation> findTop10ByOrderByTotalBountyDesc();
}
