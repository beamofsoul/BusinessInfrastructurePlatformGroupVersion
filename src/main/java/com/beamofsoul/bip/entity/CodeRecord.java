package com.beamofsoul.bip.entity;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * @ClassName CodeRecord
 * @Description 验证码记录实体类
 * @author MingshuJian
 * @Date 2017年3月24日 上午11:03:40
 * @version 1.0.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "T_CODE_RECORD")
public class CodeRecord extends BaseAbstractEntity  {

	private static final long serialVersionUID = 3734655585425554384L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;
	
	@Column(name = "user_id")
	private long userId;
	
	@Column(name = "code")
	private String code;
	
	@Column(name = "type")
	private int type;
	
	@Column(name = "expiredDate")
	private Date expiredDate;
	
	@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
	public static enum Type {
		FORGOT_PASSWORD(1);
		@Getter private final int value;
		private static HashMap<Integer, Type> codeValueMap = new HashMap<>(1);
		static {
			for (Type type : Type.values()) {
				codeValueMap.put(type.value, type);
			}
		}
		
		public static Type getInstance(int code) {
			return codeValueMap.get(code);
		}
	}
}
