package me.zohar.lottery.merchant.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.merchant.domain.Merchant;

@Data
public class MerchantVO {

	private String id;

	/**
	 * 接入平台名称
	 */
	private String name;

	/**
	 * 密钥
	 */
	private String secretKey;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	public static List<MerchantVO> convertFor(List<Merchant> platforms) {
		if (CollectionUtil.isEmpty(platforms)) {
			return new ArrayList<>();
		}
		List<MerchantVO> vos = new ArrayList<>();
		for (Merchant platform : platforms) {
			vos.add(convertFor(platform));
		}
		return vos;
	}

	public static MerchantVO convertFor(Merchant platform) {
		if (platform == null) {
			return null;
		}
		MerchantVO vo = new MerchantVO();
		BeanUtils.copyProperties(platform, vo);
		return vo;
	}

}
