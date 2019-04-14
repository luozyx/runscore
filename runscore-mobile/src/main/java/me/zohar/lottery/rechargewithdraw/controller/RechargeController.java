package me.zohar.lottery.rechargewithdraw.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.config.security.UserAccountDetails;
import me.zohar.lottery.rechargewithdraw.param.AbcyzfCallbackParam;
import me.zohar.lottery.rechargewithdraw.param.MuspayCallbackParam;
import me.zohar.lottery.rechargewithdraw.param.RechargeOrderParam;
import me.zohar.lottery.rechargewithdraw.service.RechargeService;

/**
 * 
 * @author zohar
 * @date 2019年1月21日
 *
 */
@Controller
@RequestMapping("/recharge")
public class RechargeController {

	@Autowired
	private RechargeService rechargeService;
	
	@RequestMapping("/callback/abcyzfCallback")
	@ResponseBody
	public String abcyzfCallback(AbcyzfCallbackParam param) throws IOException {
		rechargeService.checkOrderWithAbcyzf(param);
		return Result.success().getMsg();
	}
	
	@PostMapping("/generateRechargeOrderWithAbcyzf")
	@ResponseBody
	public Result generateRechargeOrderWithAbcyzf(RechargeOrderParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(rechargeService.generateRechargeOrderWithAbcyzf(param));
	}

	@RequestMapping("/muspayCallback")
	@ResponseBody
	public String muspayCallback(@RequestBody MuspayCallbackParam param) throws IOException {
		rechargeService.checkOrderWithMuspay(param);
		return Result.success().getMsg();
	}
	
	@PostMapping("/generateRechargeOrder")
	@ResponseBody
	public Result generateRechargeOrder(RechargeOrderParam param) {
		UserAccountDetails user = (UserAccountDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		param.setUserAccountId(user.getUserAccountId());
		return Result.success().setData(rechargeService.generateRechargeOrder(param));
	}

}
