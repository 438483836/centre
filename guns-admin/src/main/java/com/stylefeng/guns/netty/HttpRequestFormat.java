package com.stylefeng.guns.netty;

import com.alibaba.fastjson.JSONObject;
import com.stylefeng.guns.core.util.JsonUtil;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.netty.zto.httpSortInfoClient.SortInfoEntity;

import java.util.HashMap;
import java.util.Map;

public class HttpRequestFormat {


    //开关流水线的请求
    public static String switchLine(String status){
        long switchTime=System.currentTimeMillis();
        String sortMode="sorting";
        String pipeLine="57450-001";
        JSONObject data = new JSONObject();
        data.put("pipeLine",pipeLine);
        data.put("switchTime",switchTime);
        data.put("status",status);
        data.put("sortMode",sortMode);
        String data_digest= MD5Util.encrypt(data.toString());
        String msg_type="WCS_PIPELINE_STATUS";
        String company_id="zto";
        return "data="+data+"&data_digest="+data_digest+"&msg_type="+msg_type+"&company_id="
                +company_id;
    }

    //校验时间的请求
    public static String timeVerify(){
        return "data=null&data_digest=&msg_type=ZTO_INSPECTION_TIME&company_id=zto";
    }

    //端口配置信息请求同步
    public static String portSync(){

        Map<String,String> map  = new HashMap<>();
        map.put("data","57450-001");
        map.put("data_digest","");
        map.put("msg_type","WCS_SORTING_SETTING");
        map.put("company_id","zto");

        String digestContent= JsonUtil.toJsonByObj(map);
        String digestedContent=MD5Util.encrypt(digestContent);

        String sendContent="data={\"".concat(map.get("data")).concat("\"}&data_digest=").concat(digestedContent)
                .concat("&msg_type=WCS_SORTING_SETTING&company_id=zto");

        return sendContent;
    }

    //一段码省市区请求
    public static String oneMarkProvince(){

        Map<String,String> map  = new HashMap<>();
        map.put("data","57450-001");
        map.put("data_digest","");
        map.put("msg_type","GET_LINE_INFO");
        map.put("company_id","zto");

        String digestContent= JsonUtil.toJsonByObj(map);
        String digestedContent=MD5Util.encrypt(digestContent);

        String sendContent="data={\"".concat(map.get("data")).concat("\"}&data_digest=").concat(digestedContent)
                .concat("&msg_type=WCS_SORTING_SETTING&company_id=zto");

        return sendContent;
    }

    //面单规则请求
    public static String billRule(){
        String sendContent="data=null".concat("&data_digest=");
        sendContent=sendContent.concat("&msg_type=GET_BILL_RULE&company_id=zto");
        return sendContent;
    }

    //分拣信息请求
    public static String sortInfo(SortInfoEntity sortInfoEntity){

        String digestContent= JsonUtil.toJsonByObj(sortInfoEntity);
        String digestContentMD5=MD5Util.encrypt(digestContent);
        String sendContent="data=".concat(digestContent).concat("&data_digest=").concat(digestContentMD5);
        sendContent=sendContent.concat("&msg_type=WCS_SORTING_INFO&company_id=zto");
        return sendContent;
    }

    public static void main(String[] args) {

        SortInfoEntity sortInfoEntity = new SortInfoEntity();
        sortInfoEntity.setBillCode("");
        sortInfoEntity.setPipeline("");
        sortInfoEntity.setTurnNumber(1);
        sortInfoEntity.setRequestTime(System.currentTimeMillis());
        sortInfoEntity.setWeight(0.2f);
        sortInfoEntity.setIncomeClerk("");
        sortInfoEntity.setScanMan("");
        sortInfoEntity.setTrayCode("1");
        sortInfoEntity.setInfocamareCode("1");
        sortInfoEntity.setSupplyCode("1");
        System.out.println(sortInfo(sortInfoEntity));
    }
}
