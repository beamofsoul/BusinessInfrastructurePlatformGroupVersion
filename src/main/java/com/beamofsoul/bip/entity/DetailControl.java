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
 * 详细控制
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "T_DETAIL_CONTROL")
public class DetailControl extends BaseAbstractEntity {

	private static final long serialVersionUID = -6722830563824341150L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;
	/**
	 * 角色ID
	 */
	@Column(name = "role_id")
	private Long roleId;
	/**
	 * 实体类
	 */
	@Column(name = "entity_class")
	private String entityClass;
	/**
	 * 不可用列
	 */
	@Column(name = "unavailable_columns")
	private String unavailableColumns;
	/**
	 * 过滤规则
	 */
	@Column(name = "filter_rules")
	private String filterRules;
	/**
	 * 优先 priority的值越小,优先级越高
	 */
	@Column(name = "priority")
	private Integer priority;
	/**
	 * 启用
	 */
	@Column(name = "enabled")
	private Boolean enabled;

}
