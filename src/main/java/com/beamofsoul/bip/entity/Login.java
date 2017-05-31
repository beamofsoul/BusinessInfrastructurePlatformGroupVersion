package com.beamofsoul.bip.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
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
 * @ClassName Login
 * @Description 系统登录记录表实体类
 * @author MingshuJian
 * @Date 2017年5月31日 上午10:27:23
 * @version 1.0.0
 */
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data

@Entity
@Table(name = "T_LOGIN")
public class Login extends BaseAbstractEntity {

	private static final long serialVersionUID = 1336473767251163468L;
	
	/**
	 * id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	protected Long id;
	/**
	 * 登录用户
	 */
	@ManyToOne
    @JoinColumn(name = "user_id")
	private User user;
	/**
	 * IP地址
	 */
	@Column(name = "ip_address")
	private String ipAddress;
	/**
	 * 所用设备品牌
	 */
	@Column(name = "brand")
	private String brand;
	/**
	 * 所用设备型号
	 */
	@Column(name = "model")
	private String model;
	/**
	 * 所用设备屏幕尺寸
	 */
	@Column(name = "screen_size")
	private String screenSize;
	/**
	 * 所用设备操作系统
	 */
	@Column(name = "operating_system")
	private String operatingSystem;
	/**
	 * 访问的浏览器
	 */
	@Column(name = "browser")
	private String browser;
}
