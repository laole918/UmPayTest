package com.laole918.umpaytest.viewmodel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;

import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.laole918.umpaytest.R;
import com.laole918.umpaytest.api.TestClient;
import com.laole918.umpaytest.model.DeviceInfo;
import com.laole918.umpaytest.model.Order;
import com.laole918.utils.JSONUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by laole918 on 2016/3/28 0028.
 */
public class MainViewModel {

    public final ObservableField<Order> order = new ObservableField<>();
    public final ObservableField<String> btn_get_txt = new ObservableField<>();
    public final ObservableBoolean btn_get_enabled = new ObservableBoolean();

    private CompositeSubscription mSubscriptions;
    private Context mContext;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog;

    public MainViewModel(Context context, CompositeSubscription subscriptions) {
        mSubscriptions = subscriptions;
        mContext = context;
        btn_get_txt.set("获取验证码");
        btn_get_enabled.set(true);
    }

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
        showProgress(R.string.txt_uploading);
        Order order = new Order();
        order.setO_req_type("11");
        order.setImei(this.order.get().getImei());
        order.setImsi(this.order.get().getImsi());
        order.setIccid(this.order.get().getIccid());
        Observable<Order> observable = TestClient.getTestApiInstance().order11(order);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onUploadResponse, e -> {
                    onError();
                });
        mSubscriptions.add(subscription);
    }

    public void onClickGetCode(View view) {
        if (TextUtils.isEmpty(this.order.get().getMobile())) {
            showMessage(R.string.et_mobile_hint);
            return;
        }
        Order order = new Order();
        order.setO_req_type("12");
        order.setMobile(this.order.get().getMobile());
        Observable<Order> observable = TestClient.getTestApiInstance().order11(order);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(order1 -> {
                    this.order.get().setO_id(order1.getO_id());
                }, e -> {
                    onError();
                });
        mSubscriptions.add(subscription);
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(60)
                .map(interval -> 59 - interval)
                .map(last -> {
                    if (last < 10) {
                        return "0" + String.valueOf(last);
                    } else {
                        return String.valueOf(last);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(last -> setBtnGetTxtAndEnable(last + view.getContext().getString(R.string.btn_get_txt2), false),
                        e -> setBtnGetTxtAndEnable(view.getContext().getString(R.string.btn_get_txt1), true),
                        () -> setBtnGetTxtAndEnable(view.getContext().getString(R.string.btn_get_txt1), true));
        mSubscriptions.add(subscription);
    }

    @Subscribe(thread = EventThread.MAIN_THREAD, tags = {@Tag("code")})
    public void onVerifyCodeReceived(String code) {
        order.get().setVerifycode(code);
    }

    private void setBtnGetTxtAndEnable(String txt, boolean enabled) {
        btn_get_enabled.set(enabled);
        btn_get_txt.set(txt);
    }

    public void onClickVerifyCode(View view) {
        if (TextUtils.isEmpty(this.order.get().getMobile())) {
            showMessage(R.string.et_mobile_hint);
            return;
        }
        if (TextUtils.isEmpty(this.order.get().getVerifycode()) && TextUtils.isEmpty(this.order.get().getO_id())) {
            showMessage(R.string.txt_get_verifycode);
            return;
        }
        if (TextUtils.isEmpty(this.order.get().getVerifycode())) {
            showMessage(R.string.et_hint_verifycode);
            return;
        }
        showProgress(R.string.txt_waiting);
        Order order = new Order();
        order.setO_req_type("13");
        order.setVerifycode(this.order.get().getVerifycode());
        order.setO_id(this.order.get().getO_id());
        Observable<Order> observable = TestClient.getTestApiInstance().order11(order);
        Subscription subscription = observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(order1 -> {
                    dismissProgress();
                    showMessage(R.string.txt_verify_complete);
                }, e -> {
                    onError();
                });
        mSubscriptions.add(subscription);
    }

    private void onUploadResponse(Order order) {
        this.order.get().setReturn_str(order.getReturn_str());
        dismissProgress();
        showMessage(R.string.txt_upload_complete);
    }

    private void onError() {
        dismissProgress();
        showMessage(R.string.txt_net_error);
    }

    private void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void showProgress(int messageId) {
        showProgress(mContext.getString(messageId));
    }

    private void showProgress(CharSequence message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(mContext);
            progressDialog.setCancelable(false);
        }
        if (message != null) {
            progressDialog.setMessage(message);
        }
        progressDialog.show();
    }

    private void showMessage(int messageId) {
        showMessage(mContext.getString(messageId));
    }

    private void showMessage(CharSequence message) {
        if (alertDialog == null) {
            alertDialog = new AlertDialog.Builder(mContext).create();
        }
        if (message != null) {
            alertDialog.setMessage(message);
        }
        alertDialog.show();
    }
}
