package com.beamofsoul.bip.management.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.beamofsoul.bip.service.CodeRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName CustomJobs
 * @Description 自定义定时任务器
 * @author MingshuJian
 * @Date 2017年3月31日 下午3:47:57
 * @version 1.0.0
 */
@Slf4j
@Component
public class CustomJobs {

    public static final long ONE_SECOND = 1000;
    public static final long ONE_MINUTE = 60 * 1000;

    @Autowired
    private CodeRecordService codeRecordService;

    /**
     * @Title: clearExpiredDigitCode
     * @Description: 定期删除过期的找回密码验证码记录
     */
    @Scheduled(fixedRate = ONE_SECOND)
    public void clearExpiredDigitCode() {
        long count = codeRecordService.deleteExpiredOnes(null);
        if (count > 0) {
            log.debug("已删除" + count + "条过期的找回密码验证码记录...");
        }
    }
    
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
