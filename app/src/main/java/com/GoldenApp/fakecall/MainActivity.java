package com.GoldenApp.fakecall;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Images;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_READ_PERMISSION = 786;
    int ON_CHAR_CLICK = 2;
    int ON_CLICK;
    int ON_DIALOG_CLICK = 0;
    int ON_MORE_CLICK = 3;
    int ON_SHADLE_CLICK = 1;
    ImageView callerImage;
    CustomDialog customDialog;
    int dialogId;
    EditText nameEditText;
    EditText phoneEditText;
    int picker;
    SharedPreferences sharedPref;
    private AdView mAdView;
    AdRequest adRequestint;

    protected void onResume() {
        super.onResume();
        setCaller();
    }

    void setCaller() {
        String name = sharedPref.getString("name", "");
        String phone = sharedPref.getString("number", "");
        String image = sharedPref.getString("image", "");
        nameEditText.setText(name);
        phoneEditText.setText(phone);
        int obj = -1;
        switch (image.hashCode()) {
            case 0:
                if (image.equals("")) {
                    obj = 0;
                    break;
                }
                break;
            case 48:
                if (image.equals("0")) {
                    obj = 1;
                    break;
                }
                break;
            case 49:
                if (image.equals("1")) {
                    obj = 2;
                    break;
                }
                break;
            case 50:
                if (image.equals("2")) {
                    obj = 3;
                    break;
                }
                break;
            case 51:
                if (image.equals("3")) {
                    obj = 4;
                    break;
                }
                break;
            case 52:
                if (image.equals("4")) {
                    obj = 5;
                    break;
                }
                break;
        }
        switch (obj) {
            case 0:
                callerImage.setImageResource(R.drawable.person_add_grey);
                return;
            case 1:
                callerImage.setImageResource(R.drawable.gallery_btn_0);
                return;
            case 2:
                callerImage.setImageResource(R.drawable.gallery_btn_1);
                return;
            case 3:
                callerImage.setImageResource(R.drawable.gallery_btn_2);
                return;
            case 4:
                callerImage.setImageResource(R.drawable.gallery_btn_3);
                return;
            case 5:
                callerImage.setImageResource(R.drawable.gallery_btn_4);
                return;
            default:
                callerImage.setImageDrawable(Drawable.createFromPath(image));
//                Glide.with(getApplicationContext()).load(image).into(callerImage);
        }
    }

    public void rateUs() {
        final SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", 0);
        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert);
        alert.setTitle("Rate Us:");
        alert.setMessage(getString(R.string.rate_dialog_message));
        alert.setPositiveButton(getString(R.string.rate_dialog_ok), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                try {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                sharedpreferences.edit().putBoolean("rate", true).apply();
                dialog.dismiss();
                finish();
            }
        });
        alert.setNegativeButton(getString(R.string.rate_dialog_no), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                sharedpreferences.edit().putBoolean("rate", true).apply();
            }
        });
        alert.setNeutralButton(getString(R.string.rate_dialog_cancel), new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
                sharedpreferences.edit().putBoolean("remind", true).apply();
            }
        });
        alert.create();
        alert.show();
    }

    public void onBackPressed() {
        SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", 0);
        boolean alreadyRated = sharedpreferences.getBoolean("rate", false);
        boolean remindMeLater = sharedpreferences.getBoolean("remind", false);
        if (!alreadyRated) {
            rateUs();
        } else if (alreadyRated && remindMeLater) {
            rateUs();
        } else {
            super.onBackPressed();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        nameEditText = (EditText) findViewById(R.id.caller_name);
        phoneEditText = (EditText) findViewById(R.id.caller_number);
        callerImage = (ImageView) findViewById(R.id.caller_image);
        sharedPref = getSharedPreferences("file", 0);
        //ADS
        mAdView = (AdView) findViewById(R.id.banner_AdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        if (ON_CLICK == ON_DIALOG_CLICK) {
            if(customDialog != null){
                customDialog.show();
                customDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialog) {
                        onDialogDismiss(customDialog.buttonClick, dialogId);
                    }
                });
            }
        } else if (ON_CLICK == ON_SHADLE_CLICK) {
            startActivity(new Intent(MainActivity.this, ScheduleActivity.class));
            finish();
        } else if (ON_CLICK == ON_MORE_CLICK) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id="+getResources().getString(R.string.developer_id)));
            startActivity(intent);
            finish();
        } else if (ON_CLICK == ON_CHAR_CLICK) {
            startActivityForResult(new Intent(MainActivity.this, CharacterActivity.class), 1);
        }
        nameEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!sharedPref.getString("name", "").equals(nameEditText.getText().toString())) {
                    saveName(nameEditText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        phoneEditText.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                if (!sharedPref.getString("number", "").equals(phoneEditText.getText().toString())) {
                    savePhone(phoneEditText.getText().toString());
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });
        setCaller();
    }

    private void saveName(String name) {
        Editor editor = sharedPref.edit();
        editor.putString("name", name);
        editor.apply();
    }

    private void savePhone(String phone) {
        Editor editor = sharedPref.edit();
        editor.putString("number", phone);
        editor.apply();
    }

    private void saveImg(String img) {
        Editor editor = sharedPref.edit();
        editor.putString("image", img);
        editor.apply();
    }

    void setDialog(CustomDialog c, int dialogId) {
        this.customDialog = c;
        this.dialogId = dialogId;
    }

    public void ringtoneClick(View v) {
        final CustomDialog cdd = new CustomDialog(this, 2);
        setDialog(cdd, 2);
        ON_CLICK = ON_DIALOG_CLICK;
        cdd.show();
        cdd.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                onDialogDismiss(cdd.buttonClick, 2);
            }
        });
    }

    public void scheduleClick(View v) {
        ON_CLICK = ON_SHADLE_CLICK;
        startActivity(new Intent(this, ScheduleActivity.class));
        finish();
    }

    public void moreAppsClick(View v) {
        ON_CLICK = ON_MORE_CLICK;
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setData(Uri.parse("https://play.google.com/store/apps/dev?id="+getResources().getString(R.string.developer_id)));
        startActivity(intent);
    }

    public void characterClick(View view) {
        ON_CLICK = ON_CHAR_CLICK;
        startActivityForResult(new Intent(this, CharacterActivity.class), 1);
    }

    public void upLoadClick(View view) {
        final CustomDialog cdd = new CustomDialog(this, 0);
        setDialog(cdd, 0);
        ON_CLICK = ON_DIALOG_CLICK;
        cdd.show();
        cdd.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                onDialogDismiss(cdd.buttonClick, 0);
            }
        });
    }

    public void soundClick(View view) {
        final CustomDialog cdd = new CustomDialog(this, 1);
        setDialog(cdd, 1);
        ON_CLICK = ON_DIALOG_CLICK;
        cdd.show();
        cdd.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                onDialogDismiss(cdd.buttonClick, 1);
            }
        });
    }

    public void onDialogDismiss(int button, int id) {
        Editor editor;
        if (id == 1) {
            switch (button) {
                case 0:
                    picker = 0;
                    requestPermission();
                    return;
                case 1:
                    editor = sharedPref.edit();
                    editor.putString("audio", "");
                    editor.apply();
                    if (!sharedPref.getString("audio", "").equals("")) {
                        return;
                    }
                    return;
                case 2:
                    picker = 3;
                    requestPermission();
                    return;
                default:
            }
        } else if (id == 0) {
            switch (button) {
                case 0:
                    picker = 1;
                    requestPermission();
                    return;
                case 1:
                    saveImg("");
                    callerImage.setImageResource(R.drawable.person);
                    return;
                case 2:
                    startActivityForResult(new Intent(this, CharacterActivity.class), 1);
                    return;
                default:
            }
        } else if (id == 2) {
            editor = sharedPref.edit();
            switch (button) {
                case 0:
                    picker = 2;
                    requestPermission();
                    return;
                case 1:
                    editor.putString("ring", "");
                    editor.commit();
                    return;
                default:
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                String name = data.getStringExtra("name");
                String number = data.getStringExtra("number");
                String img = data.getStringExtra("img");
                saveName(name);
                saveImg(img);
                savePhone(number);
            }
        } else if (requestCode == 2) {
            if (resultCode == -1) {
                String audio = getRealPathFromURI(data.getData());
                Editor editor = sharedPref.edit();
                editor.putString("audio", audio);
                editor.apply();
                if (!sharedPref.getString("audio", "").equals("")) {
                }
            }
        } else if (requestCode == 3) {
            if (resultCode == -1) {
                performCrop(data.getData());
            }
        } else if (requestCode == 4) {
            if (resultCode == -1) {
                String ring = getRealPathFromURI(data.getData());
                Editor editor = sharedPref.edit();
                editor.putString("ring", ring);
                editor.apply();
            }
        } else if (requestCode == 5 && resultCode == -1) {
            saveImg(Environment.getExternalStorageDirectory() + "/Image-Caller.jpg");
        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = new CursorLoader(getApplicationContext(), contentUri, new String[]{"_data"}, null, null, null).loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow("_data");
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_PERMISSION && grantResults[0] == 0) {
            pick();
        }
        if (requestCode == 766 && grantResults[0] == 0) {
            RecordDialog recordDialog = new RecordDialog(this);
            recordDialog.show();
            recordDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialog) {
                    if (!sharedPref.getString("audio", "").equals("")) {
                    }
                }
            });
        }
        Toast.makeText(this, "Permission to Access Storage:" + isExternalStorageWritable(), Toast.LENGTH_LONG).show();
    }

    public boolean isExternalStorageWritable() {
        if ("mounted".equals(Environment.getExternalStorageState())) {
            return true;
        }
        return false;
    }

    void pick() {
        if (picker == 0) {
            pickAudio();
        } else if (picker == 1) {
            pickImage();
        } else if (picker == 2) {
            pickRing();
        } else {
            requestPermissionMIC();
        }
    }

    void pickRing() {
        Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 4);
        } else {
            Toast.makeText(this, "No app found!", Toast.LENGTH_LONG).show();

        }
    }

    void pickAudio() {
        Intent intent = new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 2);
        } else {
            Toast.makeText(this, "No app found!", Toast.LENGTH_LONG).show();
        }
    }

    void pickImage() {
        Intent intent = new Intent("android.intent.action.PICK", Images.Media.EXTERNAL_CONTENT_URI);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 3);
        } else {
            Toast.makeText(this, "No app found!", Toast.LENGTH_LONG).show();
        }
    }

    private void requestPermission() {
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, REQUEST_READ_PERMISSION);
            return;
        }
        pick();
    }

    private void requestPermissionMIC() {
        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{"android.permission.RECORD_AUDIO"}, 766);
            return;
        }
        RecordDialog recordDialog = new RecordDialog(this);
        recordDialog.show();
        recordDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                if (!sharedPref.getString("audio", "").equals("")) {
                }
            }
        });
    }

    private void performCrop(Uri picUri) {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(picUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            cropIntent.putExtra("outputX", 800);
            cropIntent.putExtra("outputY", 800);
            File f = new File(Environment.getExternalStorageDirectory(), "/Image-Caller.jpg");
            try {
                f.createNewFile();
            } catch (IOException ex) {
                Log.e("io", ex.getMessage());
            }
            cropIntent.putExtra("output", Uri.fromFile(f));
            startActivityForResult(cropIntent, 5);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Whoops - your device doesn't support the crop action!", Toast.LENGTH_LONG).show();
        }
    }
}