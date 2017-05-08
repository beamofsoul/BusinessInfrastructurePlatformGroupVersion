package com.beamofsoul.bip;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
@EnableCaching
@EnableScheduling
@EnableAsync //for websocket
public class ApplicationEntry extends SpringBootServletInitializer  {
	
	@Override
	protected SpringApplicationBuilder configure(
			SpringApplicationBuilder builder) {
		return builder.sources(ApplicationEntry.class);
	}
	
	public static void main(String[] args) {
		/**
		 * 关闭Banner - 启动服务器后用符号拼接成的[Spring]图形
		 * SpringApplication app = new SpringApplication(ApplicationEntry.class);
		 * app.setBannerMode(Banner.Mode.OFF);
		 * app.run(args); 
		 * 
		 * PS: 1.Banner.Mode.OFF:关闭
		 *     2.Banner.Mode.CONSOLE:控制台输出，默认方式
		 *     3.Banner.Mode.LOG:日志输出方式
		 *     4.或在properties文件中设置spring.main.show-banner=false
		 */
		SpringApplication.run(ApplicationEntry.class, args);
	}
}
