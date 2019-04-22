package me.zohar.lottery.useraccount.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper=false)
public class InviteCodeQueryCondParam extends PageParam {
	
	private String inviter;

}
