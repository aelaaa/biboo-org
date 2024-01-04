package com.example.biboo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Homepage_activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, Prompt_Dialogs.ExitGameCallback {

    Button animagames, animalia_sanctuary, score, exit;
    TextView text_fullname, text_username, text_trivia_animalName, text_trivia_description;
    ImageView animal, userIcon;
    private Animal_Database animalDatabase;
    private User_Database userDatabase;
    private List<Integer> allAnimalId = new ArrayList<>();
    private List<Animal_Model> animalData = new ArrayList<>();
    private int ANIMAL_LOADER_ID = 1;
    private int currentAnimalIndex = 0;
    private Prompt_Dialogs promptDialogs;
    Bundle bundle;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final String CONTENT_PROVIDER_URI = "content://com.example.biboo.animalcontentprovider/animals/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());
        setContentView(R.layout.activity_main);
        promptDialogs = new Prompt_Dialogs(this);
        promptDialogs.setExitGameCallback(this);
        initializeViews();
        loadUserData();
        updateTrivia();




         animagames.setOnClickListener(v-> {
             Intent intent = new Intent(Homepage_activity.this, AnimaGames.class);
             startActivity(intent);
         });
        animalia_sanctuary.setOnClickListener(v-> {
            Intent intent = new Intent(Homepage_activity.this, Gallery.class);
            startActivity(intent);
        });
        score.setOnClickListener(v-> {
            Intent intent = new Intent(Homepage_activity.this, viewscore.class);
            startActivity(intent);
        });
         exit.setOnClickListener(v-> {
            showExitDialog();
        });

        getSupportLoaderManager().initLoader(ANIMAL_LOADER_ID, null, this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }


    private void initializeViews(){
        animalDatabase = new Animal_Database(getApplicationContext());
        userDatabase = new User_Database(getApplicationContext());
        animalData = new ArrayList<>();
        animagames = findViewById(R.id.btnAnimagames);
        animalia_sanctuary = findViewById(R.id.btnAnimaliaSanctuary);
        score = findViewById(R.id.btnScore);
        exit = findViewById(R.id.btnExit);
        text_fullname = findViewById(R.id.txtFullname);
        text_username = findViewById(R.id.txtUsername);
        text_trivia_animalName = findViewById(R.id.tvTriviaTitle);
        text_trivia_description = findViewById(R.id.tvTriviaDescription);
        animal = findViewById(R.id.imgTriviaImg);
        userIcon = findViewById(R.id.imgUserIcon);

        allAnimalId.clear();
        allAnimalId.addAll(getShuffledAnimalIdsInRange(1, 100));
    }

    private void updateTrivia() {
        List<Integer> animalIds = allAnimalId;

        if (!animalIds.isEmpty()) {
            int currentAnimalId = animalIds.get(currentAnimalIndex);

            Bundle queryBundle = new Bundle();
            queryBundle.putInt("animalId", currentAnimalId);
            Log.d("Homepage", "Updating trivia for animalId: " + currentAnimalId);

            getSupportLoaderManager().restartLoader(ANIMAL_LOADER_ID, queryBundle, this);

        }
    }
    private void loadAnimalImage(String imageSource) {
        int resourceId = getResources().getIdentifier(imageSource, "drawable", getPackageName());
        Log.d("Animafication", "Loading image with resourceId: " + resourceId);

        Glide.with(this)
                .load(resourceId)
                .into(animal);
    }

    @SuppressLint("Range")
    private void loadUserData() {

            String usernameToLoad = getSavedUsernameFromPreferences();
            Log.d("Username", "Username: " + usernameToLoad);

            // Get user data from the database
            Cursor cursor = userDatabase.getUserdata(usernameToLoad);


            int user_icon;
            if (cursor != null && cursor.moveToFirst()) {
                if (cursor.getString(cursor.getColumnIndex("gender")).equals("Male")) {
                        user_icon = R.drawable.boy;
                }else if (cursor.getString(cursor.getColumnIndex("gender")).equals("Female")) {
                        user_icon = R.drawable.girl;
                } else {
                    user_icon = 0;
                }
                runOnUiThread(() -> {
                    Glide.with(this)
                                .load(user_icon)
                                .into(userIcon);
                    text_fullname.setText(cursor.getString(cursor.getColumnIndex("name")));
                    text_username.setText(cursor.getString(cursor.getColumnIndex("username")));
                });
                cursor.close();
            }
        }

        private String getSavedUsernameFromPreferences() {
            SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
            return preferences.getString("username", "");
        }


    private void onLoadFinished(Animal_Model animal) {
        if (animal != null) {

            runOnUiThread(() -> {
                text_trivia_animalName.setText(animal.getAnimal_Name());
                text_trivia_description.setText(animal.getTrivia());
                loadAnimalImage(animal.getImageSource());
            });
        }
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == ANIMAL_LOADER_ID && args != null) {
            int animalId = args.getInt("animalId", -1);
            return new Homepage_activity.AnimalLoader(this, animalId);
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

    }

    @Override
    public void onExitGame() {
        finishAffinity();
    }

    private static class AnimalLoader extends AsyncTaskLoader<Cursor> {
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
            Log.d("Animafication", "Content Provider URI: " + uriString);

            Uri uri = Uri.parse(uriString);
            return context.getContentResolver().query(uri, null, null, null, null);
        }

        @Override
        protected void onStartLoading() {
            forceLoad();

            // Log statement to check if the loader is starting to load data
            Log.d("Animafication", "Loader onStartLoading - animalId: " + animalId);
        }
    }
    private void showExitDialog() {
        promptDialogs.confirmationDialogs("Exit Confirmation", "Are you sure you want to exit?", this);
        promptDialogs.show();
    }
}