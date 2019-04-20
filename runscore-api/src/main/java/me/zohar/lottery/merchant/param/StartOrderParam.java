package me.zohar.lottery.merchant.param;

import java.util.Date;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.merchant.domain.MerchantOrder;

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

	public MerchantOrder convertToPo(String merchantId, Integer orderEffectiveDuration) {
		MerchantOrder po = new MerchantOrder();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setOrderNo(po.getId());
		po.setSubmitTime(new Date());
		po.setOrderState(Constant.商户订单状态_等待接单);
		po.setMerchantId(merchantId);
		po.setUsefulTime(DateUtil.offset(po.getSubmitTime(), DateField.MINUTE, orderEffectiveDuration));
		return po;
	}

}
