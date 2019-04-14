package me.zohar.lottery.platform.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.platform.domain.PlatformOrder;

@Data
public class StartOrderParam {

	@NotBlank
	private String secretKey;

	/**
	 * 收款渠道
	 */
	@NotBlank
	private String gatheringChannelCode;

	/**
	 * 收款金额
	 */
	@NotNull
	@DecimalMin(value = "0", inclusive = false)
	private Double gatheringAmount;

	public PlatformOrder convertToPo(String platformId) {
		PlatformOrder po = new PlatformOrder();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setSubmitTime(new Date());
		po.setOrderState(Constant.平台订单状态_等待接单);
		po.setPlatformId(platformId);
		return po;
	}

}
