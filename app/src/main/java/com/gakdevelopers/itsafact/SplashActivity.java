package com.gakdevelopers.itsafact;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.VideoView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;

    VideoView videoView;
    MediaPlayer mMediaPLayer;
    int mCurrentVideoPosition;

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        videoView = (VideoView) findViewById(R.id.videoView);

        imageView = (ImageView) findViewById(R.id.imageView);
        //imgSpeaker = (ImageView) findViewById(R.id.imgSpeaker);

        YoYo.with(Techniques.Wobble).duration(2100).repeat(1).playOn(findViewById(R.id.imgSpeaker));
        YoYo.with(Techniques.Flash).duration(2100).repeat(1).playOn(findViewById(R.id.relativeDoYouKnow));

        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.video);

        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPLayer = mediaPlayer;
                mMediaPLayer.setLooping(true);
                if (mCurrentVideoPosition != 0) {
                    mMediaPLayer.seekTo(mCurrentVideoPosition);
                    mMediaPLayer.start();
                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i=new Intent(SplashActivity.this,
                        MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_SCREEN_TIME_OUT);

    }
}