package me.zohar.lottery.platform.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.useraccount.domain.UserAccount;

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
	 * 有效时间
	 */
	private Date usefulTime;

	/**
	 * 订单状态
	 */
	private String orderState;
	
	/**
	 * 备注
	 */
	private String note;

	@Column(name = "platform_id", length = 32)
	private String platformId;

	/**
	 * 接单人账号id
	 */
	@Column(name = "received_account_id", length = 32)
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
	 * 处理时间
	 */
	private Date dealTime;

	/**
	 * 确认时间
	 */
	private Date confirmTime;
	
	/**
	 * 奖励金
	 */
	private Double bounty;
	
	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "platform_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private Platform platform;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "received_account_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private UserAccount userAccount;
	
	public void updateBounty(Double bounty) {
		this.setBounty(bounty);
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
