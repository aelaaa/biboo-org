package com.example.biboo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Anima_Descrip extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, Prompt_Dialogs.ResetGameActivity{

    private ImageView settings;
    private ImageButton option1, option2, option3, option4;
    private TextView animal_description;
    private int currentLevel = 0;
    private int sublevel = 0;
    private int currentAnimalIndex = 0;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private List<Integer> allAnimalId = new ArrayList<>();
    private List<Animal_Model> animalData = new ArrayList<>();
    private final int ANIMAL_LOADER_ID = 1;
    Prompt_Dialogs promptDialogs;

    private Animal_Database animalDatabase;
    private static final String CONTENT_PROVIDER_URI = "content://com.example.biboo.animalcontentprovider/animals/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anima_descrip);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());
        intializeViews();
        promptDialogs = new Prompt_Dialogs(getApplicationContext());
        animalDatabase = new Animal_Database(getApplicationContext());
        animalData = new ArrayList<>();
        AnimalSetting();
        animalUpdateDescript();


        settings.setOnClickListener(v -> {
            // Call the showSettingsDialog method when the settings icon is clicked
            promptDialogs.showSettingsDialog(getSupportFragmentManager());
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }

    private void intializeViews(){
        settings = findViewById(R.id.imgSettings);
        animal_description = findViewById(R.id.tvAnimaDescripQuestion);
        option1 = findViewById(R.id.btn_img_option1);
        option2 = findViewById(R.id.btn_img_option2);
        option3 = findViewById(R.id.btn_img_option3);
        option4 = findViewById(R.id.btn_img_option4);
        Button submit = findViewById(R.id.btnSubmitAnimaDescrip);

        option1.setOnClickListener(this::onOptionClick);
        option2.setOnClickListener(this::onOptionClick);
        option3.setOnClickListener(this::onOptionClick);
        option4.setOnClickListener(this::onOptionClick);
        submit.setOnClickListener(this::onSubmitClick);
    }

    public void AnimalSetting() {
        // Clear the list before adding new shuffled animal IDs
        allAnimalId.clear();

        // Add shuffled animal IDs for each difficulty level
        allAnimalId.addAll(getShuffledAnimalIdsInRange(1, 30));
        allAnimalId.addAll(getShuffledAnimalIdsInRange(31, 60));
        allAnimalId.addAll(getShuffledAnimalIdsInRange(61, 75));
        allAnimalId.addAll(getShuffledAnimalIdsInRange(76, 100));

        Log.d("Anima_Descrip", "AllAnimalId size: " + allAnimalId.size());
    }

    private void animalUpdateDescript() {
        List<Integer> animalIds = allAnimalId;

        if (!animalIds.isEmpty()) {
            int currentAnimalId = animalIds.get(currentAnimalIndex);

            Bundle queryBundle = new Bundle();
            queryBundle.putInt("animalId", currentAnimalId);
            Log.d("Anima_Descrip", "Updating animal description for animalId: " + currentAnimalId);

            getSupportLoaderManager().restartLoader(ANIMAL_LOADER_ID, queryBundle, this);
        }
    }
    @Override
    public void onResetGameActivity() {
        // Implement the logic to reset the activity
        // For example, reset currentLevel, sublevel, etc.
        currentLevel = 0;
        sublevel = 0;
        currentAnimalIndex = 0;
        AnimalSetting();
        animalUpdateDescript();
    }

    public void onOptionClick(View view) {
        ImageButton clickedButton = (ImageButton) view;
        checkAnswer(clickedButton);
    }

    public void onSubmitClick(View view) {
        // Optionally, you can perform some logic here before loading the next set of questions
        animalUpdateDescript();
    }

    private void checkAnswer(ImageButton clickedButton) {
        // Get the correct animal's ID from the current question
        int correctAnimalId = allAnimalId.get(currentAnimalIndex);
        // Get the animal ID associated with the clicked button
        int clickedButtonId = extractAnimalIdFromButton(clickedButton);

        // Check if the clicked option is correct
        if (correctAnimalId == clickedButtonId) {
            // Handle the correct answer logic (e.g., increment score)
            // Load the next set of questions
            animalUpdateDescript();
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the incorrect answer logic (e.g., show a message)
            Toast.makeText(this, "Incorrect. Try again!", Toast.LENGTH_SHORT).show();
        }


    }

    private int extractAnimalIdFromButton(ImageButton button) {
        // Extract the animal ID from the button's resource name
        String buttonResourceName = getResources().getResourceEntryName(button.getId());
        return Integer.parseInt(buttonResourceName.substring("btn_img_option".length()));
    }


    private void onLoadFinished(Animal_Model animal) {
        if (animal != null) {
            runOnUiThread(() -> {
                // Shuffle the animal image sources
                List<String> shuffledImageSources = getShuffledAnimalImageSources(animal);

                // Load shuffled images into the ImageButtons
                loadAnimalImageIntoButton(option1, shuffledImageSources.get(0));
                loadAnimalImageIntoButton(option2, shuffledImageSources.get(1));
                loadAnimalImageIntoButton(option3, shuffledImageSources.get(2));
                loadAnimalImageIntoButton(option4, shuffledImageSources.get(3));

                // Set the correct answer
                int correctAnswerIndex = shuffledImageSources.indexOf(getAnimalImageSource(animal));

                option1.setOnClickListener(view -> checkAnswer(correctAnswerIndex, 0));
                option2.setOnClickListener(view -> checkAnswer(correctAnswerIndex, 1));
                option3.setOnClickListener(view -> checkAnswer(correctAnswerIndex, 2));
                option4.setOnClickListener(view -> checkAnswer(correctAnswerIndex, 3));

                currentAnimalIndex = (currentAnimalIndex + 1) % allAnimalId.size();
            });
        } else {
            Log.e("Anima_Descrip", "Failed to load animal.");
        }
    }

    private void checkAnswer(int correctAnswerIndex, int clickedIndex) {
        if (correctAnswerIndex == clickedIndex) {
            // Handle the correct answer logic (e.g., increment score)
            // Load the next set of questions
            animalUpdateDescript();
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();
        } else {
            // Handle the incorrect answer logic (e.g., show a message)
            Toast.makeText(this, "Incorrect. Try again!", Toast.LENGTH_SHORT).show();
        }
    }
    private String getAnimalImageSource(Animal_Model animal) {
        // Assuming you have a method getImageSource() in your Animal_Model class
        return animal.getImageSource();
    }

    private List<String> getShuffledAnimalImageSources(Animal_Model animal_model) {
        List<String> animalImageSources = new ArrayList<>();
        animalImageSources.add(getAnimalImageSource(animal_model));

        // Add dummy image sources to make sure the list has at least 4 elements
        for (int i = 1; i < 4; i++) {
            animalImageSources.add(animal_model.getImageSource());
        }

        Collections.shuffle(animalImageSources);
        return animalImageSources;
    }
    private void loadAnimalImageIntoButton(ImageButton button, String imageSource) {
        int resourceId = getResources().getIdentifier(imageSource, "drawable", getPackageName());
        Glide.with(this).load(resourceId).into(button);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == ANIMAL_LOADER_ID && args != null) {
            int animalId = args.getInt("animalId", -1);
            return new Anima_Descrip.AnimalLoader(this, animalId);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == ANIMAL_LOADER_ID && cursor != null && cursor.moveToFirst()) {
            Animal_Model animal = new Animal_Model(cursor.getInt(0),
                    cursor.getString(1), cursor.getString(2), cursor.getString(3),
                    cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8));

            if (!animalData.contains(animal)) {
                animalData.add(animal);
            }

            onLoadFinished(animal);
            currentAnimalIndex = (currentAnimalIndex + 1) % allAnimalId.size();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        runOnUiThread(() -> {
        });
    }

    public List<Integer> getShuffledAnimalIdsInRange(int startId, int endId) {
        List<Integer> animalIds = getAnimalIdsInRange(startId, endId);
        Collections.shuffle(animalIds);
        return animalIds;
    }

    public List<Integer> getAnimalIdsInRange(int startId, int endId) {
        List<Integer> animalIds = new ArrayList<>();

        // Check if animalDatabase is not null before querying
        if (animalDatabase != null) {
            Cursor cursor = animalDatabase.getAllAnimalIdsInRange(startId, endId);

            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        int animalId = cursor.getInt(Integer.parseInt(String.valueOf(cursor.getColumnIndex("animal_id"))));
                        animalIds.add(animalId);
                    } while (cursor.moveToNext());
                }
                cursor.close();
            }
        }

        // Shuffle the animal IDs
        Collections.shuffle(animalIds);
        return animalIds;
    }
    private static class AnimalLoader extends AsyncTaskLoader<Cursor> {
        @SuppressLint("StaticFieldLeak")
        private final Context context;
        private final int animalId;

        public AnimalLoader(Context context, int animalId) {
            super(context);
            this.context = context;
            this.animalId = animalId;
        }
        @Nullable
        @Override
        public Cursor loadInBackground() {
            String uriString = CONTENT_PROVIDER_URI + animalId;

            // Log statement to check the URI being used for the content provider query
            Log.d("Anima_Descrip", "Content Provider URI: " + uriString);

            Uri uri = Uri.parse(uriString);
            return context.getContentResolver().query(uri, null, null, null, null);
        }

        @Override
        protected void onStartLoading() {
            forceLoad();

            // Log statement to check if the loader is starting to load data
            Log.d("Anima_Descrip", "Loader onStartLoading - animalId: " + animalId);
        }
    }

}
