package me.zohar.lottery.gatheringcode.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "gathering_code")
@DynamicInsert(true)
@DynamicUpdate(true)
public class GatheringCode {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 收款渠道
	 */
	private String gatheringChannelCode;

	/**
	 * 状态,启用:1;禁用:0
	 */
	private String state;

	/**
	 * 收款金额
	 */
	private Double gatheringAmount;

	/**
	 * 收款人
	 */
	private String payee;

	/**
	 * 创建时间
	 */
	private Date createTime;
	
	@Column(name = "storage_id", length = 32)
	private String storageId;
	
	/**
	 * 投注人用户账号id
	 */
	@Column(name = "user_account_id", length = 32)
	private String userAccountId;

}
