package me.zohar.lottery.gatheringcode.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.gatheringcode.domain.GatheringCode;

public interface GatheringCodeRepo
		extends JpaRepository<GatheringCode, String>, JpaSpecificationExecutor<GatheringCode> {

	GatheringCode findByUserAccountIdAndGatheringChannelCodeAndGatheringAmount(String userAccountId,
			String gatheringChannelCode, Double gatheringAmount);

}
