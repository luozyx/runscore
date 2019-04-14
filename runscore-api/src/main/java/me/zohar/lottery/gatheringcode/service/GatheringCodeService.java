package me.zohar.lottery.gatheringcode.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.hutool.core.util.StrUtil;
import me.zohar.lottery.common.exception.BizError;
import me.zohar.lottery.common.exception.BizException;
import me.zohar.lottery.common.valid.ParamValid;
import me.zohar.lottery.common.vo.PageResult;
import me.zohar.lottery.gatheringcode.domain.GatheringCode;
import me.zohar.lottery.gatheringcode.param.GatheringCodeParam;
import me.zohar.lottery.gatheringcode.param.GatheringCodeQueryCondParam;
import me.zohar.lottery.gatheringcode.repo.GatheringCodeRepo;
import me.zohar.lottery.gatheringcode.vo.GatheringCodeVO;
import me.zohar.lottery.storage.domain.Storage;
import me.zohar.lottery.storage.repo.StorageRepo;

@Validated
@Service
public class GatheringCodeService {

	@Autowired
	private GatheringCodeRepo gatheringCodeRepo;

	@Autowired
	private StorageRepo storageRepo;

	@Transactional
	public void delMyGatheringCodeById(String id, String userAccountId) {
		GatheringCode gatheringCode = gatheringCodeRepo.getOne(id);
		if (!userAccountId.equals(gatheringCode.getUserAccountId())) {
			throw new BizException(BizError.无权删除数据);
		}
		delGatheringCodeById(id);
	}

	@Transactional
	public void delGatheringCodeById(String id) {
		GatheringCode gatheringCode = gatheringCodeRepo.getOne(id);
		disassociationGatheringCodeStorage(gatheringCode.getStorageId());
		gatheringCodeRepo.delete(gatheringCode);
	}

	@Transactional
	public GatheringCodeVO findMyGatheringCodeById(String id, String userAccountId) {
		GatheringCode gatheringCode = gatheringCodeRepo.getOne(id);
		if (!userAccountId.equals(gatheringCode.getUserAccountId())) {
			throw new BizException(BizError.无权查看数据);
		}
		return GatheringCodeVO.convertFor(gatheringCode);
	}

	@Transactional(readOnly = true)
	public PageResult<GatheringCodeVO> findMyGatheringCodeByPage(GatheringCodeQueryCondParam param) {
		if (StrUtil.isBlank(param.getUserAccountId())) {
			throw new BizException(BizError.无权查看数据);
		}
		return findGatheringCodeByPage(param);
	}

	@Transactional(readOnly = true)
	public PageResult<GatheringCodeVO> findGatheringCodeByPage(GatheringCodeQueryCondParam param) {
		Specification<GatheringCode> spec = new Specification<GatheringCode>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public Predicate toPredicate(Root<GatheringCode> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (StrUtil.isNotEmpty(param.getState())) {
					predicates.add(builder.equal(root.get("state"), param.getState()));
				}
				if (StrUtil.isNotEmpty(param.getGatheringChannelCode())) {
					predicates.add(builder.equal(root.get("gatheringChannelCode"), param.getGatheringChannelCode()));
				}
				if (StrUtil.isNotEmpty(param.getUserAccountId())) {
					predicates.add(builder.equal(root.get("userAccountId"), param.getUserAccountId()));
				}
				return predicates.size() > 0 ? builder.and(predicates.toArray(new Predicate[predicates.size()])) : null;
			}
		};
		Page<GatheringCode> result = gatheringCodeRepo.findAll(spec,
				PageRequest.of(param.getPageNum() - 1, param.getPageSize(), Sort.by(Sort.Order.desc("createTime"))));
		PageResult<GatheringCodeVO> pageResult = new PageResult<>(GatheringCodeVO.convertFor(result.getContent()),
				param.getPageNum(), param.getPageSize(), result.getTotalElements());
		return pageResult;
	}

	@Transactional
	public void associateGatheringCodeStorage(String storageId, String gatheringCodeId) {
		Storage storage = storageRepo.getOne(storageId);
		storage.setAssociateId(gatheringCodeId);
		storage.setAssociateBiz("gatheringCode");
		storageRepo.save(storage);
	}

	@Transactional
	public void disassociationGatheringCodeStorage(String storageId) {
		Storage oldStorage = storageRepo.getOne(storageId);
		oldStorage.setAssociateId(null);
		oldStorage.setAssociateBiz(null);
		storageRepo.save(oldStorage);
	}

	@ParamValid
	@Transactional
	public void addOrUpdateGatheringCode(GatheringCodeParam param, String userAccountId) {
		// 新增
		if (StrUtil.isBlank(param.getId())) {
			GatheringCode gatheringCode = param.convertToPo(userAccountId);
			gatheringCodeRepo.save(gatheringCode);
			associateGatheringCodeStorage(param.getStorageId(), gatheringCode.getId());
		}
		// 修改
		else {
			GatheringCode gatheringCode = gatheringCodeRepo.getOne(param.getId());
			// 取消关联旧的收款码图片
			if (!param.getStorageId().equals(gatheringCode.getStorageId())) {
				disassociationGatheringCodeStorage(gatheringCode.getStorageId());
			}
			BeanUtils.copyProperties(param, gatheringCode);
			gatheringCodeRepo.save(gatheringCode);
			associateGatheringCodeStorage(param.getStorageId(), gatheringCode.getId());
		}
	}

}
