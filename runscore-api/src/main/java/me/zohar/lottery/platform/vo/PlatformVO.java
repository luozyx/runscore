package me.zohar.lottery.platform.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.platform.domain.Platform;

@Data
public class PlatformVO {

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

	public static List<PlatformVO> convertFor(List<Platform> platforms) {
		if (CollectionUtil.isEmpty(platforms)) {
			return new ArrayList<>();
		}
		List<PlatformVO> vos = new ArrayList<>();
		for (Platform platform : platforms) {
			vos.add(convertFor(platform));
		}
		return vos;
	}

	public static PlatformVO convertFor(Platform platform) {
		if (platform == null) {
			return null;
		}
		PlatformVO vo = new PlatformVO();
		BeanUtils.copyProperties(platform, vo);
		return vo;
	}

}
