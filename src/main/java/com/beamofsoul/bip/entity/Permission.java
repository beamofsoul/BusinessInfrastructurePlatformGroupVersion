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
 * @ClassName Permission
 * @Description 系统权限表实体类
 * @author MingshuJian
 * @Date 2017年2月7日 上午10:42:23
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)

@Entity
@Table(name = "T_PERMISSION")
public class Permission extends BaseAbstractRelationalEntity {

	private static final long serialVersionUID = -7700581193909669401L;
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;
	/**
	 * 名称
	 */
	@Column(name = "name")
	private String name;
	/**
	 * 后台访问秘钥 - 在Controller中方法上使用Spring Security注解时使用
	 */
	@Column(name = "action")
	private String action;
	/**
	 * 页面访问秘钥 - 在JSP页面中使用Spring Security标签时使用
	 */
	@Column(name = "url")
	private String url;
	/**
	 * 资源类型 - menu / button
	 */
	@Column(name = "resourceType", columnDefinition = "enum('menu','button')")
	private String resourceType;
	/**
	 * 父节点ID
	 */
	@Column(name = "parentId")
	private Long parentId;
	/**
	 * 所在分组
	 */
	@Column(name = "[group]")
	private String group;
	/**
	 * 排序字段
	 */
	@Column(name = "sort")
	private Long sort;
	/**
	 * 可用状态
	 */
	@Column(name = "available")
	private Boolean available = Boolean.FALSE;
	
	@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
	public static enum ResourceType {
		BUTTON("button"),MENU("menu");
		@Getter private final String value;
		private static HashMap<String, ResourceType> codeValueMap = new HashMap<>(3);
		static {
			for (ResourceType resourceType : ResourceType.values()) {
				codeValueMap.put(resourceType.value, resourceType);
			}
		}
		
		public static ResourceType getInstance(String code) {
			return codeValueMap.get(code);
		}
	}

	@Override
	public String toString() {
		return "Permission [id=" + id + ", name=" + name + ", action=" + action + ", url=" + url + ", resourceType="
				+ resourceType + ", parentId=" + parentId + ", group=" + group + ", sort=" + sort + ", available="
				+ available + "]";
	}
}
