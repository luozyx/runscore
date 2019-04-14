package me.zohar.lottery.platform.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import cn.hutool.core.collection.CollectionUtil;
import lombok.Data;
import me.zohar.lottery.platform.domain.ReceiveOrderSituation;
import me.zohar.lottery.platform.domain.TodayReceiveOrderSituation;

@Data
public class BountyRankVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String receivedAccountId;

	private String userName;

	private Double totalBounty;

	public static List<BountyRankVO> convertForToday(List<TodayReceiveOrderSituation> situations) {
		if (CollectionUtil.isEmpty(situations)) {
			return new ArrayList<>();
		}
		List<BountyRankVO> vos = new ArrayList<>();
		for (TodayReceiveOrderSituation situation : situations) {
			vos.add(convertForToday(situation));
		}
		return vos;
	}

	public static BountyRankVO convertForToday(TodayReceiveOrderSituation situation) {
		if (situation == null) {
			return null;
		}
		BountyRankVO vo = new BountyRankVO();
		BeanUtils.copyProperties(situation, vo);
		vo.userNameDesensitization();
		return vo;
	}

	public static List<BountyRankVO> convertFor(List<ReceiveOrderSituation> situations) {
		if (CollectionUtil.isEmpty(situations)) {
			return new ArrayList<>();
		}
		List<BountyRankVO> vos = new ArrayList<>();
		for (ReceiveOrderSituation situation : situations) {
			vos.add(convertFor(situation));
		}
		return vos;
	}

	public static BountyRankVO convertFor(ReceiveOrderSituation situation) {
		if (situation == null) {
			return null;
		}
		BountyRankVO vo = new BountyRankVO();
		BeanUtils.copyProperties(situation, vo);
		vo.userNameDesensitization();
		return vo;
	}

	public void userNameDesensitization() {
		this.setUserName(this.userName.substring(0, 1) + "***"
				+ this.userName.substring(this.userName.length() - 3, this.userName.length()));
	}

}
