package me.zohar.lottery.mastercontrol.param;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UpdatePlatformOrderSettingParam {

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Integer orderEffectiveDuration;

	@NotNull
	@DecimalMin(value = "0", inclusive = true)
	private Double returnWaterRate;

	@NotNull
	private Boolean returnWaterRateEnabled;

	@NotNull
	private Boolean unfixedGatheringCodeReceiveOrder;

}
