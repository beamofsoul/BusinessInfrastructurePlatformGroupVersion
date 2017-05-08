package com.beamofsoul.bip.management.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.management.util.AnnotationRepositoryNameMapping;
import com.beamofsoul.bip.management.util.AnnotationServiceNameMapping;
import com.beamofsoul.bip.management.util.DatabaseTableEntityMapping;
import com.beamofsoul.bip.management.util.DatabaseUtils;
import com.beamofsoul.bip.management.util.RolePermissionsMapping;
import com.beamofsoul.bip.service.RolePermissionService;

import lombok.extern.slf4j.Slf4j;

/**
 * 实现自定义CommandLineRunner
 * 自定义服务器启动时执行某些操作
 * SpringBoot在应用程序启动后,会遍历CommondLineRunner接口的实例并运行它们的run方法
 * @Order 该注解用于排序多个自定义实现的CommandLineRunner实例,其中数值越小,越先执行
 * @author MingshuJian
 */
@Component
@Slf4j
@Order(1)
public class CustomDataFillingStartupRunner implements CommandLineRunner {
	
	@Autowired
	private RolePermissionService rolePermissionService;

	@Override
	public void run(String... args) throws Exception {
		/**
		 * 这里的args就是程序启动的时候进行设置的:
		 * 		eg. SpringApplication.run(App.class, new String[]{"hello,","world"});
		 * 
		 * eclipse中给java应用传args参数的方法如下:
		 * 		1.先写好Java代码，比如文件名为IntArrqy.java
		 * 		2.在工具栏或菜单上点run as下边有个Run Configuration
		 * 		3.在弹出窗口点选第二个标签arguments
		 * 		4.把输入的参数写在program argumenst,多个参数使用空格隔开
		 * 完成后点run即可通过运行结果看到参数使用情况了。
		 */
		log.info("服务启动执行,执行加载数据等操作...");
		RolePermissionsMapping.fill(rolePermissionService.findAllRolePermissionMapping());	
		DatabaseUtils.loadDatabaseTableNames();
		DatabaseTableEntityMapping.initTableEntityMap();
		AnnotationServiceNameMapping.loadServiceMap();
		AnnotationRepositoryNameMapping.loadRepositoryMap();
	}
}
