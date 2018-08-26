package com.example.crazygz.ivideodemo;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.media.MediaPlayerManager;
import com.example.media.view.VideoView;

public class IVideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivideo);

        VideoView videoView = findViewById(R.id.video_view);
        videoView.setDataSource(Environment.getExternalStorageDirectory() + "/123.mp4");


    }

    @Override
    protected void onPause() {
        super.onPause();
        MediaPlayerManager.getInstance().pause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //MediaPlayerManager.getInstance().backgroundToForeground();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MediaPlayerManager.getInstance().release();
    }
}
