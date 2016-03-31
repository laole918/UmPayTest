package com.laole918.umpaytest.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.laole918.lib.SimpleTextWatcher;
import com.laole918.umpaytest.R;
import com.laole918.umpaytest.api.TestClient;
import com.laole918.umpaytest.databinding.ActivityMainBinding;
import com.laole918.umpaytest.model.DeviceInfo;
import com.laole918.umpaytest.model.Order;
import com.laole918.utils.JSONUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by laole918 on 2016/3/28 0028.
 */
public class MainViewModel {

    private Context mContext;
    private ProgressDialog dialog;
    private ActivityMainBinding mBinding;

    public MainViewModel(Context context, ActivityMainBinding binding) {
        mContext = context;
        mBinding = binding;
        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setMessage(mContext.getString(R.string.txt_uploading));
    }

    public final ObservableField<Order> order = new ObservableField<>();

//    public final SimpleTextWatcher mobileWatcher = new SimpleTextWatcher() {
//        @Override
//        public void afterTextChanged(Editable s) {
//            order.get().setMobile(String.valueOf(s));
//        }
//    };

    public void onClickShare(View view) {
        DeviceInfo deviceInfo = new DeviceInfo();
        deviceInfo.setImei(order.get().getImei());
        deviceInfo.setMsisdn(order.get().getMobile());
        deviceInfo.setImsi(order.get().getImsi());
        deviceInfo.setIccid(order.get().getIccid());
        if (TextUtils.isEmpty(deviceInfo.getIccid())
                && TextUtils.isEmpty(deviceInfo.getImei())
                && TextUtils.isEmpty(deviceInfo.getImsi())) {
            showMessage(R.string.txt_empty);
        } else {
            shareText(R.string.btn_txt_share, JSONUtils.toJSONString(deviceInfo));
        }
    }

    public void onClickShareResponse(View view) {
        String return_str = order.get().getReturn_str();
        if (TextUtils.isEmpty(return_str)) {
            shareText(R.string.btn_txt_share_response, return_str);
        } else {
            showMessage(R.string.txt_empty);
        }
    }

    private void shareText(int titleId, String text) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(Intent.createChooser(intent, mContext.getString(titleId)));
    }

    public void onClickUpload(View view) {
        dialog.show();
        Order order = new Order();
        order.setO_req_type("11");
        order.setImei(this.order.get().getImei());
        order.setImsi(this.order.get().getImsi());
        order.setIccid(this.order.get().getIccid());
        Observable<Order> observable = TestClient.getTestApiInstance().order11(order);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onResponse, e -> {
                    onError();
                });
    }

    public void onClickGetCode(View view) {
        Order order = new Order();
        order.setO_req_type("12");
        order.setMobile(this.order.get().getMobile());
        Observable<Order> observable = TestClient.getTestApiInstance().order11(order);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(order1 -> {
                    System.out.println("order1:id:" + order1.getO_id());
                }, e -> {
                    onError();
                });
    }

    public void onClickVerifyCode(View view) {

    }

    private void onResponse(Order order) {
        this.order.get().setReturn_str(order.getReturn_str());
        dialog.dismiss();
        showMessage(R.string.txt_upload_complete);
    }

    private void onError() {
        dialog.dismiss();
        showMessage(R.string.txt_net_error);
    }

    private void showMessage(int messageId) {
        new AlertDialog.Builder(mContext).setMessage(messageId).show();
    }
}
