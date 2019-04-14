package me.zohar.lottery.platform.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.platform.domain.TodayReceiveOrderSituation;

public interface TodayReceiveOrderSituationRepo extends JpaRepository<TodayReceiveOrderSituation, String>, JpaSpecificationExecutor<TodayReceiveOrderSituation> {
	
	List<TodayReceiveOrderSituation> findTop10ByOrderByTotalBountyDesc();

}
