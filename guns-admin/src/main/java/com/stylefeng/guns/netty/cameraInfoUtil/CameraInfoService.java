package com.stylefeng.guns.netty.cameraInfoUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * Created by Vincent on 2018-08-23.
 */
@Service
public class CameraInfoService implements ApplicationContextAware{

    private static final Logger logger = LoggerFactory.getLogger(CameraInfoService.class);

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        startService(8080);
    }

    private void startService(int port){
        CameraInfoServer server = new CameraInfoServer(applicationContext, port);
        /*try {
            Thread thread = new Thread(server);
            thread.start();
            HttpBillRuleRequestInfoService.bestExpressRequestQueue.put(Supply2ServerConst.bestExpressBillRuleURL);
            IParamConfigService paramConfigService = applicationContext.getBean(IParamConfigService.class);
            EntityWrapper<ParamConfig> wrapper = new EntityWrapper<>();
            wrapper.eq("keyy","pipeline");
            ParamConfig paramConfig = paramConfigService.selectOne(wrapper);
            HttpPortInfoSyncRequestInfoService.bestExpressRequestQueue.put(paramConfig.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
