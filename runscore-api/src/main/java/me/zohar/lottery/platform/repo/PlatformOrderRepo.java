package me.zohar.lottery.platform.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.platform.domain.PlatformOrder;

public interface PlatformOrderRepo
		extends JpaRepository<PlatformOrder, String>, JpaSpecificationExecutor<PlatformOrder> {

	List<PlatformOrder> findByOrderStateAndGatheringAmountIsLessThanEqualOrderBySubmitTimeDesc(String orderState,
			Double gatheringAmount);

	List<PlatformOrder> findByOrderStateInAndReceivedAccountIdOrderBySubmitTimeDesc(List<String> orderStates,
			String receivedAccountId);

}
