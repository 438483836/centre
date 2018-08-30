package com.stylefeng.guns.netty.complementInfoUtil;

import com.stylefeng.guns.netty.CommunicationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ComplementInfoService implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(ComplementInfoService.class);
    /**
     * 每个处理的方法的Bean都可以通过applicationContext来获得
     */
    private static ApplicationContext applicationContext;
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ComplementInfoService.applicationContext = applicationContext;
        this.startService(CommunicationConfig.ComplementInfoPort);
    }
    private void startService(int port){
        ComplementInfoServer server = new ComplementInfoServer(applicationContext, port);
        try {
            Thread thread = new Thread(server);
            thread.start();
        } catch (Exception e) {
            logger.info("ComplementInfoService startService Exception[{}]-------------->",e.getMessage());
        }
        logger.info("ComplementInfoService startService success-------------->");
    }
}
