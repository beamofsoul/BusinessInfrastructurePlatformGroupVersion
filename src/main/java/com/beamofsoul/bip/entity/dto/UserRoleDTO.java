package com.beamofsoul.bip.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(force=true)
@AllArgsConstructor
public class UserRoleDTO {

	private Long userRoleId;
	private long userId;
	private String username;
	private String nickname;
	private Long roleId;
	private String roleName;
}
