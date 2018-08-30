package com.stylefeng.guns.core.util;

/**
 * Created by Vincent on 2018-08-27.
 */
public class TypeConversion {

    /**
     * @Title:bytes2HexString
     * @Description:字节数组转16进制字符串
     * @param b
     *            字节数组
     * @return 16进制字符串
     * @throws
     */
    public static String bytes2HexString(byte[] b) {
        StringBuffer result = new StringBuffer();
        String hex;
        for (int i = 0; i < b.length; i++) {
            hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            result.append(hex.toUpperCase());
        }
        return result.toString();
    }


    /**
     * @Title:string2HexString
     * @Description:字符串转16进制字符串
     * @param strPart
     *            字符串
     * @return 16进制字符串
     * @throws
     */
    public static String string2HexString(String strPart) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < strPart.length(); i++) {
            int ch = (int) strPart.charAt(i);
            String strHex = Integer.toHexString(ch);
            hexString.append(strHex);
        }
        return hexString.toString();
    }

    /**
     * @Title:intToHexString
     * @Description:10进制数字转成16进制
     * @param a 转化数据
     * @param len 占用字节数
     * @return
     * @throws
     */
    private static String intToHexString(int a,int len){
        len<<=1;
        String hexString = Integer.toHexString(a);
        int b = len -hexString.length();
        if(b>0){
            for(int i=0;i<b;i++)  {
                hexString = "0" + hexString;
            }
        }
        return hexString;
    }

    /**
     * @Title:char2Byte
     * @Description:字符转成字节数据char-->integer-->byte
     * @param src
     * @return
     * @throws
     */
    public static Byte char2Byte(Character src) {
        return Integer.valueOf((int)src).byteValue();
    }

    /**
     * @Title:hexString2String
     * @Description:16进制字符串转字符串
     * @param src
     *            16进制字符串
     * @return 字节数组
     * @throws
     */
    public static String hexString2String(String src) {
        String temp = "";
        for (int i = 0; i < src.length() / 2; i++) {
            temp = temp
                    + (char) Integer.valueOf(src.substring(i * 2, i * 2 + 2),
                    16).byteValue();
        }
        return temp;
    }

    /**
     * 十六进制两两相加
     * @param hexString 十六进制字符串
     * @param index 位数
     * @return
     */
    public static String sumHexStringBy2(String hexString,int index){
        int length = hexString.length();
        long result = 0;
        for(int i=0;i<length;){
            String subIn = hexString.substring(i, i+=index);
            long x = Long.parseLong(subIn, 16);
            result+=x;
        }
        String sum = Long.toHexString(result);
        return sum;
    }


    /**
     * 十进制转16进制字符串
     * @param decimal
     * @return
     */
    public static String decimalToHex(int decimal) {
        String hex = "";
        while(decimal != 0) {
            int hexValue = decimal % 16;
            hex = toHexChar(hexValue) + hex;
            decimal = decimal / 16;
        }
        return  hex;
    }

    //将0~15的十进制数转换成0~F的十六进制数
    public static char toHexChar(int hexValue) {
        if(hexValue <= 9 && hexValue >= 0)
            return (char)(hexValue + '0');
        else
            return (char)(hexValue - 10 + 'A');
    }

    /**
     * 16进制字符串转成10进制数值
     * @param hex
     * @return
     */
    public static int hexToDecimal(String hex)
    {
        int decimalValue=0;
        for(int i=0;i<hex.length();i++)
        {
            char hexChar=hex.charAt(i);
            decimalValue=decimalValue*16+hexCharToDecimal(hexChar);
        }
        return decimalValue;
    }

    public static int hexCharToDecimal(char hexChar)
    {
        if(hexChar>='A'&&hexChar<='F')
            return 10+hexChar-'A';
        else
            return hexChar-'0';//切记不能写成int类型的0，因为字符'0'转换为int时值为48
    }

    /**
     * 得到十六进制数的静态方法
     * @param decimalNumber 十进制数
     * @return 四位十六进制数字符串
     */
    public static String getHexString4(int decimalNumber) {
        //将十进制数转为十六进制数
        String hex = Integer.toHexString(decimalNumber);
        //转为大写
        hex = hex.toUpperCase();
        //加长到四位字符，用0补齐
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 得到十六进制数的静态方法
     * @param decimalNumber 十进制数
     * @return 两位十六进制数字符串
     */
    public static String getHexString2(int decimalNumber) {

        String  hex = Integer.toHexString(decimalNumber);

        hex = hex.toUpperCase();

        while (hex.length() < 2){
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 得到十六进制数的静态方法
     * @param decimalNumber 十进制数
     * @param i
     * @return
     */
    public static String getHexStringI(int decimalNumber,int i){
        String  hex = Integer.toHexString(decimalNumber);

        hex = hex.toUpperCase();

        while (hex.length() < i){
            hex = "0" + hex;
        }
        return hex;
    }

    /**
     * 向右补零
     * @param barcode
     * @return
     */
    public static String rightToZero(String barcode){
        if (barcode.length() < 32){
            int i =0;
            int j = 32 - barcode.length();
            String val = String.valueOf(j);
            String str = String.format("%0"+ val+"d",i);
            String newBarcode = barcode + str;
            return newBarcode;
        }
        return null;
    }

}
