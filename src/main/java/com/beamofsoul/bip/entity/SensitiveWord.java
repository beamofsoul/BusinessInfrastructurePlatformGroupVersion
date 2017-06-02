package com.beamofsoul.bip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @ClassName SensitiveWord
 * @Description 系统敏感词表实体类
 * @author MingshuJian
 * @Date 2017年5月31日 上午10:27:23
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data

@Entity
@Table(name = "T_SENSITIVE_WORD")
public class SensitiveWord extends BaseAbstractEntity {

	private static final long serialVersionUID = 5424728581820646841L;
	
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;
	/**
	 * 敏感词
	 */
	@Column(name = "word")
	private String word; //多个词用#间隔
	/**
	 * 替换词
	 */
	@Column(name = "replacement")
	private String replacement;
	/**
	 * 是否正则表达式
	 */
	@Column(name = "regular")
	private Boolean regular;
	/**
	 * 可用状态
	 */
	@Column(name = "available")
	private Boolean available;
}
