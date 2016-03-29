package com.laole918.umpaytest.handlers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.laole918.umpaytest.R;
import com.laole918.umpaytest.api.TestClient;
import com.laole918.umpaytest.databinding.ActivityMainBinding;
import com.laole918.umpaytest.model.DeviceInfo;
import com.laole918.umpaytest.model.Order11Request;
import com.laole918.umpaytest.model.Order11Response;
import com.laole918.utils.JSONUtils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by laole918 on 2016/3/28 0028.
 */
public class MainActivityEventHandler {

    private Context mContext;
    private ProgressDialog dialog;
    private ActivityMainBinding mBinding;

    public MainActivityEventHandler(Context context, ActivityMainBinding binding) {
        mContext = context;
        mBinding = binding;
        dialog = new ProgressDialog(mContext);
        dialog.setCancelable(false);
        dialog.setMessage(mContext.getString(R.string.txt_uploading));
    }

    public void onClickShare(View view) {
        DeviceInfo deviceInfo = mBinding.getDeviceInfo();
        if (deviceInfo != null) {
            shareText(R.string.btn_txt_share, JSONUtils.toJSONString(deviceInfo));
        } else {
            showMessage(R.string.txt_empty);
        }
    }

    public void onClickShareResponse(View view) {
        Order11Response response = mBinding.getResponse();
        if (response != null) {
            shareText(R.string.btn_txt_share_response, JSONUtils.toJSONString(response));
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
        DeviceInfo deviceInfo = mBinding.getDeviceInfo();
        if (deviceInfo != null) {
            dialog.show();
            Order11Request request = new Order11Request();
            request.setO_req_type("11");
            request.setImei(deviceInfo.getImei());
            request.setImsi(deviceInfo.getImsi());
            request.setIccid(deviceInfo.getIccid());
            Observable<Order11Response> observable = TestClient.getTestApiInstance().order11(request);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::onResponse, e -> {
                        onError();
                    });
        }
    }

    private void onResponse(Order11Response response) {
        mBinding.setResponse(response);
        dialog.dismiss();
    }

    private void onError() {
        dialog.dismiss();
        showMessage(R.string.txt_net_error);
    }

    private void showMessage(int messageId) {
        new AlertDialog.Builder(mContext).setMessage(messageId).show();
    }
}
