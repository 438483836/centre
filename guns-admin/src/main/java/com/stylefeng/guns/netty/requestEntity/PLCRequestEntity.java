package com.stylefeng.guns.netty.requestEntity;

/**
 * Created by Vincent on 2018-08-21.
 */
public class PLCRequestEntity {

    private String sortingId; //唯一ID
    private String billCode; //条码
    private String trayCode;  //小车编号
    private String chute; //格口号
    private String businessType; //业务类型


    public PLCRequestEntity(String sortingId,String billCode, String trayCode, String chute, String businessType) {
        this.sortingId = sortingId;
        this.billCode = billCode;
        this.trayCode = trayCode;
        this.chute = chute;
        this.businessType = businessType;
    }

    public PLCRequestEntity(String sortingId,String billCode, String trayCode, String chute) {
        this.sortingId = sortingId;
        this.billCode = billCode;
        this.trayCode = trayCode;
        this.chute = chute;
    }

    public PLCRequestEntity(){
        super();
    }

    public String getSortingId() {
        return sortingId;
    }

    public void setSortingId(String sortingId) {
        this.sortingId = sortingId;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }


    public String getChute() {
        return chute;
    }

    public void setChute(String chute) {
        this.chute = chute;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    @Override
    public String toString() {
        return "PLCRequsetEntity{" +
                "sortingId='" + sortingId + '\'' +
                ", trayCode='" + trayCode + '\'' +
                ", chute='" + chute + '\'' +
                ", bussinessType='" + businessType + '\'' +
                '}';
    }

}
