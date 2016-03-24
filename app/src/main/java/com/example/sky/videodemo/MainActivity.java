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
        mUrls.add("http://111.206.109.51/youku/69725B70B7531827DD44E02109/030008110056F19FEF2D1D2BEEFCF9CED724C8-F199-5E66-4B4B-51D2187C3D70.mp4?&start=0");
        mUrls.add("http://111.206.109.141/youku/67760DC8FE44783A22C7B92B90/0300080F0056C765A6910D05CF07DD39076B4C-7A3F-1204-FF28-DD41E5A75930.mp4?&start=0");
        mUrls.add("http://111.206.106.182/youku/677117D8E253D831D400CE59E8/030008120054BE4988F3090014D61B5BDBE117-A72C-758E-0672-6157662632AE.mp4?&start=0");
        mUrls.add("http://111.206.106.179/youku/6775BBB4F7F3A82F56334C429E/0300081000529CA73AEC1605CF07DDE4224B7E-6B2A-024A-A59F-53DC501652E8.mp4?&start=0");
        mUrls.add("http://111.206.109.135/youku/6573A764DCE4783A22C6054BD6/0300080A0056F35C1AEAEC2BEEFCF944FCE3F8-DC2D-A555-E9D6-C85DB07E3E71.mp4?&start=0");
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
