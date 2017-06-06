package com.beamofsoul.bip.controller;

import static com.beamofsoul.bip.management.util.JSONUtils.newInstance;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import org.mdkt.compiler.InMemoryJavaCompiler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping("/admin/development")
public class DevelopmentController extends BaseAbstractController {

	@RequestMapping(value = "/adminList")
	public String adminList() {
		return "/development/admin_development_main";
	}
	
	@RequestMapping(value = "/buildEntity", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject buildEntity(@RequestBody Map<String, Object> map) {
		String entityName = map.get("entityName").toString();
		StringBuffer entityContent = getEntityContent();
		Object instance = null;
		try {
			try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(getFullSourcePath(entityName)), StandardCharsets.UTF_8)) {  
				writer.write(entityContent.toString());  
			}
			Class<?> clazz = InMemoryJavaCompiler.compile("com.beamofsoul.bip.entity." + entityName, entityContent.toString());
			instance = clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newInstance("instance", instance.toString());
	}

	private String getFullSourcePath(String entityName) {
		return System.getProperty("user.dir") + "/src/main/java/com/beamofsoul/bip/entity/" + entityName + ".java";
	}

	private StringBuffer getEntityContent() {
		StringBuffer entityContent = new StringBuffer();
		entityContent.append("package com.beamofsoul.bip.entity;\r\n\r\n");
		entityContent.append("public class TestEntity {\r\n\r\n");
		entityContent.append("	@Override\r\n");
		entityContent.append("	public String toString() {\r\n");
		entityContent.append("		return \"This is the result of toString method from TestEntity instance...\";\r\n");
		entityContent.append("	}\r\n");
		entityContent.append("}");
		return entityContent;
	}
}
