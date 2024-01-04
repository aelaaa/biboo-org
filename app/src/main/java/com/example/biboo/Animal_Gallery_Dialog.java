package com.example.biboo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Animal_Gallery_Dialog extends DialogFragment {

    ArrayList<Animal_Model> animalData;
    private final String animalName;

    public Animal_Gallery_Dialog(ArrayList<Animal_Model> animalData, Context context, String animal_name) {
        this.animalData = animalData;
        this.animalName = animal_name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.single_animal_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(false);
        // data iniialization for the dialogFragment prompt
        ImageView animal = view.findViewById(R.id.iv_single_animal);
        Button btn_return = view.findViewById(R.id.btn_return);
        TextView txtAnimalName = view.findViewById(R.id.txt_single_animal_name);
        TextView txtScientificName = view.findViewById(R.id.txt_single_scientific_name);
        TextView txtClassification = view.findViewById(R.id.txt_single_classification);
        TextView txtHabitat = view.findViewById(R.id.txt_single_habitat);
        TextView txtDiet = view.findViewById(R.id.txt_single_diet);
        TextView txtDescription = view.findViewById(R.id.txt_single_description);


        // Finding the clicked animal in the list based on the animalName
        Animal_Model clickedAnimal = findAnimalByName(animalName);

        //condition if clicked animal is null
        if (clickedAnimal != null) {
            // Set the details of the clicked animal in the TextViews
            txtAnimalName.setText(clickedAnimal.getAnimal_Name());
            txtScientificName.setText("Scientific Name: " + clickedAnimal.getScientificName());
            txtClassification.setText("Classification: " +clickedAnimal.getClassification());
            txtHabitat.setText("Habitat: " + clickedAnimal.getHabitat());
            txtDiet.setText("Diet: " + clickedAnimal.getDiet());
            txtDescription.setText(clickedAnimal.getDescription());

            //getting the resourceId for loading of image
            int resourceId = view.getResources().getIdentifier(
                    clickedAnimal.getImageSource(), "drawable",
                    view.getContext().getPackageName()
            );
            // Loading the image into the ImageView using Glide
            Glide.with(view)
                    .load(resourceId)
                    .into(animal);

        }
        // button listener for dismissing the dialogFragmentPrompt
        btn_return.setOnClickListener(v-> {
                dismiss();
        });
    }

    // method to find an animal by name in the animalData list
    private Animal_Model findAnimalByName(String animalName) {
        for (Animal_Model animal : animalData) {
            if (animal.getAnimal_Name().equals(animalName)) {
                return animal;
            }
        }
        return null;
    }
}

