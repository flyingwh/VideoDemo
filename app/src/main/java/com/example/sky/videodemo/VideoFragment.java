package com.example.sky.videodemo;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcUtil;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaList;

public class VideoFragment extends Fragment implements SurfaceHolder.Callback, IVideoPlayer{

    private static final String TAG = VideoFragment.class.getSimpleName();

    private static final int MSG_SIZE_CHANGED = 11;


    public static boolean sIsFullScreen;

    private ViewGroup mProgressVG;
    private TextView mCurrentPosTV;
    private TextView mTotalLengthTV;
    private SeekBar mSeekBar;
    private ImageView mFullScreenIV;
    private ProgressBar mProgressBar;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;

    private LibVLC mLibvlc;
    private int mVideoWidth;
    private int mVideoHeight;

    private String mUrl ;

    private long mTotalLength;
    private int mProgress;
    private float mLastPosition;

    private static Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        mSurfaceView = (SurfaceView) view.findViewById(R.id.surface);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mFullScreenIV = (ImageView) view.findViewById(R.id.iv_full_screen);
        mCurrentPosTV = (TextView) view.findViewById(R.id.tv_current_pos);
        mTotalLengthTV = (TextView) view.findViewById(R.id.tv_total_length);
        mSeekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        mProgressVG = (ViewGroup) view.findViewById(R.id.vg_progress);
        mHandler = new MyHandler();


        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                mProgress = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mLibvlc != null && mLibvlc.isPlaying()){
                    mLibvlc.setPosition(mProgress/100f);
                }
            }
        });
        if (!sIsFullScreen){
            mFullScreenIV.setVisibility(View.VISIBLE);
            mFullScreenIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mLibvlc != null && !TextUtils.isEmpty(mUrl)){
                        VideoActivity.startActivity(getContext(), mUrl, mLibvlc.getPosition());
                    }
                }
            });
        }


        return view;
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mLibvlc != null && mLibvlc.isPlaying()){
            mLastPosition = mLibvlc.getPosition();
            mLibvlc.pause();
        }
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(mUrl)){
//            play(mUrl, mLastPosition);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    public void play(String url, float position){
        mLastPosition = position;
        play(url);
    }

    public void play(String url){
        if (!TextUtils.isEmpty(url) ){
            showProgressBar();
            releasePlayer();
            mUrl = url;
            if (mUrl.startsWith("rtsp") || mUrl.startsWith("RTSP")){
                mProgressVG.setVisibility(View.GONE);
            }else {
                mProgressVG.setVisibility(View.VISIBLE);
            }
            createPlayer();
            mLibvlc.attachSurface(mSurfaceHolder.getSurface(), this);
        }

    }


    private void showProgressBar(){
        mProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideProgressBar(){
        if (mProgressBar.getVisibility() == View.VISIBLE){
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void createPlayer(){
        try {
            // Create a new media player
            mLibvlc = LibVLC.getInstance();
            mSurfaceHolder = mSurfaceView.getHolder();
            mLibvlc.setHardwareAcceleration(LibVLC.HW_ACCELERATION_DISABLED);
            mLibvlc.setSubtitlesEncoding("");
            mLibvlc.setAout(LibVLC.AOUT_OPENSLES);
            mLibvlc.setTimeStretching(true);
            mLibvlc.setVerboseMode(true);
//            mLibvlc.setNetworkCaching(500);
            if (LibVlcUtil.isGingerbreadOrLater())
                mLibvlc.setVout(LibVLC.VOUT_ANDROID_WINDOW);
            else
                mLibvlc.setVout(LibVLC.VOUT_ANDROID_SURFACE);
            LibVLC.restart(getContext());
            EventHandler.getInstance().addHandler(mHandler);
            mSurfaceHolder.setKeepScreenOn(true);
            MediaList list = mLibvlc.getMediaList();
            list.clear();
            if (mUrl.startsWith("http")){
                mLibvlc.playMRL(mUrl);
            }else {
                list.add(new Media(mLibvlc, LibVLC.PathToURI(mUrl)), false);
                mLibvlc.playIndex(0);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error creating player!",
                    Toast.LENGTH_LONG).show();
        }
    }

    private void releasePlayer() {
        Log.i(TAG, "release player");
        if (mLibvlc == null)
            return;
        EventHandler.getInstance().removeHandler(mHandler);
        mLibvlc.stop();
        mLibvlc.detachSurface();
        mSurfaceHolder = null;
        mLibvlc = null;
    }

    public void setSize(int width, int height) {
        mVideoWidth = width;
        mVideoHeight = height;
        if (mVideoWidth * mVideoHeight <= 1)
            return;

        if (mSurfaceHolder == null || mSurfaceView == null)
            return;

        // get screen size
        int w = getActivity().getWindow().getDecorView().getWidth();
        int h = getActivity().getWindow().getDecorView().getHeight();

        // getWindow().getDecorView() doesn't always take orientation into
        // account, we have to correct the values
        boolean isPortrait = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        if (w > h && isPortrait || w < h && !isPortrait) {
            int i = w;
            w = h;
            h = i;
        }

        float videoAR = (float) mVideoWidth / (float) mVideoHeight;
        float screenAR = (float) w / (float) h;

        if (screenAR < videoAR)
            h = (int) (w / videoAR);
        else
            w = (int) (h * videoAR);

        // force surface buffer size
        mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);

        // set display size
        ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
        lp.width = w;
        lp.height = h ;
        mSurfaceView.setLayoutParams(lp);
        mSurfaceView.invalidate();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mSurfaceHolder = surfaceHolder;
        if (mLibvlc != null) {
            mLibvlc.attachSurface(surfaceHolder.getSurface(), this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void setSurfaceLayout(int width, int height, int visible_width, int visible_height, int sar_num, int sar_den) {
        Message msg = Message.obtain(mHandler, MSG_SIZE_CHANGED, width, height);
        msg.sendToTarget();
    }

    @Override
    public int configureSurface(Surface surface, int width, int height, int hal) {
        if (LibVlcUtil.isICSOrLater() || surface == null)
            return -1;
        if (width * height == 0)
            return 0;
        if (hal != 0)
            mSurfaceHolder.setFormat(hal);
        mSurfaceHolder.setFixedSize(width, height);
        return 1;
    }

    @Override
    public void eventHardwareAccelerationError() {

    }

    private String getTime(long time){
        int min = (int) (time / 1000 / 60);
        int sec = (int) (time / 1000 % 60);
        return String.format("%02d:%02d", min, sec);
    }

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {

            // SamplePlayer events
            if (msg.what == MSG_SIZE_CHANGED) {
                setSize(msg.arg1, msg.arg2);
                return;
            }

            // Libvlc events
            Bundle b = msg.getData();
            switch (b.getInt("event")) {
                case EventHandler.MediaPlayerTimeChanged:
                    hideProgressBar();

                    break;
                case EventHandler.MediaPlayerPositionChanged:
                    if (mProgressVG.getVisibility() == View.VISIBLE){
                        mSeekBar.setProgress((int) (mLibvlc.getPosition() * 100));
                        mCurrentPosTV.setText(getTime(mLibvlc.getTime()));
                    }
                    break;
                case EventHandler.MediaPlayerPlaying:
                    mTotalLength = mLibvlc.getLength();
                    mTotalLengthTV.setText(getTime(mTotalLength));
                    mLibvlc.setPosition(mLastPosition);
                    break;
                case EventHandler.MediaPlayerBuffering:
                    showProgressBar();
                    break;
                case EventHandler.MediaPlayerEncounteredError:
                    Toast.makeText(getContext(), "error", Toast.LENGTH_LONG).show();
                    hideProgressBar();
                    break;
            }
        }
    }
}
