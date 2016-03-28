package com.laole918.umpaytest.handlers;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.laole918.umpaytest.R;
import com.laole918.umpaytest.databinding.ActivityMainBinding;
import com.laole918.umpaytest.model.DeviceInfo;
import com.laole918.utils.JSONUtils;

/**
 * Created by laole918 on 2016/3/28 0028.
 */
public class MainActivityEventHandler {

    private Context mContext;
    private ActivityMainBinding mBinding;

    public MainActivityEventHandler(Context context,ActivityMainBinding binding) {
        mContext = context;
        mBinding = binding;
    }

    public void onClickShare(View view) {
        DeviceInfo deviceInfo = mBinding.getDeviceInfo();
        if(deviceInfo != null) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, JSONUtils.toJSONString(deviceInfo));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(Intent.createChooser(intent, mContext.getString(R.string.share_title)));
        }
    }
    public void onClickUpload(View view) {

    }
}
