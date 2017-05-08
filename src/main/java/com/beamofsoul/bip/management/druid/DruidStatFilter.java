package com.beamofsoul.bip.management.druid;

import javax.servlet.annotation.WebFilter;
import com.alibaba.druid.support.http.WebStatFilter;
import javax.servlet.annotation.WebInitParam;

/**
 * Druid状态过滤器
 * 该配置中的所有文件或内容将被Druid过滤器忽略
 * @author MingshuJian
 */
@WebFilter(filterName="druidWebStatFilter",urlPatterns="/*",initParams={
	@WebInitParam(name="exclusions",value="*.js,*.gif,*.jpg,*.bmp,*.png,*.css,*.ico,/druid/*")},
	asyncSupported=true)
public class DruidStatFilter extends WebStatFilter {

}
