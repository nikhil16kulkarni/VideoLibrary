package com.example.videolibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

public class Player extends AppCompatActivity {


    ProgressBar spinner; // It'll be displayed till the video is not loaded, just to let others know that something is loading
    ImageView FullScreen;
    FrameLayout frameLayout;
    ImageView SmallScreen;
    VideoView videoPlayer;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // To enable the back button in the activity

        spinner = findViewById(R.id.progressBarStyle);
        FullScreen = findViewById(R.id.fullScreen);
        frameLayout = findViewById(R.id.frameLayout);
        SmallScreen = findViewById(R.id.smallScreen);

        // Get data which was inserted in VideoAdapter
        Intent i = getIntent();
        Bundle data = i.getExtras();
        Video v = (Video) data.getSerializable("videoData");

        getSupportActionBar().setTitle(v.getTitle()); // Change the Text in the Title Bar after the video is clicked

        TextView title = findViewById(R.id.videoTitle);
        TextView desc = findViewById(R.id.videoDesc);
        videoPlayer = findViewById(R.id.videoView);

        title.setText(v.getTitle());
        desc.setText(v.getDescription());

        Uri videoUrl = Uri.parse(v.getVideoUrl());// Get The URL of the Video
        videoPlayer.setVideoURI(videoUrl);

        // Controllers for video
        MediaController mc = new MediaController(this);
        videoPlayer.setMediaController(mc);

        // Wait till the Video is completely Loaded
        videoPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                videoPlayer.start();
                spinner.setVisibility(View.GONE);
            }
        });

        //ImageView FullScreen = findViewById(R.id.fullScreen);

        FullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // After clicking on the full screen button
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


                // Hide the Action Bar
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().hide();


                FullScreen.setVisibility(View.GONE);
                SmallScreen.setVisibility(View.VISIBLE);

                // Change the height & width of FrameLayout to Match Parent
                frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));
                videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)));


                /*Intent i = new Intent(MainActivity2.class);
                startActivity(i);*/


            }
        });

        SmallScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // After clicking on the full screen exit button

                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                // Show the Action Bar
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().show();

                FullScreen.setVisibility(View.VISIBLE);
                SmallScreen.setVisibility(View.GONE);


                int heightValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());

                frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightValue)));
                videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightValue)));


            }
        });



    }



    // To make the back button work
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Show the Action Bar
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().show();

        FullScreen.setVisibility(View.VISIBLE);
        SmallScreen.setVisibility(View.GONE);


        int heightValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 220, getResources().getDisplayMetrics());

        frameLayout.setLayoutParams(new ConstraintLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightValue)));
        videoPlayer.setLayoutParams(new FrameLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, heightValue)));

        // Now there is 1 more problem, if we click on the back button in the PlayerActivity, it's not working as we have defined another function for onBackPressed just above from here. So for that, we'll check the orientation of the screen. If the orientation is Landscape, we'll perform upper task & if the orientation is Portrait, we'll go back to the MainActivity.
        int orientation = getResources().getConfiguration().orientation;

        if(orientation == Configuration.ORIENTATION_PORTRAIT){
            super.onBackPressed();
        }


    }
}