package me.zohar.lottery.merchant.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * 商户
 * @author zohar
 * @date 2019年4月19日
 *
 */
@Getter
@Setter
@Entity
@Table(name = "merchant")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Merchant {
	
	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 接入商家名称
	 */
	private String name;

	/**
	 * 密钥
	 */
	private String secretKey;

	private Date createTime;
	
	/**
	 * 乐观锁版本号
	 */
	@Version
	private Long version;

}
