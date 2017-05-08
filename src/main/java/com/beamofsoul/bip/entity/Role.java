package com.beamofsoul.bip.entity;

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

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @ClassName Role
 * @Description 系统角色表实体类
 * @author MingshuJian
 * @Date 2017年2月7日 上午10:42:23
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor

@Entity
@Table(name = "T_ROLE")
public class Role extends BaseAbstractEntity {

	private static final long serialVersionUID = -3376152299137758615L;
	private static final int MAX_PRIORITY = 99;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "priority")
	private int priority; //优先级数值越低，代表优先级越大，角色normal的优先级为99

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE }, fetch = FetchType.LAZY)
	@JoinTable(name = "T_ROLE_PERMISSION", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = {
			@JoinColumn(name = "permission_id") })
	private Set<Permission> permissions = new HashSet<Permission>(0);

	public Role(Long id) {
		this.id = id;
	}

	public Role(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
		this.priority = MAX_PRIORITY;
	}
	
	public Role(Long id, String name, int priority) {
		super();
		this.id = id;
		this.name = name;
		this.priority = priority;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", priority=" + priority + "]";
	}
}