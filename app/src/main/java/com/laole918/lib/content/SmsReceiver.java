package com.laole918.lib.content;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.hwangjr.rxbus.RxBus;

/**
 * Created by laole918 on 2016/4/3.
 */
public class SmsReceiver extends BroadcastReceiver {

    public static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (SMS_ACTION.equals(action)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                if (messages.length > 0) {
                    String msgBody = messages[0].getMessageBody();
                    String msgAddress = messages[0].getOriginatingAddress();
                    RxBus.get().post("code", msgBody);
                }
            }
        }
    }
}
