package me.zohar.lottery.statisticalanalysis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.statisticalanalysis.service.StatisticalAnalysisService;

@Controller
@RequestMapping("/statisticalAnalysis")
public class StatisticalAnalysisController {

	@Autowired
	private StatisticalAnalysisService statisticalAnalysisService;

	@GetMapping("/findTodayTop10BountyRank")
	@ResponseBody
	public Result findTodayTop10BountyRank() {
		return Result.success().setData(statisticalAnalysisService.findTodayTop10BountyRank());
	}

	@GetMapping("/findTotalTop10BountyRank")
	@ResponseBody
	public Result findTotalTop10BountyRank() {
		return Result.success().setData(statisticalAnalysisService.findTotalTop10BountyRank());
	}

}
