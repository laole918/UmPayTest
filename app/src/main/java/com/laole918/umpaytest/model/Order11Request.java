package com.laole918.umpaytest.model;

/**
 * Created by laole918 on 2016/3/29 0029.
 */
public class Order11Request {
    private String o_req_type;
    private String imei;
    private String iccid;
    private String imsi;

    public String getO_req_type() {
        return o_req_type;
    }

    public void setO_req_type(String o_req_type) {
        this.o_req_type = o_req_type;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }
}
