package me.zohar.lottery.merchant.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.merchant.domain.MerchantOrder;

public interface MerchantOrderRepo
		extends JpaRepository<MerchantOrder, String>, JpaSpecificationExecutor<MerchantOrder> {

	List<MerchantOrder> findTop10ByOrderStateAndGatheringAmountIsLessThanEqualAndGatheringChannelCodeInOrderBySubmitTimeDesc(String orderState,
			Double gatheringAmount, List<String> gatheringChannelCodes);

	List<MerchantOrder> findByOrderStateInAndReceivedAccountIdOrderBySubmitTimeDesc(List<String> orderStates,
			String receivedAccountId);
	
	MerchantOrder findByIdAndReceivedAccountId(String id, String receivedAccountId);

}
