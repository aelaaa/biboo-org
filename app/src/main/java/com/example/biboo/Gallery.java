package com.example.biboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

public class Gallery extends AppCompatActivity {

    CardView land, aerial, aquatic, prehistoric;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());

        // Initialization of various cardview
        land = findViewById(R.id.cv_land_animals);
        aerial = findViewById(R.id.cv_aerial_animals);
        aquatic = findViewById(R.id.cv_aquatic_animals);
        prehistoric = findViewById(R.id.cv_prehistoric_animals);
        back = findViewById(R.id.btn_gallery_back);


        //listener to start the next activity
        //passing the classification that will be used for population of data in recycler view
        land.setOnClickListener(v-> {
            Intent intent = new Intent(Gallery.this, GalleryV2.class);
            intent.putExtra("classification", "Land");
            startActivity(intent);

        });

        aerial.setOnClickListener(v-> {
            Intent intent = new Intent(Gallery.this, GalleryV2.class);
            intent.putExtra("classification", "Aerial");
            startActivity(intent);
        });

        aquatic.setOnClickListener(v-> {
            Intent intent = new Intent(Gallery.this, GalleryV2.class);
            intent.putExtra("classification", "Aquatic");
            startActivity(intent);
        });

        prehistoric.setOnClickListener(v-> {
            Intent intent = new Intent(Gallery.this, GalleryV2.class);
            intent.putExtra("classification", "Prehistoric");
            startActivity(intent);
        });

        back.setOnClickListener(v-> {
            Intent intent = new Intent(Gallery.this, Homepage_activity.class);
            startActivity(intent);
            finish();
        });


    }
}