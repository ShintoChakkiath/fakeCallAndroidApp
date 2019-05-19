package com.GoldenApp.fakecall;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RecordDialog extends Dialog implements OnClickListener {
    LinearLayout buttomLayout;
    public Context c;
    private Button cancel;
    private Drawable img;
    private MediaPlayer mPlayer = null;
    private MediaRecorder mRecorder = null;
    public String path;
    private Button play;
    boolean playStarted = false;
    private Button record;
    boolean recordStarted = false;
    private Button save;
    SharedPreferences sharedPref;

    RecordDialog(Context context) {
        super(context);
        this.c = context;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.recording_dialog);
        setCanceledOnTouchOutside(false);
        buttomLayout = (LinearLayout) findViewById(R.id.bottum_layout);
        buttomLayout.setVisibility(8);
        sharedPref = c.getSharedPreferences("file", 0);
        play = (Button) findViewById(R.id.btn_play);
        record = (Button) findViewById(R.id.btn_record);
        cancel = (Button) findViewById(R.id.cancel);
        save = (Button) findViewById(R.id.save);
        play.setOnClickListener(this);
        record.setOnClickListener(this);
        cancel.setOnClickListener(this);
        save.setOnClickListener(this);
        play.setClickable(false);
    }

    private void onRecord(boolean started) {
        if (started) {
            stopRecording();
        } else {
            startRecording();
        }
    }

    private void startRecording() {
        recordStarted = true;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(1);
        mRecorder.setOutputFormat(1);
        mRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.3gp");
        mRecorder.setAudioEncoder(1);
        try {
            mRecorder.prepare();
            mRecorder.start();
            record.setText("recording");
        } catch (IOException e) {
            Log.e("APP", "prepare() failed");
        }
    }

    private void stopRecording() {
        recordStarted = false;
        try {
            if (mRecorder != null) {
                mRecorder.release();
            }
        } catch (Exception e) {
        }
        record.setText("record");
    }

    private void onPlay(boolean started) {
        if (started) {
            stopPlaying();
            Drawable d = c.getResources().getDrawable(R.drawable.ic_play_circle_filled_black_24dp);
            d.setBounds(0, 0, 100, 100);
            play.setCompoundDrawables(d, null, null, null);
            return;
        }
        startPlaying();
        Drawable d = c.getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp);
        d.setBounds(0, 0, 100, 100);
        play.setCompoundDrawables(d, null, null, null);
    }

    private void startPlaying() {
        playStarted = true;
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.3gp");
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e("APP", "prepare() failed");
        }
        mPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                RecordDialog.this.onPlay(true);
            }
        });
    }

    private void stopPlaying() {
        playStarted = false;
        try {
            if (mPlayer.isPlaying()) {
                mPlayer.stop();
                mPlayer.release();
            }
        } catch (Exception e) {
        }
    }

    public void onBackPressed() {
        onPlay(true);
        onRecord(true);
        Toast.makeText(c, "back", 0).show();
        super.onBackPressed();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_record :
                buttomLayout.setVisibility(View.VISIBLE);
                if (playStarted) {
                    onPlay(playStarted);
                }
                onRecord(recordStarted);
                play.setClickable(true);
                return;
            case R.id.btn_play :
                if (recordStarted) {
                    onRecord(recordStarted);
                }
                onPlay(playStarted);
                return;
            case R.id.cancel :
                onPlay(true);
                onRecord(true);
                dismiss();
                return;
            case R.id.save :
                onPlay(true);
                onRecord(true);
                copyFile();
                dismiss();
                return;
            default:
                dismiss();
                return;
        }
    }

    private void copyFile() {
        FileNotFoundException fnfe1;
        InputStream inputStream;
        Exception e;
        OutputStream outputStream;
        try {
            OutputStream out = null;
            File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/callervoice.3gp");
            InputStream in = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.3gp");
            try {
                out = new FileOutputStream(dir);
            } catch (FileNotFoundException e2) {
                fnfe1 = e2;
                inputStream = in;
                Log.e("tag", fnfe1.getMessage());
            } catch (Exception e3) {
                e = e3;
                inputStream = in;
                Log.e("tag", e.getMessage());
            }
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = in.read(buffer);
                    if (read != -1) {
                        out.write(buffer, 0, read);
                    } else {
                        in.close();
                        try {
                            out.flush();
                            out.close();
                            new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/temp.3gp").delete();
                            Editor editor = sharedPref.edit();
                            editor.putString("audio", Environment.getExternalStorageDirectory().getAbsolutePath() + "/callervoice.3gp");
                            editor.apply();
                            Toast.makeText(c, "Recorded Audio set to caller Voice", 0).show();
                            return;
                        } catch (FileNotFoundException e4) {
                            fnfe1 = e4;
                            outputStream = out;
                            Log.e("tag", fnfe1.getMessage());
                        } catch (Exception e5) {
                            e = e5;
                            outputStream = out;
                            Log.e("tag", e.getMessage());
                        }
                    }
                }
            } catch (FileNotFoundException e6) {
                fnfe1 = e6;
                outputStream = out;
                inputStream = in;
                Log.e("tag", fnfe1.getMessage());
            } catch (Exception e7) {
                e = e7;
                outputStream = out;
                inputStream = in;
                Log.e("tag", e.getMessage());
            }
        } catch (FileNotFoundException e8) {
            fnfe1 = e8;
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e9) {
            e = e9;
            Log.e("tag", e.getMessage());
        }
    }
}
