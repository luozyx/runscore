package me.zohar.lottery.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.merchant.param.AppealQueryCondParam;
import me.zohar.lottery.merchant.service.AppealService;

@Controller
@RequestMapping("/appeal")
public class AppealController {

	@Autowired
	private AppealService appealService;

	@GetMapping("/dontProcess")
	@ResponseBody
	public Result dontProcess(String appealId) {
		appealService.dontProcess(appealId);
		return Result.success();
	}
	
	@GetMapping("/cancelOrder")
	@ResponseBody
	public Result cancelOrder(String appealId) {
		appealService.cancelOrder(appealId);
		return Result.success();
	}
	
	@GetMapping("/alterToActualPayAmount")
	@ResponseBody
	public Result alterToActualPayAmount(String appealId) {
		appealService.alterToActualPayAmount(appealId);
		return Result.success();
	}

	@GetMapping("/findAppealByPage")
	@ResponseBody
	public Result findAppealByPage(AppealQueryCondParam param) {
		return Result.success().setData(appealService.findAppealByPage(param));
	}

}
