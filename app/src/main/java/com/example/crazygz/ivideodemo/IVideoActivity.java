package com.example.crazygz.ivideodemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.media.MediaPlayerManager;
import com.example.media.view.VideoView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;

public class IVideoActivity extends AppCompatActivity {

    private static String DEFAULT_PATH = Environment.getExternalStorageDirectory() + "/123.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivideo);

        VideoView videoView = findViewById(R.id.video_view);
        Button button = findViewById(R.id.btn_play);
        EditText editText = findViewById(R.id.et_url);
        videoView.setDataSource(DEFAULT_PATH);
        button.setOnClickListener((v) -> {
            String path = editText.getText().toString();
            MediaPlayerManager.getInstance().changeDataSource(TextUtils.isEmpty(path) ? DEFAULT_PATH : path);
            Toast.makeText(this, "" +(String) videoView.getDataSource(), Toast.LENGTH_SHORT).show();
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.getInstance().pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.getInstance().release();
    }
}
