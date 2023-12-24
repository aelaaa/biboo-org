package com.example.biboo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class GalleryV2 extends AppCompatActivity implements Animal_Data_Adapter.OnButtonClickListener{
    ArrayList<Animal_Model> animalData= new ArrayList<Animal_Model>();
    Bundle bundle;
    String classification;
    TextView txt_gallery_classification, txt_description;
    RecyclerView recyclerView;
    Button btn_backToGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_v2);
        //data initialization
        txt_gallery_classification = findViewById(R.id.txt_galleryClassificiation);
        txt_description = findViewById(R.id.txt_galleryDescription);
        btn_backToGallery = findViewById(R.id.btn_galleryV2_back);
        recyclerView = findViewById(R.id.reView_gallery);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        //receiving the intent from previous activity
        bundle = new Bundle();
        bundle = getIntent().getExtras();
        if (bundle != null) {
            classification = bundle.getString("classification");
        }

        if (classification.equals("Land")){
            txt_gallery_classification.setText(R.string.land);
            txt_description.setText(R.string.land_animals);
        }
        if (classification.equals("Aquatic")){
            txt_gallery_classification.setText(R.string.aquatic);
            txt_description.setText(R.string.aquatic_animals);
        }
        if (classification.equals("Aerial")){
            txt_gallery_classification.setText(R.string.aerial);
            txt_description.setText(R.string.aerial_animals);
        }
        if (classification.equals("Prehistoric")){
            txt_gallery_classification.setText(R.string.prehistoric);
            txt_description.setText(R.string.prehistoric_animals);
        }
        // setting the gallery by using the classification received
        setGallery(classification);

        // listener used in returning to previous activity (Gallery)
        btn_backToGallery.setOnClickListener(v-> {
            Intent intentBack = new Intent(getApplicationContext(), Gallery.class);
            intentBack.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentBack);
            overridePendingTransition(R.anim.fade_out, 0);
            finish();
        });




    }

    public void setGallery(String animal_class) {
        // getting all animal from the Animal_Database using cursor
        Cursor cursor = new Animal_Database(getApplicationContext()).getAllAnimal(animal_class);

        //getting data from columns using cursor and inserting it on animalData
        while (cursor.moveToNext()) {
            Animal_Model model = new Animal_Model(cursor.getInt(0),cursor.getString(1), cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6), cursor.getString(8));
            animalData.add(model);
        }
        //using the constructed adapter to set the data for recycler view
        Animal_Data_Adapter adapter = new Animal_Data_Adapter(animalData, this);
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onButtonClick(String animal_name) {
        // Find the clicked animal in the list
        Animal_Model clickedAnimal = findAnimalByName(animal_name);

        // Check if the animal is found
        if (clickedAnimal != null) {
            // Create and show the dialog with the details of the clicked animal
            showAnimalGalleryDialog(clickedAnimal);
        }
    }

    // Helper method to find an animal by name in the animalData list
    private Animal_Model findAnimalByName(String animalName) {
        for (Animal_Model animal : animalData) {
            if (animal.getAnimal_Name().equals(animalName)) {
                return animal;
            }
        }
        return null;
    }

    // Helper method to show the Animal_Gallery_Dialog
    private void showAnimalGalleryDialog(Animal_Model animal) {
        Animal_Gallery_Dialog dialog = new Animal_Gallery_Dialog(
                animalData, // Pass the list of animals
                this,
                animal.getAnimal_Name());
        dialog.show(getSupportFragmentManager(), "animal_gallery_dialog");
    }



}
