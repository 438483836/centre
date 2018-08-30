package com.stylefeng.guns.netty.cameraInfoUtil;

import com.stylefeng.guns.cache.BillRuleCache;
import com.stylefeng.guns.core.util.DateUtil;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.netty.requestEntity.PLCRequestEntity;
import com.stylefeng.guns.netty.supplyPlatUtil.SupplyPlatService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Vincent on 2018-08-23.
 */
@Service
public class CameraInfoInHandler extends ChannelInboundHandlerAdapter implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(CameraInfoInHandler.class);

    private static ApplicationContext applicationContext;

    public static AtomicInteger ai = new AtomicInteger(-1);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        String geSickData = (String) msg;
        if (ToolUtil.isEmpty(geSickData)) {
            logger.info("get geSickData data is null");
            return ;
        }
        InetSocketAddress socketAddress = (InetSocketAddress) ctx.channel().remoteAddress();
        String remoteIp = socketAddress.getAddress().getHostAddress();

        String[] contents = geSickData.split("\\+");
        if(contents.length!=2)
        {
            logger.info("Receive SICK data is ："+geSickData+". by"+remoteIp);
            return ;
        }

        String barcodes[] = getInt(contents[1]);
        Set<String> billCodes = new HashSet<>();

        for(String str : barcodes) {
            billCodes.add(str);
        }

        if (ToolUtil.isEmpty(billCodes)) {
            logger.info("billCode is empty");
            return;
        }

        billCodes = BillRuleCache.matchBarCode(billCodes);

        List<String> listCodes= new ArrayList<>(billCodes);

        logger.info("Receive geSickData Data:{},remoteIp:{}", geSickData,remoteIp);

        //分拣线编码
        //String pipeline = ParamConigCache.getPipeline();
        //线号
        //String lineNo = ParamConigCache.getLineNo();

        //分拣唯一ID
        String sortId = newSortId();

        String[] arr = getInt(contents[0]);
        String trayCode="";
        for (int i=0;i<arr.length;i++){
            trayCode +=arr[i];
        }

        if(trayCode.length()>=3)
        {
            trayCode = trayCode.substring(trayCode.length()-3,trayCode.length());

        }
        else {
            for (int i=trayCode.length();i<3;i++){
                trayCode = "0"+trayCode;
            }
        }

        //如果有多个条码。直接下异常口，不做分拣请求（可能一个小车上有多件货,避免错分）
        /*List<String> excChutelist = ParamConigCache.getExceptionChute();
        String excChutes = "";
        for (String code:excChutelist){
            if(ToolUtil.isEmpty(excChutes)){
                excChutes = code;
            }else {
                excChutes = excChutes+"|"+code;
            }
        }*/

        if (listCodes.size() > 1) {
            PLCRequestEntity plcRequestEntity = new PLCRequestEntity(sortId,listCodes.get(0),trayCode,"","1");
            SupplyPlatService.plcRequestQueue.add(plcRequestEntity);
            logger.error("There are multiple barcodes[{}]",listCodes.toString());
            return;
        }

    }

    @Override
    public boolean isSharable() {
        System.out.println("==============CameraGetInfoInHandler handler-sharable==============");
        return super.isSharable();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============CameraGetInfoInHandler channel-register==============");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============CameraGetInfoInHandler channel-unregister==============");
    }
    //新客户端接入
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============CameraGetInfoInHandler channel-Active==============");
    }
    //客户端断开
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("==============CameraGetInfoInHandler channel-inactive==============");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("==============channel-read-complete==============");
        ctx.flush();
    }

    //异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //打印异常
        logger.error(cause.getMessage());
        //关闭通道
        ctx.close();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static String newSortId() {
        int machineId = 1;//最大支持1-9个集群机器部署

        return machineId + DateUtil.getAllTime()+ incrementAndGet();
    }


    public static int incrementAndGet() {
        int current;
        int next;
        do {
            current = ai.get();
            next = current >= 9?0:current + 1;
        } while(!ai.compareAndSet(current, next));

        return next;
    }

    public static String[] getInt(String str){
        String[] arr = str.split("[^0-9]");
        for(int i=0;i<arr.length;i++){
            logger.info("content ".concat(arr[i]));
        }
        return arr;
    }

}
