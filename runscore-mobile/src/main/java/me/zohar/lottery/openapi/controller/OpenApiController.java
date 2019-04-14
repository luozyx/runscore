package me.zohar.lottery.openapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.platform.param.StartOrderParam;
import me.zohar.lottery.platform.service.PlatformOrderService;
import me.zohar.lottery.platform.vo.OrderGatheringCodeVO;
import me.zohar.lottery.platform.vo.PlatformOrderVO;

@Controller
@RequestMapping("/openApi")
public class OpenApiController {

	@Autowired
	private PlatformOrderService platformOrderService;

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
	
	@GetMapping("/platformConfirmToPaid")
	@ResponseBody
	public Result platformConfirmToPaid(String secretKey, String orderId) {
		platformOrderService.platformConfirmToPaid(secretKey, orderId);
		return Result.success();
	}

}
