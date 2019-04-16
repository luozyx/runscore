package me.zohar.lottery.statisticalanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.statisticalanalysis.domain.TodayStatistical;

public interface TodayStatisticalRepo
		extends JpaRepository<TodayStatistical, String>, JpaSpecificationExecutor<TodayStatistical> {

	TodayStatistical findTopBy();
}
