package me.zohar.lottery.statisticalanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.statisticalanalysis.domain.TotalStatistical;

public interface TotalStatisticalRepo
		extends JpaRepository<TotalStatistical, String>, JpaSpecificationExecutor<TotalStatistical> {

	TotalStatistical findTopBy();
	
}
