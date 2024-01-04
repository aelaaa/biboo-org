package com.example.biboo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class viewscore extends AppCompatActivity {
    private Button back;
    private TextView animafication_Score, animafication_bestScore, animadiet_Score, animadiet_bestScore;
    private User_Database user_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewscore);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());
        initializeViews();
        user_database = new User_Database(this);
        ScoreDisplay();

        back.setOnClickListener(v-> {
            Intent intent = new Intent(viewscore.this, Homepage_activity.class);
            startActivity(intent);
            finish();
        });

    }

    private void initializeViews(){
        animafication_Score = findViewById(R.id.txtRecentScore);
        animafication_bestScore = findViewById(R.id.txtBestScore);
        animadiet_Score = findViewById(R.id.txtAnimadietRecentScore);
        animadiet_bestScore = findViewById(R.id.txtAnimadietBestScore);
        back = findViewById(R.id.btn_score_back);
    }

    private void ScoreDisplay(){
        int animaFicationScore = user_database.getScore(getSavedUsernameFromPreferences(), "animafication_score");
        int animaFicationBestScore = user_database.getScore(getSavedUsernameFromPreferences(), "animafication_bestscore");
        int animaDietScore = user_database.getScore(getSavedUsernameFromPreferences(), "animadiet_score");
        int animaDietBestScore = user_database.getScore(getSavedUsernameFromPreferences(), "animadiet_bestscore");

        animafication_Score.setText(String.format("%s pts.", animaFicationScore));
        animafication_bestScore.setText(String.format("%s pts.", animaFicationBestScore));
        animadiet_Score.setText(String.format("%s pts.", animaDietScore));
        animadiet_bestScore.setText(String.format("%s pts.", animaDietBestScore));
    }

    private String getSavedUsernameFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getString("username", "");
    }
}