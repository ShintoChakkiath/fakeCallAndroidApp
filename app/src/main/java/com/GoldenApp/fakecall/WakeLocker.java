package com.GoldenApp.fakecall;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public abstract class WakeLocker {
    private static WakeLock wakeLock;

    public static void acquire(Context ctx) {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = ((PowerManager) ctx.getSystemService("power")).newWakeLock(PowerManager.FULL_WAKE_LOCK, "com.GoldenApp.fakecall");
        ((KeyguardManager) ctx.getSystemService("keyguard")).newKeyguardLock("com.GoldenApp.fakecall").disableKeyguard();
        wakeLock.acquire();
    }

    public static void release() {
        if (wakeLock != null) {
            wakeLock.release();
        }
        wakeLock = null;
    }
}