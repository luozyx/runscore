package me.zohar.lottery.merchant.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.constants.Constant;
import me.zohar.lottery.merchant.domain.Appeal;
import me.zohar.lottery.merchant.domain.MerchantOrder;
import me.zohar.lottery.merchant.param.AppealQueryCondParam;
import me.zohar.lottery.merchant.param.UserStartAppealParam;
import me.zohar.lottery.merchant.repo.AppealRepo;
import me.zohar.lottery.merchant.vo.AppealVO;
import me.zohar.lottery.storage.domain.Storage;
import me.zohar.lottery.storage.repo.StorageRepo;

@Validated
@Service
public class AppealService {

	@Autowired
	private AppealRepo appealRepo;

	@Autowired
	private StorageRepo storageRepo;

	@Transactional(readOnly = true)
	public PageResult<AppealVO> findAppealByPage(AppealQueryCondParam param) {
		Specification<Appeal> spec = new Specification<Appeal>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<Appeal> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getOrderNo())) {
					predicates.add(builder.equal(root.join("merchantOrder", JoinType.INNER).get("orderNo"),
							param.getOrderNo()));
				}
				if (StrUtil.isNotBlank(param.getMerchantName())) {
					predicates.add(builder.equal(
							root.join("merchantOrder", JoinType.INNER).join("merchant", JoinType.INNER).get("name"),
							param.getMerchantName()));
				}
				if (StrUtil.isNotBlank(param.getGatheringChannelCode())) {
					predicates.add(builder.equal(root.join("merchantOrder", JoinType.INNER).get("gatheringChannelCode"),
							param.getGatheringChannelCode()));
				}
				if (StrUtil.isNotBlank(param.getReceiverUserName())) {
					predicates.add(builder.equal(root.join("merchantOrder", JoinType.INNER)
							.join("userAccount", JoinType.INNER).get("userName"), param.getReceiverUserName()));
				}
				if (StrUtil.isNotBlank(param.getAppealType())) {
					predicates.add(builder.equal(root.get("appealType"), param.getAppealType()));
				}
				if (StrUtil.isNotBlank(param.getAppealState())) {
					predicates.add(builder.equal(root.get("state"), param.getAppealState()));
				}
				if (param.getInitiationStartTime() != null) {
					predicates.add(builder.greaterThanOrEqualTo(root.get("initiationTime").as(Date.class),
							DateUtil.beginOfDay(param.getInitiationStartTime())));
				}
				if (param.getInitiationEndTime() != null) {
					predicates.add(builder.lessThanOrEqualTo(root.get("initiationTime").as(Date.class),
							DateUtil.endOfDay(param.getInitiationEndTime())));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<Appeal> result = appealRepo.findAll(spec, PageRequest.of(param.getPageNum() - 1, param.getPageSize(),
				Sort.by(Sort.Order.desc("initiationTime"))));
		PageResult<AppealVO> pageResult = new PageResult<>(AppealVO.convertFor(result.getContent()), param.getPageNum(),
				param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void userCancelAppeal(@NotBlank String userAccountId, @NotBlank String appealId) {
		Appeal appeal = appealRepo.findById(appealId).orElse(null);
		if (appeal == null) {
			throw new BizException(BizError.参数异常);
		}
		if (!(Constant.申诉类型_未支付申请取消订单.equals(appeal.getAppealType())
				|| Constant.申诉类型_实际支付金额小于收款金额.equals(appeal.getAppealType()))) {
			throw new BizException(BizError.无权撤销申诉);
		}
		if (!Constant.申诉状态_待处理.equals(appeal.getState())) {
			throw new BizException(BizError.无权撤销申诉);
		}
		MerchantOrder merchantOrder = appeal.getMerchantOrder();
		if (merchantOrder == null) {
			throw new BizException(BizError.商户订单不存在);
		}
		if (!merchantOrder.getReceivedAccountId().equals(userAccountId)) {
			throw new BizException(BizError.无权撤销申诉);
		}
		appeal.setState(Constant.申诉状态_用户撤销申诉);
		appealRepo.save(appeal);
	}

	@ParamValid
	@Transactional
	public void userStartAppeal(UserStartAppealParam param) {
		if (!Constant.申诉类型_未支付申请取消订单.equals(param.getAppealType())
				&& !Constant.申诉类型_实际支付金额小于收款金额.equals(param.getAppealType())) {
			throw new BizException(BizError.参数异常);
		}
		if (Constant.申诉类型_实际支付金额小于收款金额.equals(param.getAppealType())) {
			if (param.getActualPayAmount() == null || param.getActualPayAmount() <= 0) {
				throw new BizException(BizError.参数异常);
			}
			if (StrUtil.isBlank(param.getUserSreenshotIds())) {
				throw new BizException(BizError.参数异常);
			}
		}
		Appeal existAppeal = appealRepo.findByMerchantOrderIdAndState(param.getMerchantOrderId(), Constant.申诉状态_待处理);
		if (existAppeal != null) {
			throw new BizException(BizError.该订单存在未处理的申诉记录不能重复发起);
		}
		Appeal appeal = param.convertToPo();
		appealRepo.save(appeal);
		if (StrUtil.isNotBlank(param.getUserSreenshotIds())) {
			for (String sreenshotId : param.getUserSreenshotIds().split(",")) {
				Storage storage = storageRepo.getOne(sreenshotId);
				storage.setAssociateId(appeal.getId());
				storage.setAssociateBiz("appealSreenshot");
				storageRepo.save(storage);
			}
		}
	}

}
