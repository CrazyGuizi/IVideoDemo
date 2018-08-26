package com.example.crazygz.ivideodemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] request = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.INTERNET,
        Manifest.permission.CHANGE_WIFI_STATE};
        for (int i = 0; i < request.length; i++) {
            if (ContextCompat.checkSelfPermission(this,
                    request[i]) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{request[i]}, i + 1);
            }
        }
    }

    public void click(View v) {
        startActivity(new Intent(this, VideoActivity.class));
    }

    public void click2(View v) {
        startActivity(new Intent(this, IVideoActivity.class));
    }
}
