package com.example.sky.videodemo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        String url = getIntent().getStringExtra("url");
        float position = getIntent().getFloatExtra("position", 0);

        VideoFragment videoFragment = (VideoFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_video);

        videoFragment.play(url, position);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoFragment.sIsFullScreen = false;
    }

    public static void startActivity(Context context, String url, float position){
        VideoFragment.sIsFullScreen = true;
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }
}
