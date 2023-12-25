package com.example.biboo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ProgressBar;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        progressBar = findViewById(R.id.splashProgressBar);
        startDelayedSplashProgress();
    }

    public void SplashProgress() {
        handler = new Handler(Looper.getMainLooper());
        int newProgress = progressBar.getProgress() + 1;

        if (newProgress <= progressBar.getMax()) {
            progressBar.setProgress(newProgress);

            // Adjust the delay based on the current progress value
            int delay;

            if (newProgress >= 50) {
                delay = 5; // Set the delay for progress 50 and beyond
            } else {
                delay = 50; // Default delay for progress less than 50
            }

            handler.postDelayed(this::SplashProgress, delay);
        }

        if (newProgress == progressBar.getMax()) {
            Intent intent = new Intent(SplashActivity.this, Login_activity.class);
            startActivity(intent);
            finish();
        }
    }

    public void startDelayedSplashProgress() {
        handler = new Handler(Looper.getMainLooper());

        // Set an initial delay before starting the progress
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the progress updates after the initial delay
                SplashProgress();
            }
        }, 2000); // Delay for 2000 milliseconds (adjust as needed)
    }
}