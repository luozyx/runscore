package me.zohar.lottery.merchant.param;

import lombok.Data;
import lombok.EqualsAndHashCode;
import me.zohar.lottery.common.param.PageParam;

@Data
@EqualsAndHashCode(callSuper = false)
public class MerchantQueryCondParam extends PageParam {

	private String name;

}
