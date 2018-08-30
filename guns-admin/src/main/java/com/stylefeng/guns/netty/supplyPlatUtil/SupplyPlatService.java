package com.stylefeng.guns.netty.supplyPlatUtil;

import com.stylefeng.guns.cache.PlcConfigCache;
import com.stylefeng.guns.core.util.VerificationUtil;
import com.stylefeng.guns.netty.Server2PlcConst;
import com.stylefeng.guns.netty.requestEntity.PLCRequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Vincent on 2018-08-21.
 */
@Service
public class SupplyPlatService implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(SupplyPlatService.class);

    private ApplicationContext applicationContext;

    private static SupplyPlatClient supplyPlatClient;

    public static final LinkedBlockingQueue<PLCRequestEntity> plcRequestQueue = new LinkedBlockingQueue<PLCRequestEntity>();

    public static void sendData2Plc(String ip ,int port ,PLCRequestEntity plcRequestEntity){
        try {
            StringBuffer data = new StringBuffer();
            data.append("A5A5");
            data.append("01");
            data.append("1E");
            data.append("02");//上位机编号
            data.append(plcRequestEntity.getBillCode().length());
            String billCode = Server2PlcConst.fillBlank(plcRequestEntity.getBillCode());
            data.append(billCode);
            data.append(plcRequestEntity.getChute().split("\\|"));
            data.append("000000");
            data.append(VerificationUtil.upperPieceV(plcRequestEntity));//校验
            data.append("5A5A");
            supplyPlatClient.sendSyncImpl(ip, port, data.toString());

            logger.info("SupplyPlatService sendData2MesPlc success,sortingId:[{}],barcode[{}],trayCode:[{}], chute:[{}],businessType:[{}]",plcRequestEntity.getSortingId(),plcRequestEntity.getBillCode(),plcRequestEntity.getTrayCode(),plcRequestEntity.getChute(),plcRequestEntity.getBusinessType());


        } catch (Exception e) {
            logger.error("SupplyPlatService sendData2MesPlc success,sortingId:[{}],barcode[{}],trayCode:[{}], chute:[{}],businessType:[{}]",plcRequestEntity.getSortingId(),plcRequestEntity.getBillCode(),plcRequestEntity.getTrayCode(),plcRequestEntity.getChute(),plcRequestEntity.getBusinessType());
        }

    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        this.startService();
    }

    private void startService() {
        this.supplyPlatClient = new SupplyPlatClient(this.applicationContext);

        try {
            this.supplyPlatClient.start();

            Thread thread = new Thread(() -> {
                try{
                    while(true) {
                        PLCRequestEntity plcRequestEntity = plcRequestQueue.take();
                        if(plcRequestEntity != null) {
                            try {
                                Thread.sleep(20);
                                sendData2Plc(PlcConfigCache.getSortPlcConfig().getIp(), PlcConfigCache.getSortPlcConfig().getPort(), plcRequestEntity);
                            } catch (InterruptedException e) {
                                logger.error("send sorting data to plc error2:", e);
                            }
                        }
                    }
                }catch (Exception e){
                    logger.error("send sorting data to plc error:",e);
                }
            });

            thread.start();

        }catch (Exception e) {

        }

    }
}
