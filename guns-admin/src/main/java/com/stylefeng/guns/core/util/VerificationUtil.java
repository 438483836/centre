package com.stylefeng.guns.core.util;

import com.stylefeng.guns.netty.requestEntity.PLCRequestEntity;

/**
 * Created by Vincent on 2018-08-21.
 */
public class VerificationUtil {

    /**
     * 上件台校验码
     * @param plcRequestEntity
     * @return 16进制字符串
     */
    public static String upperPieceV(PLCRequestEntity plcRequestEntity){
        StringBuffer data = new StringBuffer();
        data.append("01");
        data.append("1E");
        data.append("02");
        data.append(plcRequestEntity.getBillCode().length());
        data.append(plcRequestEntity.getBillCode());
        data.append(plcRequestEntity.getChute());
        data.append("000000");
        String lString = data.toString();
        String hexStr = TypeConversion.sumHexStringBy2(lString,2);

        if (hexStr.length() < 4){
            int i = 0;
            int j = 4 - hexStr.length();
            String val = String.valueOf(j);
            String str = String.format("%0" + val + "d",i);
            String newHexStr = str + hexStr;
            return newHexStr;
        }else {
            return hexStr;
        }

    }

}
