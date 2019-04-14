package me.zohar.lottery.platform.domain;

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
@Table(name = "v_receive_order_situation")
@DynamicInsert(true)
@DynamicUpdate(true)
public class ReceiveOrderSituation {

	@Id
	@Column(name = "received_account_id", length = 32)
	private String receivedAccountId;

	private String userName;

	private Double totalGatheringAmount;

	private Double totalBounty;

}
