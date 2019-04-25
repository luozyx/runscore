package me.zohar.lottery.merchant.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.merchant.param.PlatformOrderQueryCondParam;
import me.zohar.lottery.merchant.service.MerchantOrderService;

@Controller
@RequestMapping("/platformOrder")
public class MerchantOrderController {

	@Autowired
	private MerchantOrderService platformOrderService;

	@GetMapping("/findPlatformOrderByPage")
	@ResponseBody
	public Result findPlatformOrderByPage(PlatformOrderQueryCondParam param) {
		return Result.success().setData(platformOrderService.findPlatformOrderByPage(param));
	}

	@GetMapping("/cancelOrder")
	@ResponseBody
	public Result cancelOrder(String id) {
		platformOrderService.cancelOrder(id);
		return Result.success();
	}

}
