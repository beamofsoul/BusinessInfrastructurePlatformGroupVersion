package com.beamofsoul.bip.entity;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @ClassName Organization
 * @Description 系统组织机构信息表实体类
 * @author MingshuJian
 * @Date 2017年1月21日 上午9:21:27
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)

@Entity
@Table(name = "T_ORGANIZATION")
public class Organization extends BaseAbstractRelationalEntity {

	private static final long serialVersionUID = -3698064755378429720L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;
	/**
	 * 组织机构名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 组织机构描述
	 */
	@Column(name = "descirption")
	private String descirption;
	/**
	 * 排序
	 */
	@Column(name = "sort")
	private Integer sort;
	/**
	 * 上级组织机构
	 */
	@Column(name = "parentId")
	private Long parentId;
//	@ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "PARENT_ID")
//	private Organization parent;
	/**
	 * 是否可用
	 */
//	@Column
//	private Boolean available;
	@Column(name = "available")
	private Boolean available = Boolean.FALSE;

	@Override
	public String toString() {
		return "Organization [id=" + id + ", name=" + name + ", descirption=" + descirption + ", sort=" + sort
				+ ", parentId=" + parentId + ", available=" + available + "]";
	}
}
