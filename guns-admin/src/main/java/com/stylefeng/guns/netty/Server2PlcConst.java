package com.stylefeng.guns.netty;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by Vincent on 2018-08-21.
 */
public class Server2PlcConst {


    public static final String PLC_TYPE_ScanDetail = "01";
    public static final String PLC_TYPE_Lchute = "02";
    public static final String PLC_TYPE_UploadMapping = "03";
    public static final String PLC_TYPE_SupplyPlat = "04";
    public static final String CONN_YUNDA = "05";
    public static final String SEND_YUNDA_DATA_TO_PLC = "06";

    public static String fillBlank(String barcode){
        if(barcode != null && barcode.length() == 30){
            return barcode;
        } else if(barcode == null){
            return "000000000000000000000000000000";
        } else if(barcode.length() >30){
            return "000000000000000000000000000000";
        } else{
            int fillCount = 30 - barcode.length();
            String blank = "";
            for(int i=0;i<fillCount;i++){
                blank = blank + "0";
            }
            return barcode + blank;
        }
    }


    public static String getAllDataForBuMa(String[] lchute, String barcode) {
        char a = '0';

        // 合并数据
        StringBuffer bs = new StringBuffer();
        for(int i=0;i<barcode.length();i++){
            a = barcode.charAt(i);
            bs.append(Integer.toHexString((byte)a));
        }

        String xx = bs.toString();

        if(lchute == null){
            xx += TQ_GetHexStringEx(0, 4);
        } else{
            xx += TQ_GetHexStringEx(lchute.length, 4);
            for(int i=0;i<lchute.length;i++) {
                xx += TQ_GetHexStringEx(Integer.valueOf(lchute[i]), 4);
            }
        }

        // 计算
        StringBuffer sb = new StringBuffer();
        for(int i=1;i<=xx.length();i++){
            if(i % 4 == 1){
                sb.append(" ").append(xx.substring(i-1, i+3));
            }
        }

        return sb.toString();
    }





    public static String getAllDataForSort(String trayCode, String[] chute, String barcode, String lchute, String businessType) {
        char a = '0';

        // 合并数据
        StringBuffer bs = new StringBuffer();

        for(int i=0;i<barcode.length();i++){
            a = barcode.charAt(i);
            bs.append(Integer.toHexString((byte)a));
        }

        String xx = TQ_GetHexStringEx(Integer.valueOf(trayCode), 4) + bs.toString();

        //增加逻辑格口
        if(lchute != null) {
            if(!StringUtils.isBlank(lchute) && !lchute.equals("null")) {
                xx += TQ_GetHexStringEx(Integer.valueOf(lchute), 8);
            }else {
                xx += TQ_GetHexStringEx(Integer.valueOf(0), 8);
            }
        }

        //增加业务种类
        if(businessType != null) {
            if(!StringUtils.isBlank(businessType) && !businessType.equals("null")) {
                xx += TQ_GetHexStringEx(Integer.valueOf(businessType), 4);
            }else {
                xx += TQ_GetHexStringEx(Integer.valueOf(0), 4);
            }
        }

        if(chute == null){
            xx += TQ_GetHexStringEx(Integer.valueOf(0), 4);
        } else{
            xx += TQ_GetHexStringEx(Integer.valueOf(chute.length), 4);
            for(int i=0;i<chute.length;i++) {
                xx += TQ_GetHexStringEx(Integer.valueOf(chute[i]), 4);
            }
        }

        // 计算
        StringBuffer sb = new StringBuffer();
        for(int i=1;i<=xx.length();i++){
            if(i % 4 == 1){
                sb.append(" ").append(xx.substring(i-1, i+3));
            }
        }

        return sb.toString();
    }

    public static String TQ_GetHexStringEx(int iNumber,int iCount){
        String s = Integer.toHexString(iNumber);
        int length = s.length();
        for(int i=0;i<iCount-length;i++) {
            s="0"+s;
        }
        return s;
    }

    public static void main(String[] args) {
        String a = fillBlank("12345");
        System.out.println(a);
    }

}
