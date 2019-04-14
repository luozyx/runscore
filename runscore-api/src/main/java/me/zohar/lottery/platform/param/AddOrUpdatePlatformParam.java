package me.zohar.lottery.platform.param;

import java.util.Date;

import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;

import lombok.Data;
import me.zohar.lottery.common.utils.IdUtils;
import me.zohar.lottery.platform.domain.Platform;

@Data
public class AddOrUpdatePlatformParam {

	private String id;

	private String name;

	@NotBlank
	private String secretKey;

	public Platform convertToPo() {
		Platform po = new Platform();
		BeanUtils.copyProperties(this, po);
		po.setId(IdUtils.getId());
		po.setCreateTime(new Date());
		return po;
	}

}
