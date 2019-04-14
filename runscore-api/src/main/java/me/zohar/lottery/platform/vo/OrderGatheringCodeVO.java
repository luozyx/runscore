package me.zohar.lottery.platform.vo;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.dictconfig.DictHolder;
import me.zohar.lottery.platform.domain.PlatformOrder;

@Data
public class OrderGatheringCodeVO {
	
	/**
	 * 主键id
	 */
	private String id;
	
	/**
	 * 订单号
	 */
	private String orderNo;
	
	/**
	 * 收款渠道
	 */
	private String gatheringChannelCode;
	
	private String gatheringChannelName;

	/**
	 * 收款金额
	 */
	private Double gatheringAmount;
	
	/**
	 * 订单状态
	 */
	private String orderState;
	
	private String orderStateName;
	
	private String gatheringCodeUrl;
	
	public static OrderGatheringCodeVO convertFor(PlatformOrder platformOrder) {
		if (platformOrder == null) {
			return null;
		}
		OrderGatheringCodeVO vo = new OrderGatheringCodeVO();
		BeanUtils.copyProperties(platformOrder, vo);
		vo.setGatheringChannelName(DictHolder.getDictItemName("gatheringChannel", vo.getGatheringChannelCode()));
		vo.setOrderStateName(DictHolder.getDictItemName("platformOrderState", vo.getOrderState()));
		return vo;
	}

}
