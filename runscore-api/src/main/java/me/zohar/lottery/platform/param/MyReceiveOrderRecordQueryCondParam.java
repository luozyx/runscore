package me.zohar.lottery.platform.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class MyReceiveOrderRecordQueryCondParam extends PageParam {

	private String gatheringChannelCode;

	/**
	 * 接单时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date receiveOrderTime;

	/**
	 * 用户账号id
	 */
	@NotBlank
	private String userAccountId;

}
