package me.zohar.lottery.statisticalanalysis.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.statisticalanalysis.domain.MonthStatistical;

public interface MonthStatisticalRepo extends JpaRepository<MonthStatistical, String>, JpaSpecificationExecutor<MonthStatistical>  {

	MonthStatistical findTopBy();
	
}
