package com.beamofsoul.bip.management.control;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.entity.ActionMonitor;
import com.beamofsoul.bip.entity.User;
import com.beamofsoul.bip.management.util.AnnotationRepositoryNameMapping;
import com.beamofsoul.bip.management.util.ClientInformationUtils;
import com.beamofsoul.bip.management.util.CurrentThreadDataManager;
import com.beamofsoul.bip.management.util.SpringUtils;
import com.beamofsoul.bip.management.util.UserUtils;
import com.beamofsoul.bip.service.ActionMonitorService;
import com.beamofsoul.bip.service.UserService;

import jodd.util.ReflectUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName MonitoringAspect
 * @Description 操作行为监控切面实现类
 * @author MingshuJian
 * @Date 2017年4月12日 下午1:41:29
 * @version 1.0.0
 */
@Slf4j
@Aspect
@Component
public class MonitoringAspect {
	
	private static final String ENTITY_ID = "####ENTITYID####";
	private static final String UNKNOWN = "unknown";
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ActionMonitorService actionMonitorService;
	
	@Pointcut(value="@annotation(monitoring)")
	public void locateAnnotation(Monitoring monitoring) {}
	
	@Around("locateAnnotation(monitoring)")
	public Object doAround(ProceedingJoinPoint joinPoint, Monitoring monitoring) throws Throwable {
		Class<?> targetEntity = monitoring.target(); //注解中输入的目标对象的类对象
		String targetFullPath = targetEntity.getName(); //注解中输入的目标对象全路径名
		String key = monitoring.key(); //注解中输入的指向目标方法输入参数的标识
		Object[] args = joinPoint.getArgs(); //目标方法输入参数数组
		Object targetEntityInstance = getTargetEntityInstanceFromArguments(key, args); //目标方法输入参数中获取到的对目标对象有影响的业务实体
		StringBuffer effects = new StringBuffer(); //存放最终影响的字符串缓存对象		
		
		//如果注解中没有输入了目标对象的类对象，而使用了默认值(Object.class)
		//同时注解中输入了指向目标方法输入参数的标识
		//则默认目标对象的类对象为目标方法输入参数的同类型对象
		if (targetEntity.equals(Object.class)) {
			if (targetEntityInstance != null) {
				targetEntity = targetEntityInstance.getClass();
				targetFullPath = targetEntity.getName();
			} else {
				targetFullPath = UNKNOWN;
			}
		}
		
		//如果目标方法输入参数中存在对目标对象有影响的业务实体
		if (targetEntityInstance != null) {
			//如果输入参数对象与注解中输入的目标对象是同一类型对象
			if (targetEntityInstance.getClass().equals(targetEntity)) {
				//获取输入参数对象的主键id值
				Object entityIdObject = invokeMethod(targetEntityInstance, "id");
				Long entityId = entityIdObject == null ? null : (Long) entityIdObject;
				
				//如果输入参数对象的主键id值为空，说明当前目标方法有可能是在创建业务对象，将输入参数对象进行持久化到数据库中
				//否则，根据输入参数对象的主键id值，获取到该类型完整的数据记录，以便后续对特定字段进行进一步比较，从而识别当前操作行为的影响
				Object originalEntity = null;
				if (entityId != null) {
					originalEntity = getEntity(targetEntity, entityId);
				}
				
				//获取并解析注解中输入的重点观察指标(entity的field)，允许多个影响值同时存在，并以英文逗号(,)作为分割符
				String[] effectFields = monitoring.effect().split(",");
				String originalValue = null; //准备装载从数据库中查询到的原始对象记录的某个特定属性值
				String effectedResult = null; //准备装载从目标方法输入参数中辨识出的输入参数对象的某个特定属性的值
				
				//遍历重点观察指标，同时生产并拼接处对目标对象的具体影响信息
				for (String effectField : effectFields) {
					//获取输入参数对象中特定重点观察指标的值
					effectedResult = toString(invokeMethod(targetEntityInstance, effectField));
					
					//如果不能通过输入参数对象的主键id获取到该记录的原始记录，说明当前目标方法有可能是在创建业务对象，将其创建对象的细节记录到最终影响的字符串缓存对象中
					if (originalEntity == null) {
						effects.append("entity " + ENTITY_ID + " created field " + effectField + " value is " + effectedResult + "\r\n");
					} else { //否则，将输入参数对象当前重点观察指标的值与数据库中该记录的值进行比较，如果发生变化，则将其变化细节记录到最终影响的字符串缓存对象中
						originalValue = toString(invokeMethod(originalEntity, effectField));
						if (!originalValue.equals(effectedResult)) {
							effects.append("entity "+ entityId + " field "+effectField+" value changed from " + originalValue + " to " + effectedResult + "\r\n");
						}
					}
				}
			} else { //如果输入参数对象与目标对象不是同一类型对象，则不能识别当前操作动作对具体实体类和数据库中数据的影响，但是仍然可以打印方法输入参数，以便给需要查看此数据记录的人尽可能的指引
				effects.append("cannot recognize which kind of effect has been made, but arguments are ");
				for (Object argument : args) {
					effects.append(argument.getClass().isArray() ? Arrays.deepToString((Object[]) argument) : argument);
					effects.append(",");
				}
				effects.delete(effects.lastIndexOf(","), effects.length());
			}
		}
		
		Object returnValue = joinPoint.proceed();
		effects = getId4CreateAction(effects, returnValue);
		actionMonitorService.create(generateActionMonitor(joinPoint, monitoring, targetFullPath, effects));
		return returnValue;
	}

	private StringBuffer getId4CreateAction(StringBuffer effects, Object returnValue) {
		//如果预测是对业务对象的创建功能，尝试去获取创建后得到的业务对象主键id，并拼接effects中
		Object entityId = "";
		try {
			entityId = invokeMethod(returnValue, "id");
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			log.debug("cannot recognize method as a create type method");
		}
		effects = new StringBuffer(effects.toString().replace(ENTITY_ID, entityId.toString()));
		return effects;
	}

	private Object getTargetEntityInstanceFromArguments(@NonNull String key, Object[] args) {
		try {
			int keyIndex = 0;
			if (args != null && args.length > 0) {
				if (key.startsWith("#p")) {
					key = key.substring(2);
					keyIndex = Integer.valueOf(key).intValue();
				}
				return args[keyIndex];
			}
		} catch (Exception e) {
			log.warn("unrecognized monitoring key found in annotation Monitoring", e);
		}
		return null;
	}

	private ActionMonitor generateActionMonitor(ProceedingJoinPoint joinPoint, Monitoring monitoring, String targetFullPath, StringBuffer effects) {
		Object ipAddress = CurrentThreadDataManager.getData(ClientInformationUtils.CLIENT_IP_ADDRESS);
		Object macAddress = CurrentThreadDataManager.getData(ClientInformationUtils.CLIENT_MAC_ADDRESS);
		Object userId = CurrentThreadDataManager.getData(UserUtils.CURRENT_USER);
		User currentUser = userId == null ? null : userService.findById(Long.valueOf(userId.toString()));
		
		return ActionMonitor
			.builder()
			.target(targetFullPath)
			.specificAction(joinPoint.getTarget().getClass().toString().split(" ")[1] + "." + joinPoint.getSignature().getName())
			.effect(effects.length() == 0 ? UNKNOWN : effects.toString())
			.hazardLevel(monitoring.hazardLevel().getValue())
			.ipAddress(ipAddress == null ? UNKNOWN : ipAddress.toString())
			.macAddress(macAddress == null ? UNKNOWN : macAddress.toString())
			.user(currentUser)
			.build();
	}

	@SuppressWarnings("unchecked")
	private Object getEntity(Class<?> targetEntity, Long entityId) {
		String entityRepositoryName = AnnotationRepositoryNameMapping.repositoryMap.get(targetEntity.getSimpleName().toLowerCase());
		CrudRepository<?, Long> crudRepository = (CrudRepository<?, Long>) SpringUtils.getBean(entityRepositoryName);
		return crudRepository.findOne(entityId);
	}

	private Object invokeMethod(Object targetEntityInstance, String effectField) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return ReflectUtil.invoke(targetEntityInstance, "get" + StringUtils.capitalize(effectField), new Object[]{});
	}
	
	public static String toString(Object object) {
		return object == null ? "null" : String.valueOf(object);
	}
}
