package me.zohar.lottery.merchant.domain;

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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "appeal")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Appeal {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 申诉类型
	 */
	private String appealType;

	/**
	 * 实际支付金额
	 */
	private Double actualPayAmount;
	
	private String userSreenshotIds;

	/**
	 * 客户订单id
	 */
	@Column(name = "merchant_order_id", length = 32)
	private String merchantOrderId;

	private String state;

	/**
	 * 发起时间
	 */
	private Date initiationTime;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "merchant_order_id", updatable = false, insertable = false, foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	private MerchantOrder merchantOrder;

}
