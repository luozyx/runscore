package me.zohar.lottery.useraccount.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.useraccount.domain.InviteCode;

@Data
public class InviteCodeVO {

	private String id;

	/**
	 * 邀请码
	 */
	private String code;

	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date createTime;

	/**
	 * 有效期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	private Date periodOfValidity;

	/**
	 * 有效标识
	 */
	private Boolean validFlag;

	private String inviterId;

	private String inviterUserName;

	private String inviteeId;

	private String inviteeUserName;

	public static List<InviteCodeVO> convertFor(List<InviteCode> inviteCodes) {
		if (CollectionUtil.isEmpty(inviteCodes)) {
			return new ArrayList<>();
		}
		List<InviteCodeVO> vos = new ArrayList<>();
		for (InviteCode inviteCode : inviteCodes) {
			vos.add(convertFor(inviteCode));
		}
		return vos;
	}

	public static InviteCodeVO convertFor(InviteCode inviteCode) {
		if (inviteCode == null) {
			return null;
		}
		InviteCodeVO vo = new InviteCodeVO();
		BeanUtils.copyProperties(inviteCode, vo);
		vo.setValidFlag(vo.getPeriodOfValidity().getTime() > new Date().getTime());
		if (inviteCode.getInviter() != null) {
			vo.setInviterUserName(inviteCode.getInviter().getUserName());
		}
		if (inviteCode.getInvitee() != null) {
			vo.setInviteeUserName(inviteCode.getInvitee().getUserName());
		}
		return vo;
	}

}
