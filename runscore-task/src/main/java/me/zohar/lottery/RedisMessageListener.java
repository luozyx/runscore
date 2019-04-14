package me.zohar.lottery;

import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.common.utils.ThreadPoolUtils;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.rechargewithdraw.service.RechargeService;

@Slf4j
@Component
public class RedisMessageListener {

	@Autowired
	private StringRedisTemplate redisTemplate;

	@Autowired
	private RechargeService rechargeService;

	@PostConstruct
	public void init() {
		listenRechargeSettlement();
	}


	public void listenRechargeSettlement() {
		new Thread(() -> {
			while (true) {
				try {
					String orderNo = redisTemplate.opsForList().rightPop(Constant.充值订单_已支付订单单号, 2L, TimeUnit.SECONDS);
					if (StrUtil.isBlank(orderNo)) {
						continue;
					}
					
					ThreadPoolUtils.getRechargeSettlementPool().execute(() -> {
						try {
							log.info("系统进行充值结算操作,订单单号为{}", orderNo);
							rechargeService.rechargeOrderSettlement(orderNo);
						} catch (Exception e) {
							log.error(MessageFormat.format("系统进行充值结算操作出现异常,订单单号为{0}", orderNo), e);
							throw new RuntimeException();
						}
					});
				} catch (Exception e) {
					log.error("充值结算消息队列异常", e);
				}
			}
		}).start();
	}

}
