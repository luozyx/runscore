package me.zohar.lottery.openapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.merchant.param.StartOrderParam;
import me.zohar.lottery.merchant.service.MerchantOrderService;
import me.zohar.lottery.merchant.vo.OrderGatheringCodeVO;
import me.zohar.lottery.merchant.vo.PlatformOrderVO;

@Controller
@RequestMapping("/openApi")
public class OpenApiController {

	@Autowired
	private MerchantOrderService platformOrderService;

	@PostMapping("/startOrder")
	@ResponseBody
	public Result startOrder(StartOrderParam param) {
		PlatformOrderVO vo = platformOrderService.startOrder(param);
		return Result.success().setData(vo);
	}

	@GetMapping("/getOrderGatheringCode")
	@ResponseBody
	public Result getOrderGatheringCode(String secretKey, String orderId) {
		OrderGatheringCodeVO vo = platformOrderService.getOrderGatheringCode(secretKey, orderId);
		return Result.success().setData(vo);
	}
	
	@GetMapping("/merchantConfirmToPaid")
	@ResponseBody
	public Result merchantConfirmToPaid(String secretKey, String orderId) {
		platformOrderService.merchantConfirmToPaid(secretKey, orderId);
		return Result.success();
	}

}
