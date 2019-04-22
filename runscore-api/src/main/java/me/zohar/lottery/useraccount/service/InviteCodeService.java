package me.zohar.lottery.useraccount.service;

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

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.mastercontrol.domain.InviteRegisterSetting;
import me.zohar.lottery.mastercontrol.repo.InviteRegisterSettingRepo;
import me.zohar.lottery.useraccount.domain.InviteCode;
import me.zohar.lottery.useraccount.param.InviteCodeQueryCondParam;
import me.zohar.lottery.useraccount.repo.InviteCodeRepo;
import me.zohar.lottery.useraccount.vo.InviteCodeVO;

@Validated
@Service
public class InviteCodeService {

	@Autowired
	private InviteCodeRepo inviteCodeRepo;

	@Autowired
	private InviteRegisterSettingRepo inviteRegisterSettingRepo;

	@Transactional(readOnly = true)
	public PageResult<InviteCodeVO> findUserAccountDetailsInfoByPage(InviteCodeQueryCondParam param) {
		Specification<InviteCode> spec = new Specification<InviteCode>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<InviteCode> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getInviter())) {
					predicates.add(builder.equal(root.join("inviter", JoinType.INNER).get("userName"),
							param.getInviter()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<InviteCode> result = inviteCodeRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<InviteCodeVO> pageResult = new PageResult<>(InviteCodeVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	/**
	 * 生成邀请码
	 * 
	 * @param userAccountId
	 */
	@Transactional
	public void generateInviteCode(@NotBlank String userAccountId) {
		InviteRegisterSetting setting = inviteRegisterSettingRepo.findTopByOrderByEnabled();
		if (setting == null || !setting.getEnabled()) {
			throw new BizException(BizError.邀请注册功能已关闭);
		}
		String code = IdUtil.fastSimpleUUID().substring(0, 6);
		while (inviteCodeRepo.findTopByInviteeIdIsNullAndCodeAndPeriodOfValidityGreaterThanEqual(code,
				new Date()) != null) {
			code = IdUtil.fastSimpleUUID().substring(0, 6);
		}
		InviteCode newInviteCode = InviteCode.generateInviteCode(code, setting.getEffectiveDuration(), userAccountId);
		inviteCodeRepo.save(newInviteCode);
	}

	/**
	 * 确认邀请码并返回邀请人id
	 * 
	 * @param code
	 * @param inviteeId
	 * @return
	 */

	@Transactional
	public String confirmCodeAndGetInviterId(String code, String inviteeId) {
		InviteRegisterSetting setting = inviteRegisterSettingRepo.findTopByOrderByEnabled();
		if (setting == null || !setting.getEnabled()) {
			return null;
		}
		if (StrUtil.isBlank(code)) {
			throw new BizException(BizError.邀请码不存在或已失效);
		}
		InviteCode inviteCode = inviteCodeRepo.findTopByInviteeIdIsNullAndCodeAndPeriodOfValidityGreaterThanEqual(code,
				new Date());
		if (inviteCode == null) {
			throw new BizException(BizError.邀请码不存在或已失效);
		}
		inviteCode.setInviteeId(inviteeId);
		inviteCodeRepo.save(inviteCode);
		return inviteCode.getInviterId();
	}

}
