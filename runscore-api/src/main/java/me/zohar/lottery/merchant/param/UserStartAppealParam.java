package me.zohar.lottery.merchant.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.merchant.domain.Appeal;

@Data
public class UserStartAppealParam {
	
	@NotBlank
	private String appealType;
	
	private Double actualPayAmount;
	
	private String userSreenshotIds;
	
	@NotBlank
	private String merchantOrderId;
	
	@NotBlank
	private String initiatorId;
	
	public Appeal convertToPo() {
		Appeal po = new Appeal();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setState(Constant.申诉状态_待处理);
		po.setInitiationTime(new Date());
		return po;
	}

}
