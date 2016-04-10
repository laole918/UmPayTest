package com.laole918.umpaytest.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.laole918.umpaytest.BR;

/**
 * Created by laole918 on 2016/3/31 0031.
 */
public class Order extends BaseObservable {

    private String imei;
    private String iccid;
    private String imsi;
    private String mobile;
    private String verifycode;
    private String return_str;

    private String o_id;
    private String o_req_type;

    @Bindable
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
        notifyPropertyChanged(BR.imei);
    }

    @Bindable
    public String getIccid() {
        return iccid;
    }

    public void setIccid(String iccid) {
        this.iccid = iccid;
        notifyPropertyChanged(BR.iccid);
    }

    @Bindable
    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
        notifyPropertyChanged(BR.imsi);
    }

    @Bindable
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
        notifyPropertyChanged(BR.mobile);
    }

    @Bindable
    public String getVerifycode() {
        return verifycode;
    }

    public void setVerifycode(String verifycode) {
        this.verifycode = verifycode;
        notifyPropertyChanged(BR.verifycode);
    }

    @Bindable
    public String getReturn_str() {
        return return_str;
    }

    public void setReturn_str(String return_str) {
        this.return_str = return_str;
        notifyPropertyChanged(BR.return_str);
    }

    public String getO_req_type() {
        return o_req_type;
    }

    public String getO_id() {
        return o_id;
    }

    public void setO_id(String o_id) {
        this.o_id = o_id;
    }

    public void setO_req_type(String o_req_type) {
        this.o_req_type = o_req_type;
    }
}
