package me.zohar.lottery.mastercontrol.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import me.zohar.lottery.mastercontrol.domain.PlatformOrderSetting;

public interface PlatformOrderSettingRepo
		extends JpaRepository<PlatformOrderSetting, String>, JpaSpecificationExecutor<PlatformOrderSetting> {

	PlatformOrderSetting findTopByOrderByLatelyUpdateTime();

}
