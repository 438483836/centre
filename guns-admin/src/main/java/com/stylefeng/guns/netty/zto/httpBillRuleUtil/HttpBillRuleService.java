package com.stylefeng.guns.netty.zto.httpBillRuleUtil;

import com.stylefeng.guns.core.util.ToolUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class HttpBillRuleService implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static final LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<>();

    private static final Logger logger = LoggerFactory.getLogger(HttpBillRuleService.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        startService();
    }

    private void startService() {
        HttpBillRuleClient httpBillRuleClient = new HttpBillRuleClient();
        try {
            httpBillRuleClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Thread thread = new Thread(() -> {
            while (true){
                try {
                    String msg = linkedBlockingQueue.take();
                    if (ToolUtil.isNotEmpty(msg)){
                        Thread.sleep(20);
                        try {
                            httpBillRuleClient.sendImpl(msg);
                        } catch (Exception e) {
                            logger.error("send bill-rule request to zto Exception1[{}]",e.getMessage());
                        }
                    }
                } catch (InterruptedException e) {
                    logger.error("send bill-rule request to zto Exception2[{}]",e.getMessage());
                }
            }
        });

        thread.start();
        logger.info("HttpBillRuleService startService success-------------->");
    }
}
