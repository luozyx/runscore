package me.zohar.lottery.platform.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import me.zohar.lottery.common.vo.Result;
import me.zohar.lottery.platform.param.AddOrUpdatePlatformParam;
import me.zohar.lottery.platform.param.PlatformQueryCondParam;
import me.zohar.lottery.platform.service.PlatformService;

@Controller
@RequestMapping("/platform")
public class PlatformController {

	@Autowired
	private PlatformService platformService;
	
	@PostMapping("/addOrUpdatePlatform")
	@ResponseBody
	public Result addOrUpdatePlatform(AddOrUpdatePlatformParam param) {
		platformService.addOrUpdatePlatform(param);
		return Result.success();
	}

	@GetMapping("/findPlatformById")
	@ResponseBody
	public Result findPlatformById(String id) {
		return Result.success().setData(platformService.findPlatformById(id));
	}

	@GetMapping("/delPlatformById")
	@ResponseBody
	public Result delPlatformById(String id) {
		platformService.delPlatformById(id);
		return Result.success();
	}

	@GetMapping("/findPlatformByPage")
	@ResponseBody
	public Result findPlatformOrderByPage(PlatformQueryCondParam param) {
		return Result.success().setData(platformService.findPlatformByPage(param));
	}

}
