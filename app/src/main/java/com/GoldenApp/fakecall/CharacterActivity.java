package com.GoldenApp.fakecall;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class CharacterActivity extends AppCompatActivity {
    static final /* synthetic */ boolean $assertionsDisabled = (!CharacterActivity.class.desiredAssertionStatus());
    public static int[] prgmImages = new int[]{R.drawable.gallery_btn_0, R.drawable.gallery_btn_1, R.drawable.gallery_btn_2, R.drawable.gallery_btn_3, R.drawable.gallery_btn_4};
    public static String[] prgmNameList = new String[]{"Police", "Pizza", "Girl Friend", "MOM", "Santa Claus"};
    public static String[] prgmPhoneList = new String[]{"15", "03126688776", "03007865456", "0426754346", "0548755726"};
    CharAdapter adapter;
    ArrayList<Charactor> categroyList;
    Intent returnIntent;
    AdView mAdView;

    public void onBackPressed() {
        super.onBackPressed();
        setResult(0, returnIntent);
        finish();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);
        //ADS
        mAdView = (AdView) findViewById(R.id.banner_AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        categroyList = new ArrayList();
        ListView listView = (ListView) findViewById(R.id.list_view);
        for (int i = 0; i < prgmNameList.length; i++) {
            categroyList.add(new Charactor(prgmNameList[i], prgmPhoneList[i], prgmImages[i]));
        }
        adapter = new CharAdapter(this, categroyList);
        if ($assertionsDisabled || listView != null) {
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    characterClick(position);
                }
            });
            returnIntent = new Intent();
            return;
        }
        throw new AssertionError();
    }

    public void characterClick(int pos) {
        switch (pos) {
            case 0:
                returnIntent.putExtra("name", "Police");
                returnIntent.putExtra("number", "15");
                returnIntent.putExtra("img", "0");
                setResult(-1, returnIntent);
                finish();
                return;
            case 1:
                returnIntent.putExtra("name", "Pizza");
                returnIntent.putExtra("number", "03126688776");
                returnIntent.putExtra("img", "1");
                setResult(-1, returnIntent);
                finish();
                return;
            case 2:
                returnIntent.putExtra("name", "Girl Friend");
                returnIntent.putExtra("number", "03007865456");
                returnIntent.putExtra("img", "2");
                setResult(-1, returnIntent);
                finish();
                return;
            case 3:
                returnIntent.putExtra("name", "MOM");
                returnIntent.putExtra("number", "0426754346");
                returnIntent.putExtra("img", "3");
                setResult(-1, returnIntent);
                finish();
                return;
            case 4:
                returnIntent.putExtra("name", "Santa Claus");
                returnIntent.putExtra("number", "0548755726");
                returnIntent.putExtra("img", "4");
                setResult(-1, returnIntent);
                finish();
                return;
            default:
        }
    }
}