package com.stylefeng.guns.netty;

import java.net.URI;

/**

* @Description:    通信参数配置

* @Author:         sipengfei

* @CreateDate:     2018/8/17 16:44

* @UpdateUser:     sipengfei

* @UpdateDate:     2018/8/17 16:44

* @UpdateRemark:   修改内容

* @Version:        1.0

*/
public class CommunicationConfig {

    public static final String ztoUrl = "http://10.97.10.21:8090/branchweb/sortservice";

    public static final String ztoIp = "10.97.10.21";

    public static final int ztoPort = 8090;


    //相机信息绑定端口
    public static final int CameraGetInfoPort = 9195;

    //补码绑定端口
    public static final int ComplementInfoPort = 9196;

    //开关机信息绑定端口
    public static final int RequestMappingPort = 10001;

    //集包信息绑定端口
    public static final int PackagePort = 10002;

    //下包信息绑定端口
    public static final int DownSealMailPort = 10003;
}
