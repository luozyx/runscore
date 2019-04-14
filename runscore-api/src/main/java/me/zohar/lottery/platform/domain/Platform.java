package me.zohar.lottery.platform.domain;

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

@Getter
@Setter
@Entity
@Table(name = "platform")
@DynamicInsert(true)
@DynamicUpdate(true)
public class Platform {
	
	/**
	 * 主键id
	 */
	@Id
	@Column(name = "id", length = 32)
	private String id;

	/**
	 * 接入平台名称
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
