package com.GoldenApp.fakecall;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class CustomDialog extends Dialog implements OnClickListener {
    private Button add;
    public int buttonClick = -1;
    public Context c;
    private Button choose;
    private int id;
    private Drawable img;
    private Button remove;
    SharedPreferences sharedPref;

    CustomDialog(Context context, int id) {
        super(context);
        this.c = context;
        this.id = id;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.custom_dialog);
        setCanceledOnTouchOutside(false);
        sharedPref = c.getSharedPreferences("file", 0);
        add = (Button) findViewById(R.id.btn_add);
        remove = (Button) findViewById(R.id.btn_remove);
        choose = (Button) findViewById(R.id.btn_choose);
        String path;
        if (id == 1) {
            path = sharedPref.getString("audio", "");
            if (path.equals("")) {
                remove.setVisibility(View.GONE);
            } else {
                remove.setText(path.substring(path.lastIndexOf("/") + 1));
            }
            choose.setText("Record");
            img = getContext().getResources().getDrawable(R.drawable.ic_record_voice_over_black_24dp);
            img.setBounds(0, 0, 60, 60);
            choose.setCompoundDrawables(img, null, null, null);
        } else if (id == 2) {
            choose.setVisibility(View.GONE);
            path = sharedPref.getString("ring", "");
            if (path.equals("")) {
                add.setText("Select Ringtone");
                img = getContext().getResources().getDrawable(R.drawable.ic_audiotrack_black_24dp);
                img.setBounds(0, 0, 60, 60);
                add.setCompoundDrawables(img, null, null, null);
            } else {
                add.setText(path.substring(path.lastIndexOf("/") + 1));
                img = getContext().getResources().getDrawable(R.drawable.ic_check_box_black_24dp);
                img.setBounds(0, 0, 60, 60);
                add.setCompoundDrawables(img, null, null, null);
            }
            remove.setText("Default");
            img = getContext().getResources().getDrawable(R.drawable.ic_refresh_black_24dp);
            img.setBounds(0, 0, 60, 60);
            remove.setCompoundDrawables(img, null, null, null);
        }
        add.setOnClickListener(this);
        remove.setOnClickListener(this);
        choose.setOnClickListener(this);
    }

    public void onBackPressed() {
        super.onBackPressed();
        buttonClick = -1;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add :
                buttonClick = 0;
                break;
            case R.id.btn_remove :
                buttonClick = 1;
                break;
            case R.id.btn_choose :
                buttonClick = 2;
                break;
        }
        dismiss();
    }
}
