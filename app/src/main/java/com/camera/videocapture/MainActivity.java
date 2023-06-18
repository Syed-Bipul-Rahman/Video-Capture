package com.camera.videocapture;

import static android.Manifest.permission.CAMERA;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {
    //declaration variables

    VideoView videoView;
    Button button;
    private static final int REQUEST_VIDEO_CAPUTRE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoView = findViewById(R.id.videoView);
        button = findViewById(R.id.button);

        //action
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check for permission
                if (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent takeVideo = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    if (takeVideo.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(takeVideo, REQUEST_VIDEO_CAPUTRE);
                    } else {
                        //requst for camera
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{CAMERA}, 1);
                    }
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIDEO_CAPUTRE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri videoUri = getIntent().getData();
                videoView.setVideoURI(videoUri);
                videoView.setMediaController(new MediaController(this));
                videoView.requestFocus();
                videoView.start();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    Intent cameraIntent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                    startActivityForResult(cameraIntent,REQUEST_VIDEO_CAPUTRE);
                }else{
                    Toast.makeText(this, "You cannot capture video without permission", Toast.LENGTH_SHORT).show();
                }
        }
    }
}