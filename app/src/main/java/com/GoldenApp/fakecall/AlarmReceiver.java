package com.GoldenApp.fakecall;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public void onReceive(Context arg0, Intent arg1) {
        WakeLocker.acquire(arg0);
        Intent i = new Intent();
        i.setClassName(arg0, "com.GoldenApp.fakecall.CallActivity");
        i.setFlags(268435456);
        arg0.startActivity(i);
    }
}
