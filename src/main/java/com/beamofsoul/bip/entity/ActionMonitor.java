package com.beamofsoul.bip.entity;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 行为监控
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "T_ACTION_MONITOR")
public class ActionMonitor extends BaseAbstractEntity {

	private static final long serialVersionUID = 7936770852487042470L;
	
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column
	private long id;
	/**
	 * 操作行为用户
	 */
	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;
	/**
	 * 针对目标
	 */
	@Column
	private String target;
	/**
	 * 特定操作
	 */
	@Column(name = "specific_action")
	private String specificAction;
	/**
	 * 对目标的影响
	 */
	@Column
	private String effect;
	/**
	 * 客户端ip地址
	 */
	@Column
	private String ipAddress;
	/**
	 * 客户端mac地址
	 */
	private String macAddress;
	/**
	 * 可能对系统造成的危害的危险等级
	 */
	@Column(name = "hazard_level")
	private Integer hazardLevel; //0: 毁灭性的, 1: 极恶性的, 2: 恶性的, 3: 良性的, 4: 可忽略的
	
	@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
	public static enum HazardLevel {
		CATASTROPHIC(0),EXTREMELY_VIRULENT(1),VIRULENT(2),BENIGN(3),INSIGNIFICANT(4);
		@Getter private final Integer value;
		private static HashMap<Integer, HazardLevel> codeValueMap = new HashMap<>(5);
		static {
			for (HazardLevel hazardLevel : HazardLevel.values()) {
				codeValueMap.put(hazardLevel.value, hazardLevel);
			}
		}
		
		public static HazardLevel getInstance(Integer code) {
			return codeValueMap.get(code);
		}
	}
}