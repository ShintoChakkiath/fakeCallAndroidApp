package com.GoldenApp.fakecall;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class ScheduleActivity extends AppCompatActivity {
    private PendingIntent alarmIntent;
    private AlarmManager alarmMgr;
    private AdView mAdView;
    AdRequest adRequestint;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        //ADS
        mAdView = (AdView) findViewById(R.id.banner_AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        alarmMgr = (AlarmManager) getSystemService("alarm");
        alarmIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), 0);
    }

    public void onButtonClick(View view) {
        int timer = 0;
        String time = "0";
        switch (view.getId()) {
            case R.id._now:
                time = "0 Sec";
                timer = 0;
                break;
            case R.id._10s:
                timer = 10;
                time = "10 Sec";
                break;
            case R.id._30s:
                time = "30 Sec";
                timer = 30;
                break;
            case R.id._01m:
                time = "1 Min";
                timer = 60;
                break;
            case R.id._5m:
                time = "5 Min";
                timer = 300;
                break;
            case R.id._15m:
                time = "15 Min";
                timer = 900;
                break;
            case R.id._30m:
                time = "30 Min";
                break;
        }
        alarmMgr.set(2, SystemClock.elapsedRealtime() + ((long) (timer * 1000)), alarmIntent);
        Toast.makeText(this, "Call timer Set to: " + time, Toast.LENGTH_LONG).show();
        finish();
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
