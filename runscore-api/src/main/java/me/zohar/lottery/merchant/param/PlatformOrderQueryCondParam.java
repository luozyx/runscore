package me.zohar.lottery.merchant.param;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlatformOrderQueryCondParam extends PageParam {

	private String orderNo;
	
	private String platformName;

	private String gatheringChannelCode;

	/**
	 * 订单状态
	 */
	private String orderState;
	
	private String receiverUserName;

	/**
	 * 提交开始时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date submitStartTime;

	/**
	 * 提交结束时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date submitEndTime;

}
