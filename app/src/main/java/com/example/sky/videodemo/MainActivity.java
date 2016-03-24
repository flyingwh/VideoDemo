package com.example.sky.videodemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VideoFragment mVideoFragment;
    private RecyclerView mRecyclerView;

    private List<String> mUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mUrls.add("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov");
        mUrls.add("http://www.sample-videos.com/video/flv/240/big_buck_bunny_240p_50mb.flv");
        mUrls.add("http://www.cybertechmedia.com/samples/carmelhill.ram");
        mUrls.add("http://techslides.com/demos/sample-videos/small.mp4");
        mUrls.add("http://www.cybertechmedia.com/samples/taylors.ram");
        mUrls.add("http://www.cybertechmedia.com/samples/pahio.ram");
//        mUrls.add();




        mVideoFragment = (VideoFragment)getSupportFragmentManager().findFragmentById(R.id.fragment_video);
//        mVideoFragment.play(mUrls.get(0));

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(new VideoAdapter(mUrls, mVideoFragment));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
