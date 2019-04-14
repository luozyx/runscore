package me.zohar.lottery.platform.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class PlatformQueryCondParam extends PageParam {

	private String name;

}
