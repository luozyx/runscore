package me.zohar.lottery.platform.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;
import me.zohar.lottery.constants.Constant;

@Getter
@Setter
@Entity
@Table(name = "platform_order")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PlatformOrder {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 订单号
	 */
	private String orderNo;

	/**
	 * 收款渠道
	 */
	private String gatheringChannelCode;

	/**
	 * 收款金额
	 */
	private Double gatheringAmount;

	/**
	 * 提交时间
	 */
	private Date submitTime;

	/**
	 * 订单状态
	 */
	private String orderState;

	@Column(name = "platform_id", length = 32)
	private String platformId;

	/**
	 * 接单人账号id
	 */
	private String receivedAccountId;

	/**
	 * 接单时间
	 */
	private Date receivedTime;

	/**
	 * 平台确认时间
	 */
	private Date platformConfirmTime;

	/**
	 * 确认时间
	 */
	private Date confirmTime;
	
	/**
	 * 奖励金
	 */
	private Double bounty;
	
	/**
	 * 奖励金结算时间
	 */
	private Date bountySettlementTime;

	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;
	
	public void updateBounty(Double bounty) {
		this.setBounty(bounty);
		this.setBountySettlementTime(new Date());
	}
	
	public void platformConfirmToPaid() {
		this.setOrderState(Constant.平台订单状态_平台已确认支付);
		this.setPlatformConfirmTime(new Date());
	}

	public void confirmToPaid() {
		this.setOrderState(Constant.平台订单状态_已支付);
		this.setConfirmTime(new Date());

	}

	public void updateReceived(String receivedAccountId) {
		this.setReceivedAccountId(receivedAccountId);
		this.setOrderState(Constant.平台订单状态_已接单);
		this.setReceivedTime(new Date());
	}

}
