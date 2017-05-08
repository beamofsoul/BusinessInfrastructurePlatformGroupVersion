package com.beamofsoul.bip.management.druid;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.alibaba.druid.support.http.StatViewServlet;

/**
 * Druid数据源状态监控
 * @author MingshuJian
 */
@WebServlet(urlPatterns="/druid/*",initParams={
	@WebInitParam(name="allow",value=""), //IP白名单，以IP,IP形式存储，如127.0.0.1,192.168.1.1
	@WebInitParam(name="deny",value=""), //IP黑名单，存在共同时，deny优先于allow
	@WebInitParam(name="loginUsername",value="root"),
	@WebInitParam(name="loginPassword",value="root"),
	@WebInitParam(name="resetEnable",value="false")}, //禁止HTML页面上的"Reset All"功能
	asyncSupported=true)
public class DruidStatViewServlet extends StatViewServlet {
	private static final long serialVersionUID = 8690242272455275524L;
}
