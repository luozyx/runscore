package me.zohar.lottery.invitecode.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.useraccount.param.InviteCodeQueryCondParam;
import me.zohar.lottery.useraccount.service.InviteCodeService;

@Controller
@RequestMapping("/inviteCode")
public class InviteCodeController {

	@Autowired
	private InviteCodeService inviteCodeService;

	@GetMapping("/findGatheringCodeByPage")
	@ResponseBody
	public Result findInviteCodeByPage(InviteCodeQueryCondParam param) {
		return Result.success().setData(inviteCodeService.findInviteCodeByPage(param));
	}

}
