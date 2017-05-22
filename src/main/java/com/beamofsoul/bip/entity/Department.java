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

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName Department
 * @Description 部门表实体类
 * @author MingshuJian
 * @Date 2017年5月19日 上午09:53:00
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)

@Entity
@Table(name = "T_DEPARTMENT")
public class Department extends BaseAbstractRelationalEntity {

	private static final long serialVersionUID = 7325041962975446069L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	protected Long id;
	/**
	 * 部门编码
	 */
	@Column
	private String code;
	/**
	 * 部门名称
	 */
	@Column
	private String name;
	/**
	 * 部门描述
	 */
	@Column
	private String descirption;
	/**
	 * 排序
	 */
	@Column
	private Integer sort;
	/**
	 * 上级部门
	 */
	@ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REFRESH,CascadeType.REMOVE,CascadeType.DETACH})
    @JoinColumn(name = "PARENT_ID", nullable = true)
	private Department parent;
	/**
	 * 所属结构
	 */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ORGANIZATION_ID", nullable = true)
	private Organization organization;
	/**
	 * 是否可用
	 */
	@Column
	private Boolean available;
	
	@Override
	public String toString() {
		return "Department [id=" + id + ", code=" + code + ", name=" + name + ", descirption=" + descirption + ", sort="
				+ sort + ", parent=" + parent + ", available=" + available + "]";
	}
}
