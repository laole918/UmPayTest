package com.laole918.umpaytest;

import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.hwangjr.rxbus.RxBus;
import com.laole918.lib.content.SmsReceiver;
import com.laole918.umpaytest.databinding.ActivityMainBinding;
import com.laole918.umpaytest.model.Order;
import com.laole918.umpaytest.viewmodel.MainViewModel;
import com.laole918.umpaytest.model.DeviceInfo;
import com.laole918.utils.DeviceUtils;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends AppCompatActivity {

    private CompositeSubscription mSubscriptions;
    private ActivityMainBinding mBinding;
    private MainViewModel viewModel;
    private SmsReceiver mSmsReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long l1 = System.currentTimeMillis();
        mSubscriptions = new CompositeSubscription();
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        AdRequest adRequest = new AdRequest.Builder().build();
        mBinding.adView.loadAd(adRequest);
        viewModel = new MainViewModel(this, mSubscriptions);
        mBinding.setViewModel(viewModel);
        RxBus.get().register(viewModel);

        mSmsReceiver = new SmsReceiver();
        IntentFilter intentFilter = new IntentFilter(SmsReceiver.SMS_ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        registerReceiver(mSmsReceiver, intentFilter);
        bindDeviceInfo();
        long l2 = System.currentTimeMillis();
        System.out.println("注册耗时：" + (l2 - l1));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBinding.adView.resume();
    }

    @Override
    protected void onPause() {
        mBinding.adView.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mBinding.adView.destroy();
        mSubscriptions.unsubscribe();
        RxBus.get().unregister(viewModel);
        unregisterReceiver(mSmsReceiver);
        super.onDestroy();
    }

    private void bindDeviceInfo() {
        Subscription subscription = Observable.create(new Observable.OnSubscribe<DeviceInfo>() {
            @Override
            public void call(Subscriber<? super DeviceInfo> subscriber) {
                DeviceInfo deviceInfo = new DeviceInfo();
                deviceInfo.setImei(DeviceUtils.getDeviceId(MainActivity.this));
                deviceInfo.setMsisdn(DeviceUtils.getLine1Number(MainActivity.this));
                deviceInfo.setIccid(DeviceUtils.getSimSerialNumber(MainActivity.this));
                deviceInfo.setImsi(DeviceUtils.getSubscriberId(MainActivity.this));
                subscriber.onNext(deviceInfo);
                subscriber.onCompleted();
            }
        }).subscribe(deviceInfo -> {
            Order order = new Order();
            order.setIccid(deviceInfo.getIccid());
            order.setImsi(deviceInfo.getImsi());
            order.setImei(deviceInfo.getImei());
            mBinding.getViewModel().order.set(order);
        });
        mSubscriptions.add(subscription);
    }
}
