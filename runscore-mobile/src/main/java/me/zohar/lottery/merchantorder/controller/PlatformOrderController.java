package me.zohar.lottery.merchantorder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;
import me.zohar.lottery.merchant.param.MyReceiveOrderRecordQueryCondParam;
import me.zohar.lottery.merchant.service.MerchantOrderService;

@Controller
@RequestMapping("/platformOrder")
public class PlatformOrderController {

	@Autowired
	private MerchantOrderService platformOrderService;
	
	@GetMapping("/userConfirmToPaid")
	@ResponseBody
	public Result userConfirmToPaid(String orderId) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		platformOrderService.userConfirmToPaid(user.getUserAccountId(), orderId);
		return Result.success();
	}
	
	@GetMapping("/applyCancelOrder")
	@ResponseBody
	public Result applyCancelOrder(String orderId) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		platformOrderService.applyCancelOrder(user.getUserAccountId(), orderId);
		return Result.success();
	}
	
	@GetMapping("/findMyWaitConfirmOrder")
	@ResponseBody
	public Result findMyWaitConfirmOrder() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(platformOrderService.findMyWaitConfirmOrder(user.getUserAccountId()));
	}

	@GetMapping("/findMyWaitReceivingOrder")
	@ResponseBody
	public Result findMyWaitReceivingOrder() {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		return Result.success().setData(platformOrderService.findMyWaitReceivingOrder(user.getUserAccountId()));
	}

	@GetMapping("/receiveOrder")
	@ResponseBody
	public Result receiveOrder(String orderId) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		platformOrderService.receiveOrder(user.getUserAccountId(), orderId);
		return Result.success();
	}
	
	@GetMapping("/findMyReceiveOrderRecordByPage")
	@ResponseBody
	public Result findMyReceiveOrderRecordByPage(MyReceiveOrderRecordQueryCondParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(platformOrderService.findMyReceiveOrderRecordByPage(param));
	}

}
