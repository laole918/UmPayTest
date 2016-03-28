package com.laole918.umpaytest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.laole918.umpaytest.databinding.ActivityMainBinding;
import com.laole918.umpaytest.handlers.MainActivityEventHandler;
import com.laole918.umpaytest.model.DeviceInfo;
import com.laole918.utils.DeviceUtils;

import rx.Observable;
import rx.Subscriber;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setHandler(new MainActivityEventHandler(this, binding));
        bindData();
    }

    private void bindData() {
        Observable.create(new Observable.OnSubscribe<DeviceInfo>() {
            @Override
            public void call(Subscriber<? super DeviceInfo> subscriber) {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setImei(DeviceUtils.getDeviceId(MainActivity.this));
                deviceInfo.setMsisdn(DeviceUtils.getLine1Number(MainActivity.this));
                deviceInfo.setIccid(DeviceUtils.getSimSerialNumber(MainActivity.this));
                deviceInfo.setImsi(DeviceUtils.getSubscriberId(MainActivity.this));
                subscriber.onNext(deviceInfo);
            }
        }).subscribe(deviceInfo -> {
            binding.setDeviceInfo(deviceInfo);
        });
    }
}
