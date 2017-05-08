package com.beamofsoul.bip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 角色权限
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "T_ROLE_PERMISSION")
public class RolePermission extends BaseAbstractEntity {

	private static final long serialVersionUID = 4579842972075517496L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;
	/**
	 * 角色
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	private Role role;
	/**
	 * 权限
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PERMISSION_ID")
	private Permission permission;
	
	public RolePermission(Role role, Permission permission) {
		this.role = role;
		this.permission = permission;
	}

}
