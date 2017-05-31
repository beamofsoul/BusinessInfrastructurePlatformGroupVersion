package com.beamofsoul.bip.management.schedule;

import org.springframework.stereotype.Component;

/**
 * @ClassName CustomJobs
 * @Description 自定义定时任务器
 * @author MingshuJian
 * @Date 2017年3月31日 下午3:47:57
 * @version 1.0.0
 */
@Component
public class CustomJobs {

    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60 * 1000;

//    /**
//     * @Title: pushSystemLog
//     * @Description: 定期向后台用户推送系统日志信息
//     */
//    @Scheduled(fixedRate = ONE_SECOND)
//    public void pushSystemLog() {
//    	if (CustomSystemLogWebSocketHandler.getOnlineCount() > 0) {
//    		CustomSystemLogWebSocketHandler.sendMessageToAll(new TextMessage(new Date().toString()));
//    	}
//    }
}
