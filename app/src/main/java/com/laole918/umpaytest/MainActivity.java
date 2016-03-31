package com.laole918.umpaytest;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.laole918.umpaytest.databinding.ActivityMainBinding;
import com.laole918.umpaytest.model.Order;
import com.laole918.umpaytest.viewmodel.MainViewModel;
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
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.setViewModel(new MainViewModel(this, binding));
        bindDeviceInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        binding.adView.resume();
    }

    @Override
    protected void onPause() {
        binding.adView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        binding.adView.destroy();
        super.onDestroy();
    }

    private void bindDeviceInfo() {
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
            Order order = new Order();
            order.setIccid(deviceInfo.getIccid());
            order.setImsi(deviceInfo.getImsi());
            order.setImei(deviceInfo.getImei());
            binding.getViewModel().order.set(order);
        });
    }
}
