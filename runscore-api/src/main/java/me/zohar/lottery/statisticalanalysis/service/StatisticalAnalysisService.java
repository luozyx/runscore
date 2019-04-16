package me.zohar.lottery.statisticalanalysis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alicp.jetcache.anno.Cached;

import cn.hutool.core.util.NumberUtil;
import me.zohar.lottery.statisticalanalysis.domain.MonthStatistical;
import me.zohar.lottery.statisticalanalysis.domain.TodayAccountReceiveOrderSituation;
import me.zohar.lottery.statisticalanalysis.domain.TodayStatistical;
import me.zohar.lottery.statisticalanalysis.domain.TotalCashDeposit;
import me.zohar.lottery.statisticalanalysis.domain.TotalAccountReceiveOrderSituation;
import me.zohar.lottery.statisticalanalysis.domain.TotalStatistical;
import me.zohar.lottery.statisticalanalysis.repo.MonthStatisticalRepo;
import me.zohar.lottery.statisticalanalysis.repo.TodayAccountReceiveOrderSituationRepo;
import me.zohar.lottery.statisticalanalysis.repo.TodayStatisticalRepo;
import me.zohar.lottery.statisticalanalysis.repo.TotalCashDepositRepo;
import me.zohar.lottery.statisticalanalysis.repo.TotalAccountReceiveOrderSituationRepo;
import me.zohar.lottery.statisticalanalysis.repo.TotalStatisticalRepo;
import me.zohar.lottery.statisticalanalysis.vo.BountyRankVO;
import me.zohar.lottery.statisticalanalysis.vo.IndexStatisticalVO;

@Service
public class StatisticalAnalysisService {

	@Autowired
	private TotalAccountReceiveOrderSituationRepo totalAccountReceiveOrderSituationRepo;

	@Autowired
	private TodayAccountReceiveOrderSituationRepo todayAccountReceiveOrderSituationRepo;

	@Autowired
	private TotalCashDepositRepo totalCashDepositRepo;

	@Autowired
	private TotalStatisticalRepo totalStatisticalRepo;

	@Autowired
	private MonthStatisticalRepo monthStatisticalRepo;

	@Autowired
	private TodayStatisticalRepo todayStatisticalRepo;
	
	@Cached(name = "totalTop10BountyRank", expire = 300)
	@Transactional(readOnly = true)
	public List<BountyRankVO> findTotalTop10BountyRank() {
		List<TotalAccountReceiveOrderSituation> receiveOrderSituations = totalAccountReceiveOrderSituationRepo
				.findTop10ByOrderByTotalBountyDesc();
		return BountyRankVO.convertFor(receiveOrderSituations);
	}

	@Cached(name = "todayTop10BountyRank", expire = 300)
	@Transactional(readOnly = true)
	public List<BountyRankVO> findTodayTop10BountyRank() {
		List<TodayAccountReceiveOrderSituation> todayReceiveOrderSituations = todayAccountReceiveOrderSituationRepo
				.findTop10ByOrderByTotalBountyDesc();
		return BountyRankVO.convertForToday(todayReceiveOrderSituations);
	}

	@Cached(name = "totalCashDeposit", expire = 300)
	@Transactional(readOnly = true)
	public Double findTotalCashDeposit() {
		TotalCashDeposit totalCashDeposit = totalCashDepositRepo.findTopBy();
		return NumberUtil.round(totalCashDeposit.getTotalCashDeposit(), 4).doubleValue();
	}

	@Cached(name = "totalStatistical", expire = 300)
	@Transactional(readOnly = true)
	public IndexStatisticalVO findTotalStatistical() {
		TotalStatistical statistical = totalStatisticalRepo.findTopBy();
		return IndexStatisticalVO.convertForTotal(statistical);
	}
	
	@Cached(name = "monthStatistical", expire = 300)
	@Transactional(readOnly = true)
	public IndexStatisticalVO findMonthStatistical() {
		MonthStatistical statistical = monthStatisticalRepo.findTopBy();
		return IndexStatisticalVO.convertForMonth(statistical);
	}
	
	@Cached(name = "todayStatistical", expire = 300)
	@Transactional(readOnly = true)
	public IndexStatisticalVO findTodayStatistical() {
		TodayStatistical statistical = todayStatisticalRepo.findTopBy();
		return IndexStatisticalVO.convertForToday(statistical);
	}

}
