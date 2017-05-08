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
 * 用户角色
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "T_USER_ROLE")
public class UserRole extends BaseAbstractEntity {

	private static final long serialVersionUID = 5958425793207457337L;

	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;
	/**
	 * 用户
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	private User user;
	/**
	 * 角色
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ROLE_ID")
	private Role role;

	public UserRole(User user, Role role) {
		this.user = user;
		this.role = role;
	}
}
