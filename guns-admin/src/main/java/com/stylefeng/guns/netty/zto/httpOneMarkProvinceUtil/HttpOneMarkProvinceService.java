package com.stylefeng.guns.netty.zto.httpOneMarkProvinceUtil;

import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.netty.zto.httpPortSyncUtil.HttpPortSyncClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

@Service
public class HttpOneMarkProvinceService implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static final LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<String>();

    private static final Logger logger = LoggerFactory.getLogger(HttpOneMarkProvinceService.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        startService();
    }

    private void startService() {
        HttpPortSyncClient httpPortSyncClient = new HttpPortSyncClient();
        try {
            httpPortSyncClient.start();
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
                            httpPortSyncClient.sendImpl(msg);
                        } catch (Exception e) {
                            logger.error("send oneMark-province request to zto Exception1[{}]",e.getMessage());
                        }
                    }
                } catch (InterruptedException e) {
                    logger.error("send oneMark-province request to zto Exception2[{}]",e.getMessage());
                }
            }
        });

        thread.start();
        logger.info("HttpOneMarkProvinceService startService success-------------->");
    }
}
