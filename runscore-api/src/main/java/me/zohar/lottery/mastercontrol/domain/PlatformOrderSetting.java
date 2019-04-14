package me.zohar.lottery.mastercontrol.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.zohar.lottery.common.utils.IdUtils;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "platform_order_setting")
@DynamicInsert(true)
@DynamicUpdate(true)
public class PlatformOrderSetting {

	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 平台订单有效时长
	 */
	private Integer orderEffectiveDuration;

	/**
	 * 接单返水率
	 */
	private Double returnWaterRate;

	/**
	 * 接单返水率启用标识
	 */
	private Boolean returnWaterRateEnabled;

	/**
	 * 最近修改时间
	 */
	private Date latelyUpdateTime;

	public void update(Integer orderEffectiveDuration, Double returnWaterRate,
			Boolean returnWaterRateEnabled) {
		this.setOrderEffectiveDuration(orderEffectiveDuration);
		this.setReturnWaterRate(returnWaterRate);
		this.setReturnWaterRateEnabled(returnWaterRateEnabled);
		this.setLatelyUpdateTime(new Date());
	}

	public static PlatformOrderSetting build() {
		PlatformOrderSetting platformOrderSetting = new PlatformOrderSetting();
		platformOrderSetting.setId(IdUtils.getId());
		return platformOrderSetting;
	}

}
