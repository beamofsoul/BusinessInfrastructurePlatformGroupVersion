package com.beamofsoul.bip.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name="V_User_Role_Combine_Role")
public class UserRoleCombineRole implements Serializable {

	private static final long serialVersionUID = 6512226179888555302L;
	
	@Id
	@Column(name="id", insertable=false, updatable=false)
	private String id;
	
	@Column(name="user_id", insertable=false, updatable=false)
	private Long userId;
	
	@Column(name="username", insertable=false, updatable=false)
	private String username;
	
	@Column(name="nickname", insertable=false, updatable=false)
	private String nickname;
	
	@Column(name="role_id", insertable=false, updatable=false)
	private String roleId;
	
	@Column(name="role_name", insertable=false, updatable=false)
	private String roleName;
}