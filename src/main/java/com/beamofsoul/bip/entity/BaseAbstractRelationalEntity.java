package com.beamofsoul.bip.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@MappedSuperclass
public class BaseAbstractRelationalEntity extends BaseAbstractEntity {

	private static final long serialVersionUID = 3939416931646679237L;

	/**
	 * 子节点的数量
	 */
	@Transient
	private Long countOfChildren;
	/**
	 * 是否为父节点
	 */
	@Transient
	private Boolean isParent;
	
	public BaseAbstractRelationalEntity() {
		super();
	}
	
	public BaseAbstractRelationalEntity(Long countOfChildren) {
		super();
		setter(countOfChildren);
	}
	
	public void setCountOfChildren(Long countOfChildren) {
		setter(countOfChildren);
	}
	
	private void setter(Long countOfChildren) {
		this.countOfChildren = countOfChildren;
		this.isParent = countOfChildren > 0;
	}
}
