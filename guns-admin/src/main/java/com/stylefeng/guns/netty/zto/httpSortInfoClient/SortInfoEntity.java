package com.stylefeng.guns.netty.zto.httpSortInfoClient;

public class SortInfoEntity {

    private String billCode;    //运单编号
    private String pipeline;    //分拣线编码
    private Integer turnNumber; //扫描次数
    private long requestTime;   //扫描时间
    private float weight;   //重量
    private String incomeClerk; //收件员业务编码
    private String scanMan; //扫描人编码
    private String trayCode;  //小车编号
    private String infocamareCode; //请求相机编号
    private String supplyCode;  //供件台编号

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getPipeline() {
        return pipeline;
    }

    public void setPipeline(String pipeline) {
        this.pipeline = pipeline;
    }

    public Integer getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(Integer turnNumber) {
        this.turnNumber = turnNumber;
    }

    public long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getIncomeClerk() {
        return incomeClerk;
    }

    public void setIncomeClerk(String incomeClerk) {
        this.incomeClerk = incomeClerk;
    }

    public String getScanMan() {
        return scanMan;
    }

    public void setScanMan(String scanMan) {
        this.scanMan = scanMan;
    }

    public String getTrayCode() {
        return trayCode;
    }

    public void setTrayCode(String trayCode) {
        this.trayCode = trayCode;
    }

    public String getInfocamareCode() {
        return infocamareCode;
    }

    public void setInfocamareCode(String infocamareCode) {
        this.infocamareCode = infocamareCode;
    }

    public String getSupplyCode() {
        return supplyCode;
    }

    public void setSupplyCode(String supplyCode) {
        this.supplyCode = supplyCode;
    }

    @Override
    public String toString() {
        return "SortInfoEntity{" +
                "billCode='" + billCode + '\'' +
                ", pipeline='" + pipeline + '\'' +
                ", turnNumber=" + turnNumber +
                ", requestTime=" + requestTime +
                ", weight=" + weight +
                ", incomeClerk='" + incomeClerk + '\'' +
                ", scanMan='" + scanMan + '\'' +
                ", trayCode='" + trayCode + '\'' +
                ", infocamareCode='" + infocamareCode + '\'' +
                ", supplyCode='" + supplyCode + '\'' +
                '}';
    }


}
