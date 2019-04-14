package me.zohar.lottery.platform.service;

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
import me.zohar.lottery.platform.domain.Platform;
import me.zohar.lottery.platform.param.AddOrUpdatePlatformParam;
import me.zohar.lottery.platform.param.PlatformQueryCondParam;
import me.zohar.lottery.platform.repo.PlatformRepo;
import me.zohar.lottery.platform.vo.PlatformVO;

@Service
public class PlatformService {

	@Autowired
	private PlatformRepo platformRepo;

	@Transactional(readOnly = true)
	public PlatformVO findPlatformById(@NotBlank String id) {
		return PlatformVO.convertFor(platformRepo.getOne(id));
	}

	@Transactional
	public void delPlatformById(@NotBlank String id) {
		platformRepo.deleteById(id);
	}

	@ParamValid
	@Transactional
	public void addOrUpdatePlatform(AddOrUpdatePlatformParam param) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			Platform platform = param.convertToPo();
			platformRepo.save(platform);
		}
		// 修改
		else {
			Platform platform = platformRepo.getOne(param.getId());
			BeanUtils.copyProperties(param, platform);
			platformRepo.save(platform);
		}
	}

	@Transactional(readOnly = true)
	public PageResult<PlatformVO> findPlatformByPage(PlatformQueryCondParam param) {
		Specification<Platform> spec = new Specification<Platform>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<Platform> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotBlank(param.getName())) {
					predicates.add(builder.equal(root.get("name"), param.getName()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<Platform> result = platformRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<PlatformVO> pageResult = new PageResult<>(PlatformVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

}
