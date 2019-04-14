package me.zohar.lottery.gatheringcode.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper=false)
public class GatheringCodeQueryCondParam extends PageParam {
	
	private String state;
	
	/**
	 * 收款渠道
	 */
	private String gatheringChannelCode;
	
	/**
	 * 用户账号id
	 */
	private String userAccountId;

}
