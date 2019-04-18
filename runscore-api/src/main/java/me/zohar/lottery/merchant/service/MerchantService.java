package me.zohar.lottery.merchant.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.NotBlank;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.merchant.domain.Merchant;
import me.zohar.lottery.merchant.param.AddOrUpdateMerchantParam;
import me.zohar.lottery.merchant.param.MerchantQueryCondParam;
import me.zohar.lottery.merchant.repo.MerchantRepo;
import me.zohar.lottery.merchant.vo.MerchantVO;

@Service
public class MerchantService {

	@Autowired
	private MerchantRepo merchantRepo;

	@Transactional(readOnly = true)
	public MerchantVO findPlatformById(@NotBlank String id) {
		return MerchantVO.convertFor(merchantRepo.getOne(id));
	}

	@Transactional
	public void delMerchantById(@NotBlank String id) {
		merchantRepo.deleteById(id);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateMerchant(AddOrUpdateMerchantParam param) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			Merchant platform = param.convertToPo();
			merchantRepo.save(platform);
		}
		// 修改
		else {
			Merchant merchant = merchantRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, merchant);
			merchantRepo.save(merchant);
		}
	}

	@Transactional(readOnly = true)
	public PageResult<MerchantVO> findMerchantByPage(MerchantQueryCondParam param) {
		Specification<Merchant> spec = new Specification<Merchant>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<Merchant> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getName())) {
					predicates.add(builder.equal(root.get("name"), param.getName()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<Merchant> result = merchantRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<MerchantVO> pageResult = new PageResult<>(MerchantVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
