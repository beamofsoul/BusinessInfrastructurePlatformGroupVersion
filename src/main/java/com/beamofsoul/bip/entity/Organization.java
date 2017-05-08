package com.beamofsoul.bip.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
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

@Entity
@Table(name = "T_ORGANIZATION")
public class Organization extends BaseAbstractEntity {

	private static final long serialVersionUID = -3698064755378429720L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	protected Long id;
	/**
	 * 组织机构名称
	 */
	@Column
	private String name;
	/**
	 * 组织机构描述
	 */
	@Column
	private String descirption;
	/**
	 * 排序
	 */
	@Column
	private Integer sort;
	/**
	 * 上级组织机构
	 */
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARENT_ID")
	private Organization parent;
	/**
	 * 是否可用
	 */
	@Column
	private Boolean available;

	@Override
	public String toString() {
		return "Organization [id=" + id + ", name=" + name + ", descirption=" + descirption + ", sort=" + sort
				+ ", parent=" + parent + ", available=" + available + "]";
	}
}
