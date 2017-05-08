package com.beamofsoul.bip.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @ClassName User
 * @Description 系统用户账户信息表实体类
 * @author MingshuJian
 * @Date 2017年1月21日 上午9:21:27
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor

@JsonIgnoreProperties(value={"roles"})

@Entity
@Table(name = "T_USER")
public class User extends BaseAbstractEntity {

	private static final long serialVersionUID = -300036193618708950L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	protected Long id;

	@Column(unique = true)
	private String username;
	
	@Column
	private String password;
	
	@Column
	private String email;
	
	@Column
	private String phone;
	
	@Column(unique = true)
	private String nickname;
	
	@Column
	private String photo;
	
	@Transient
	private String photoString;
	
	@Column
	private int status; //1:正常, 0:冻结, -1:删除
	
	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinTable(name="T_USER_ROLE",
        joinColumns={@JoinColumn(name="user_id")},  
        inverseJoinColumns={@JoinColumn(name="role_id")})
	private Set<Role> roles = new HashSet<Role>(0);
	
	public User(Long id) {
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", nickname=" + nickname
				+ ", photo=" + photo + ", email=" + email + ", phone=" + phone + ", phoneString=" + photoString + ", status=" + status + ", roles=" + roles
				+ "]";
	}
	
	@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
	public static enum Status {
		NORMAL(1),LOCKED(0),DELETED(-1);
		@Getter private final int value;
		private static HashMap<Integer, Status> codeValueMap = new HashMap<>(3);
		static {
			for (Status status : Status.values()) {
				codeValueMap.put(status.value, status);
			}
		}
		public static Status getInstance(int code) {
			return codeValueMap.get(code);
		}
	}
}
