package com.example.biboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;

public class AnimaGames extends AppCompatActivity {

    Button back;
    CardView animafication, animadescript, animadiet;
    Intent intent;
    private Prompt_Dialogs promptDialogs;
    private Vibrator vibrator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anima_games);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());
        promptDialogs = new Prompt_Dialogs(this);

        back = findViewById(R.id.btn_animagames_back);
        animafication = findViewById(R.id.cv_animaFication);
        animadiet = findViewById(R.id.cv_animaDiet);
        animadescript = findViewById(R.id.cv_animaDescript);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);


        back.setOnClickListener(v-> {
            intent = new Intent(AnimaGames.this, Homepage_activity.class);
            startActivity(intent);
            finish();
        });

        animafication.setOnClickListener(v-> {
            intent = new Intent(AnimaGames.this, Animafication.class);
            startActivity(intent);
            finish();
        });

        animadiet.setOnClickListener(v-> {
            intent = new Intent(AnimaGames.this, Anima_Diet.class);
            startActivity(intent);
            finish();
        });

        animadescript.setOnClickListener(v-> {
            vibrate();
            promptDialogs.statusDialogs("NOTICE!", "This anima game mode is not yet available! Thank you.");
            promptDialogs.show();
        });

    }
    private void vibrate() {
        // Check if the device has a vibrator
        if (vibrator.hasVibrator()) {
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
        }
    }
}