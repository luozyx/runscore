package me.zohar.lottery;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

	/**
	 * 首页
	 * 
	 * @return
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}

	@GetMapping("/login")
	public String login() {
		return "login";
	}

	@GetMapping("/register")
	public String register() {
		return "register";
	}

	/**
	 * 我的主页
	 * 
	 * @return
	 */
	@GetMapping("/my-home-page")
	public String myHomePage() {
		return "my-home-page";
	}

	/**
	 * 个人信息
	 * 
	 * @return
	 */
	@GetMapping("/personal-info")
	public String personalInfo() {
		return "personal-info";
	}

	/**
	 * 个人帐变
	 * 
	 * @return
	 */
	@GetMapping("/personal-account-change")
	public String personalAccountChange() {
		return "personal-account-change";
	}

	/**
	 * 充值
	 * 
	 * @return
	 */
	@GetMapping("/recharge")
	public String recharge() {
		return "recharge";
	}

	/**
	 * 提现
	 * 
	 * @return
	 */
	@GetMapping("/withdraw")
	public String withdraw() {
		return "withdraw";
	}

	/**
	 * 收款码
	 * 
	 * @return
	 */
	@GetMapping("/gathering-code")
	public String gatheringCode() {
		return "gathering-code";
	}

	/**
	 * 接单
	 * 
	 * @return
	 */
	@GetMapping("/receive-order")
	public String receiveOrder() {
		return "receive-order";
	}

	/**
	 * 审核订单
	 * 
	 * @return
	 */
	@GetMapping("/audit-order")
	public String auditOrder() {
		return "audit-order";
	}
	
	/**
	 * 接单记录
	 * 
	 * @return
	 */
	@GetMapping("/receive-order-record")
	public String receiveOrderRecord() {
		return "receive-order-record";
	}

}
