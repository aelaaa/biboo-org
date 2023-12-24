package com.example.biboo;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

public class Animal_Data_Adapter extends RecyclerView.Adapter<Animal_Data_Adapter.ViewData> {

    private ArrayList<Animal_Model> data_form;
    private OnButtonClickListener buttonClickListener;


    public Animal_Data_Adapter(ArrayList<Animal_Model> data_Form, OnButtonClickListener listener) {
        this.data_form = data_Form;
        this.buttonClickListener = listener;
    }

    @NonNull
    @Override
    public ViewData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.galleryview_animal_layout, parent, false);
        return new ViewData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewData data, int position) {

        //setting the name of animal in recycler view
        data.animal_name.setText(data_form.get(position).getAnimal_Name());
        // storing the image file name
        //getImageSource() returns the resource name
        String imageName = data_form.get(position).getImageSource();

        //getting resource identifier by its name
        int resourceId = data.animal.getContext().getResources().getIdentifier(
                imageName,
                "drawable", data.animal.getContext().getPackageName()
        );
        //loading the image using the obtained resourceId to glide
        Glide.with(data.animal.getContext())
                .load(resourceId)
                .apply(new RequestOptions().override(80, 80))
                .into(data.animal);

        //setting a onClicklistener per recycler item in recycler view
        data.linearLayout.setOnClickListener(view -> {
           if (buttonClickListener != null) {
                    buttonClickListener.onButtonClick(data_form.get(position).getAnimal_Name());

            }
        });
    }


    //getting the item count in arraylist
    @Override
    public int getItemCount() {
        return data_form.size();
    }


    public class ViewData extends RecyclerView.ViewHolder {
        TextView animal_name;
        ImageView animal;
        LinearLayoutCompat linearLayout;

        public ViewData(View itemView) {
            super(itemView);
            //Initializing the data for recycler view
            linearLayout = itemView.findViewById(R.id.Ll_animal);
            animal = itemView.findViewById(R.id.iv_animal);
            animal_name = itemView.findViewById(R.id.txt_animalName);

        }
    }

    // listener for every clicked item in recycler view
    public interface OnButtonClickListener {
        void onButtonClick(String animal_name);

    }
}
