package me.zohar.lottery.merchant.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.zengtengpeng.annotation.Lock;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.dictconfig.ConfigHolder;
import me.zohar.lottery.gatheringcode.domain.GatheringCode;
import me.zohar.lottery.gatheringcode.repo.GatheringCodeRepo;
import me.zohar.lottery.mastercontrol.domain.PlatformOrderSetting;
import me.zohar.lottery.mastercontrol.repo.PlatformOrderSettingRepo;
import me.zohar.lottery.merchant.domain.Merchant;
import me.zohar.lottery.merchant.domain.MerchantOrder;
import me.zohar.lottery.merchant.param.MyReceiveOrderRecordQueryCondParam;
import me.zohar.lottery.merchant.param.PlatformOrderQueryCondParam;
import me.zohar.lottery.merchant.param.StartOrderParam;
import me.zohar.lottery.merchant.repo.MerchantOrderRepo;
import me.zohar.lottery.merchant.repo.MerchantRepo;
import me.zohar.lottery.merchant.vo.MyReceiveOrderRecordVO;
import me.zohar.lottery.merchant.vo.MyWaitConfirmOrderVO;
import me.zohar.lottery.merchant.vo.MyWaitReceivingOrderVO;
import me.zohar.lottery.merchant.vo.OrderGatheringCodeVO;
import me.zohar.lottery.merchant.vo.PlatformOrderVO;
import me.zohar.lottery.useraccount.domain.AccountChangeLog;
import me.zohar.lottery.useraccount.domain.UserAccount;
import me.zohar.lottery.useraccount.repo.AccountChangeLogRepo;
import me.zohar.lottery.useraccount.repo.UserAccountRepo;

@Validated
@Slf4j
@Service
public class MerchantOrderService {

	@Autowired
	private MerchantOrderRepo merchantOrderRepo;

	@Autowired
	private MerchantRepo merchantRepo;

	@Autowired
	private UserAccountRepo userAccountRepo;

	@Autowired
	private GatheringCodeRepo gatheringCodeRepo;

	@Autowired
	private AccountChangeLogRepo accountChangeLogRepo;

	@Autowired
	private PlatformOrderSettingRepo platformOrderSettingRepo;

	@Transactional
	public void merchantConfirmToPaid(@NotBlank String secretKey, @NotBlank String orderId) {
		Merchant merchant = merchantRepo.findBySecretKey(secretKey);
		if (merchant == null) {
			throw new BizException(BizError.商户未接入);
		}
		MerchantOrder order = merchantOrderRepo.findById(orderId).orElse(null);
		if (order == null) {
			log.error("商家订单不存在;secretKey:{},orderId:{}", secretKey, orderId);
			throw new BizException(BizError.平台订单不存在);
		}
		if (!order.getMerchantId().equals(merchant.getId())) {
			log.error("无权更新商家订单状态为平台已确认支付;secretKey:{},orderId:{}", secretKey, orderId);
			throw new BizException(BizError.无权更新平台订单状态为平台已确认支付);
		}
		if (!Constant.商户订单状态_已接单.equals(order.getOrderState())) {
			throw new BizException(BizError.订单状态为已接单才能转为平台已确认支付);
		}
		order.merchantConfirmToPaid();
		merchantOrderRepo.save(order);
	}

	@Transactional(readOnly = true)
	public OrderGatheringCodeVO getOrderGatheringCode(@NotBlank String secretKey, @NotBlank String orderId) {
		Merchant platform = merchantRepo.findBySecretKey(secretKey);
		if (platform == null) {
			throw new BizException(BizError.商户未接入);
		}
		MerchantOrder order = merchantOrderRepo.findById(orderId).orElse(null);
		if (order == null) {
			log.error("平台订单不存在;secretKey:{},orderId:{}", secretKey, orderId);
			throw new BizException(BizError.平台订单不存在);
		}
		if (!order.getMerchantId().equals(platform.getId())) {
			log.error("无权获取商家订单收款码信息;secretKey:{},orderId:{}", secretKey, orderId);
			throw new BizException(BizError.无权获取平台订单收款码信息);
		}

		OrderGatheringCodeVO vo = OrderGatheringCodeVO.convertFor(order);
		if (Constant.商户订单状态_已接单.equals(vo.getOrderState())) {
			GatheringCode gatheringCode = gatheringCodeRepo
					.findByUserAccountIdAndGatheringChannelCodeAndGatheringAmount(order.getReceivedAccountId(),
							order.getGatheringChannelCode(), order.getGatheringAmount());
			if (gatheringCode != null) {
				String gatheringCodeUrl = ConfigHolder.getConfigValue("storageUrl") + gatheringCode.getStorageId();
				vo.setGatheringCodeUrl(gatheringCodeUrl);
			}
		}
		return vo;
	}

	@Transactional
	public void userConfirmToPaid(@NotBlank String userAccountId, @NotBlank String orderId) {
		MerchantOrder platformOrder = merchantOrderRepo.findByIdAndReceivedAccountId(orderId, userAccountId);
		if (platformOrder == null) {
			throw new BizException(BizError.平台订单不存在);
		}
		confirmToPaidInner(orderId, null);
	}

	/**
	 * 客服确认已支付
	 * 
	 * @param orderId
	 */
	@Transactional
	public void customerServiceConfirmToPaid(@NotBlank String orderId, String note) {
		confirmToPaidInner(orderId, note);
	}

	@Transactional
	public void confirmToPaidInner(@NotBlank String orderId, String note) {
		MerchantOrder platformOrder = merchantOrderRepo.findById(orderId).orElse(null);
		if (platformOrder == null) {
			throw new BizException(BizError.平台订单不存在);
		}
		if (!(Constant.商户订单状态_已接单.equals(platformOrder.getOrderState())
				|| Constant.商户订单状态_商户已确认支付.equals(platformOrder.getOrderState())
				|| Constant.商户订单状态_申请取消订单.equals(platformOrder.getOrderState()))) {
			throw new BizException(BizError.订单状态为已接单或平台已确认支付或申请取消订单才能转为确认已支付);
		}
		platformOrder.confirmToPaid(note);
		merchantOrderRepo.save(platformOrder);
		receiveOrderBountySettlement(platformOrder);
	}

	/**
	 * 接单奖励金结算
	 */
	@Transactional
	public void receiveOrderBountySettlement(MerchantOrder platformOrder) {
		PlatformOrderSetting setting = platformOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting == null || !setting.getReturnWaterRateEnabled() || setting.getReturnWaterRate() == null) {
			platformOrder.updateBounty(0d);
			merchantOrderRepo.save(platformOrder);
			return;
		}

		UserAccount userAccount = platformOrder.getUserAccount();
		double returnWater = NumberUtil
				.round(platformOrder.getGatheringAmount() * setting.getReturnWaterRate() * 0.01, 4).doubleValue();
		platformOrder.updateBounty(returnWater);
		merchantOrderRepo.save(platformOrder);
		userAccount.setCashDeposit(NumberUtil.round(userAccount.getCashDeposit() + returnWater, 4).doubleValue());
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(
				AccountChangeLog.buildWithReceiveOrderBounty(userAccount, returnWater, setting.getReturnWaterRate()));
	}

	@Transactional(readOnly = true)
	public List<MyWaitConfirmOrderVO> findMyWaitConfirmOrder(@NotBlank String userAccountId) {
		return MyWaitConfirmOrderVO
				.convertFor(merchantOrderRepo.findByOrderStateInAndReceivedAccountIdOrderBySubmitTimeDesc(
						Arrays.asList(Constant.商户订单状态_已接单, Constant.商户订单状态_商户已确认支付), userAccountId));
	}

	@Transactional(readOnly = true)
	public List<MyWaitReceivingOrderVO> findMyWaitReceivingOrder(@NotBlank String userAccountId) {
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		List<GatheringCode> gatheringCodes = gatheringCodeRepo.findByUserAccountId(userAccountId);
		if (CollectionUtil.isEmpty(gatheringCodes)) {
			throw new BizException(BizError.未设置收款码无法接单);
		}
		Map<String, String> gatheringChannelCodeMap = new HashMap<>();
		for (GatheringCode gatheringCode : gatheringCodes) {
			gatheringChannelCodeMap.put(gatheringCode.getGatheringChannelCode(),
					gatheringCode.getGatheringChannelCode());
		}
		List<MerchantOrder> waitReceivingOrders = merchantOrderRepo
				.findTop10ByOrderStateAndGatheringAmountIsLessThanEqualAndGatheringChannelCodeInOrderBySubmitTimeDesc(
						Constant.商户订单状态_等待接单, userAccount.getCashDeposit(),
						new ArrayList<>(gatheringChannelCodeMap.keySet()));
		return MyWaitReceivingOrderVO.convertFor(waitReceivingOrders);
	}

	@ParamValid
	@Transactional
	public PlatformOrderVO startOrder(StartOrderParam param) {
		Merchant merchant = merchantRepo.findBySecretKey(param.getSecretKey());
		if (merchant == null) {
			throw new BizException(BizError.商户未接入);
		}

		Integer orderEffectiveDuration = Constant.平台订单默认有效时长;
		PlatformOrderSetting setting = platformOrderSettingRepo.findTopByOrderByLatelyUpdateTime();
		if (setting != null) {
			orderEffectiveDuration = setting.getOrderEffectiveDuration();
		}

		MerchantOrder platformOrder = param.convertToPo(merchant.getId(), orderEffectiveDuration);
		merchantOrderRepo.save(platformOrder);
		return PlatformOrderVO.convertFor(platformOrder);
	}

	/**
	 * 接单
	 * 
	 * @param param
	 * @return
	 */
	@Lock(keys = "'receiveOrder_' + #orderId")
	@Transactional
	public void receiveOrder(@NotBlank String userAccountId, @NotBlank String orderId) {
		MerchantOrder platformOrder = merchantOrderRepo.getOne(orderId);
		if (platformOrder == null) {
			throw new BizException(BizError.平台订单不存在);
		}
		if (!Constant.商户订单状态_等待接单.equals(platformOrder.getOrderState())) {
			throw new BizException(BizError.订单已被接或已取消);
		}
		UserAccount userAccount = userAccountRepo.getOne(userAccountId);
		Double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() - platformOrder.getGatheringAmount(), 4)
				.doubleValue();
		if (cashDeposit < 0) {
			throw new BizException(BizError.保证金不足无法接单);
		}

		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		platformOrder.updateReceived(userAccount.getId());
		merchantOrderRepo.save(platformOrder);
		accountChangeLogRepo.save(AccountChangeLog.buildWithReceiveOrderDeduction(userAccount, platformOrder));
	}

	@ParamValid
	@Transactional(readOnly = true)
	public PageResult<MyReceiveOrderRecordVO> findMyReceiveOrderRecordByPage(MyReceiveOrderRecordQueryCondParam param) {
		Specification<MerchantOrder> spec = new Specification<MerchantOrder>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MerchantOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (param.getReceiveOrderTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("receivedTime").as(Date.class),
							DateUtil.beginOfDay(param.getReceiveOrderTime())));
				}
				if (param.getReceiveOrderTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("receivedTime").as(Date.class),
							DateUtil.endOfDay(param.getReceiveOrderTime())));
				}
				if (StrUtil.isNotEmpty(param.getGatheringChannelCode())) {
					predicates.add(builder.equal(root.get("gatheringChannelCode"), param.getGatheringChannelCode()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<MerchantOrder> result = merchantOrderRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("receivedTime"))));
		PageResult<MyReceiveOrderRecordVO> pageResult = new PageResult<>(
				MyReceiveOrderRecordVO.convertFor(result.getContent()), param.getPageNum(), param.getPageSize(),
				result.getTotalElements());
		return pageResult;
	}

	@Transactional(readOnly = true)
	public PageResult<PlatformOrderVO> findPlatformOrderByPage(PlatformOrderQueryCondParam param) {
		Specification<MerchantOrder> spec = new Specification<MerchantOrder>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<MerchantOrder> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getOrderNo())) {
					predicates.add(builder.equal(root.get("orderNo"), param.getOrderNo()));
				}

				if (StrUtil.isNotBlank(param.getPlatformName())) {
					predicates.add(
							builder.equal(root.join("platform", JoinType.INNER).get("name"), param.getPlatformName()));
				}
				if (StrUtil.isNotBlank(param.getGatheringChannelCode())) {
					predicates.add(builder.equal(root.get("gatheringChannelCode"), param.getGatheringChannelCode()));
				}
				if (StrUtil.isNotBlank(param.getOrderState())) {
					predicates.add(builder.equal(root.get("orderState"), param.getOrderState()));
				}
				if (StrUtil.isNotBlank(param.getReceiverUserName())) {
					predicates.add(builder.equal(root.join("userAccount", JoinType.INNER).get("userName"),
							param.getReceiverUserName()));
				}
				if (param.getSubmitStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.beginOfDay(param.getSubmitStartTime())));
				}
				if (param.getSubmitEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("submitTime").as(Date.class),
							DateUtil.endOfDay(param.getSubmitEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<MerchantOrder> result = merchantOrderRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("submitTime"))));
		PageResult<PlatformOrderVO> pageResult = new PageResult<>(PlatformOrderVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	/**
	 * 取消订单
	 * 
	 * @param id
	 */
	@Transactional
	public void cancelOrder(@NotBlank String id) {
		MerchantOrder platformOrder = merchantOrderRepo.getOne(id);
		if (!Constant.商户订单状态_等待接单.equals(platformOrder.getOrderState())) {
			throw new BizException(BizError.只有等待接单状态的平台订单才能取消);
		}
		platformOrder.setOrderState(Constant.商户订单状态_人工取消);
		platformOrder.setDealTime(new Date());
		merchantOrderRepo.save(platformOrder);
	}

	@Transactional
	public void unpaidCancelOrder(@NotBlank String id, String note) {
		MerchantOrder merchantOrder = merchantOrderRepo.getOne(id);
		if (!Constant.商户订单状态_申请取消订单.equals(merchantOrder.getOrderState())) {
			throw new BizException(BizError.只有等待接单状态的平台订单才能取消);
		}
		merchantOrder.unpaidCancelOrder(note);
		merchantOrderRepo.save(merchantOrder);
		UserAccount userAccount = merchantOrder.getUserAccount();
		Double cashDeposit = NumberUtil.round(userAccount.getCashDeposit() + merchantOrder.getGatheringAmount(), 4)
				.doubleValue();
		userAccount.setCashDeposit(cashDeposit);
		userAccountRepo.save(userAccount);
		accountChangeLogRepo.save(AccountChangeLog.buildWithRefundCashDeposit(userAccount, merchantOrder));
	}

	@Transactional
	public void applyCancelOrder(@NotBlank String userAccountId, @NotBlank String orderId) {
		MerchantOrder platformOrder = merchantOrderRepo.findById(orderId).orElse(null);
		if (platformOrder == null) {
			throw new BizException(BizError.平台订单不存在);
		}
		if (!(Constant.商户订单状态_已接单.equals(platformOrder.getOrderState())
				|| Constant.商户订单状态_商户已确认支付.equals(platformOrder.getOrderState()))) {
			throw new BizException(BizError.订单状态为已接单或平台已确认支付才能申请取消订单);
		}
		if (!platformOrder.getReceivedAccountId().equals(userAccountId)) {
			throw new BizException(BizError.无权确认订单);
		}
		platformOrder.setOrderState(Constant.商户订单状态_申请取消订单);
		merchantOrderRepo.save(platformOrder);
	}

}
