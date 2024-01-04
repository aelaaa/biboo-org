package com.example.biboo;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Anima_Diet extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnLongClickListener, Prompt_Dialogs.ResetGameActivity{

    private ImageView info, hint, settings, animal;
    private TextView animadiet_score, game_level, score_update;
    private String diet, animalDiet;
    private int currentLevel = 0;
    private int sublevel = 0;
    private int win, winstreak, losestreak;
    private int score = 0;
    private int bestscore = 0;
    private ConstraintLayout carnivore, herbivore, omnivore, dragNdropSubmit, animaDiet_constraintLayout;
    private List<Integer> allAnimalId = new ArrayList<>();
    private List<Animal_Model> animalData;
    private Animal_Database animalDatabase;
    private int currentAnimalIndex = 0;
    private ClipData clipdata;
    private Vibrator vibrator;
    private User_Database user_database;

    Prompt_Dialogs promptDialogs;

    private static final int ANIMAL_LOADER_ID = 1;
    private static final String CONTENT_PROVIDER_URI = "content://com.example.biboo.animalcontentprovider/animals/";


    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anima_diet);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());
        promptDialogs = new Prompt_Dialogs(this);
        user_database = new User_Database(this);
        initializeViews();
        win = 0; winstreak = 0; losestreak = 0;
        animalDatabase = new Animal_Database(getApplicationContext());
        animalData = new ArrayList<>();
        AnimalSetting();
        updateDietDrag();

        dragNdropSubmit.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                final View view = (View) event.getLocalState();
                int state = event.getAction();
                diet = clipdata.getDescription().getLabel().toString();
                switch (state) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        break;
                    case DragEvent.ACTION_DROP:
                        if (diet.equalsIgnoreCase(animalDiet)) {
                            win++; winstreak++;
                            losestreak = 0;
                            score_update.setVisibility(View.VISIBLE);
                            updateDietDrag();
                            runOnUiThread(() -> {
                                score_update.setVisibility(View.VISIBLE);
                                // Update the score and setText
                                PointingSystem(winstreak);
                                // Use a Handler to delay hiding the score_update TextView
                                new Handler().postDelayed(() -> score_update.setVisibility(View.INVISIBLE), 2000);
                            });

                        } else {
                            losestreak++;
                            winstreak = 0;
                            vibrate();
                            Log.d( "Animafication", "winstreak: " + winstreak +"| Losestreak: " + losestreak );

                        }
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                    case DragEvent.ACTION_DRAG_ENDED:

                        animaDiet_constraintLayout.removeView(view);
                        view.setVisibility(View.VISIBLE);
                        animaDiet_constraintLayout.addView(view);
                        carnivore.setVisibility(View.VISIBLE);
                        herbivore.setVisibility(View.VISIBLE);
                        omnivore.setVisibility(View.VISIBLE);
                        break;
                }
                return true;
            }

        });

        info.setOnClickListener(v-> {
            promptDialogs.infoDialog();
            promptDialogs.show();
        });

        hint.setOnClickListener(v-> {
            showHint();
        });
        settings.setOnClickListener(v -> {

            // Call the showSettingsDialog method when the settings icon is clicked
            promptDialogs.showSettingsDialog(getSupportFragmentManager());
        });


    }

    @Override
    protected void onStop() {
        super.onStop();
        saveScoreToDatabase();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
        saveScoreToDatabase();
    }
    public void updateDietDrag(){
        List<Integer> animalIds = allAnimalId;

        if (!animalIds.isEmpty()) {
            int currentAnimalId = animalIds.get(currentAnimalIndex);

            Bundle queryBundle = new Bundle();
            queryBundle.putInt("animalId", currentAnimalId);
            Log.d("AnimaDiet", "Updating animal ID " + currentAnimalId);
            getSupportLoaderManager().restartLoader(ANIMAL_LOADER_ID, queryBundle, this);

        }
    }

    private void initializeViews() {
        info = findViewById(R.id.imgInfo);
        hint = findViewById(R.id.imgBulbHint);
        settings = findViewById(R.id.imgSettings);
        animal = findViewById(R.id.imgAnimaDiet);
        animadiet_score = findViewById(R.id.tvScore);
        score_update = findViewById(R.id.tvscore_show);
        game_level = findViewById(R.id.tvLevel_count);
        carnivore = findViewById(R.id.img_carnivores_);
        herbivore = findViewById(R.id.img_herbivores);
        omnivore = findViewById(R.id.img_omnivores);
        dragNdropSubmit = findViewById(R.id.img_drop_box);
        animaDiet_constraintLayout = findViewById(R.id.animaDiet_ConstraintLayout);
        Glide.init(this, new GlideBuilder());

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        animadiet_score.setText("0");
        score_update.setVisibility(View.INVISIBLE);

        carnivore.setOnLongClickListener(this);
        herbivore.setOnLongClickListener(this);
        omnivore.setOnLongClickListener(this);
    }

    private void PointingSystem(int winstreak){
        if (winstreak >= 0 && winstreak < 2 ) {
            score = score + 100;
            score_update.setText("+100");
        } else if (winstreak > 1 && winstreak < 5) {
            score = score + 300;
            score_update.setText("+300");
        } else if (winstreak > 4 && winstreak < 10) {
            score = score + 500;
            score_update.setText("+500");
        } else if (winstreak > 9 && winstreak <=100) {
            score = score + 1000;
            score_update.setText("+1000");
        } else {
            score = score;
        }

        animadiet_score.setText(String.valueOf(score));
    }


    private void showHint() {
        final String title = "HINT!";
        final String description = "Would you like to use a hint? Using a hint would consume your accredited points by 300. Would you like to continue?";

        // Initialize diet before using it
        if (animalDiet != null) {
            promptDialogs.show();

            if (score >= 300) {
                List<ConstraintLayout> incorrectOptions = new ArrayList<>();
                if (!animalDiet.equalsIgnoreCase("Carnivore")) {
                    incorrectOptions.add(carnivore);
                }
                if (!animalDiet.equalsIgnoreCase("Herbivore")) {
                    incorrectOptions.add(herbivore);
                }
                if (!animalDiet.equalsIgnoreCase("Omnivore")) {
                    incorrectOptions.add(omnivore);
                }


                // Triggering the hint logic here
                // Assuming that the method HintDialogforAnimaDiet shows the dialog
                promptDialogs.HintDialogforAnimaDiet(title, description, new Prompt_Dialogs.AnimaDietUseHintCallback() {
                    @Override
                    public void onUseHint() {
                        score = score - 300;


                        runOnUiThread(() -> {
                            animadiet_score.setText(String.valueOf(score));
                            score_update.setText("-300");
                            score_update.setVisibility(View.VISIBLE);
                            new Handler().postDelayed(() -> score_update.setVisibility(View.INVISIBLE), 2000);
                        });
                        Random random = new Random();
                        incorrectOptions.get(random.nextInt(incorrectOptions.size())).setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                promptDialogs.statusDialogs("ERROR!", "You don't have enough points to use hints.");
                promptDialogs.show();
            }


        } else {
            // Handle the case where diet is null
            Log.e("AnimaDiet", "Diet is null when trying to show hint.");
        }
    }





    @Override
    public void onResetGameActivity() {

        currentLevel = 0;
        sublevel = 0;
        currentAnimalIndex = 0;
        AnimalSetting();
        updateDietDrag();
    }





    //OnLongCLick Method
    @Override
    public boolean onLongClick(View v) {
        v.setVisibility(View.INVISIBLE);
        View.DragShadowBuilder dragShadowBuilder = new View.DragShadowBuilder(v);
        clipdata = ClipData.newPlainText(v.getTag().toString(), null);
        v.startDrag(clipdata,dragShadowBuilder,v,0);
        return true;
    }





    private void loadAnimalImage(String imageSource) {
        int resourceId = getResources().getIdentifier(imageSource, "drawable", getPackageName());
        Log.d("Animafication", "Loading image with resourceId: " + resourceId);

        Glide.with(this)
                .load(resourceId)
                .into(animal);
    }

    private void onLoadFinished(Animal_Model animal) {
        if (animal != null) {
            animalDiet = animal.getDiet();
            Log.d("AnimaDiet", "onLoadFinished - Animal Diet: " + animalDiet);

            currentLevel = (currentLevel + 1) % 10;
            // Use runOnUiThread to update the UI on the main thread
            runOnUiThread(() -> {
                game_level.setText("Level " + sublevel + ":" + currentLevel);
                loadAnimalImage(animal.getImageSource());
            });

            if (currentLevel == 9) {
                // If currentLevel is back to 0, increment the sub-level
                sublevel++;
            }
            if (currentLevel == 10 && sublevel == 9) {
                allAnimalId.clear();
                AnimalSetting();
                updateDietDrag();
                promptDialogs.CongratsDialogs("CONGRATULATIONS", "You have completed all the levels! You will now return to the homescreen");
                promptDialogs.show();
            }
        } else {
            Log.e("Animafication", "Failed to load animal.");
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == ANIMAL_LOADER_ID && args != null) {
            int animalId = args.getInt("animalId", -1);
            return new Anima_Diet.AnimalLoader(this, animalId);
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

    public void AnimalSetting() {
        // Clear the list before adding new shuffled animal IDs
        allAnimalId.clear();

        // Add shuffled animal IDs for each difficulty level
        allAnimalId.addAll(getShuffledAnimalIdsInRange(1, 30));
        allAnimalId.addAll(getShuffledAnimalIdsInRange(31, 60));
        allAnimalId.addAll(getShuffledAnimalIdsInRange(61, 75));
        allAnimalId.addAll(getShuffledAnimalIdsInRange(76, 100));

        Log.d("Animafication", "AllAnimalId size: " + allAnimalId.size());
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

    private void vibrate() {
        // Check if the device has a vibrator
        if (vibrator.hasVibrator()) {
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
        }
    }

    private void saveScoreToDatabase() {

        bestscore = user_database.getScore(getSavedUsernameFromPreferences(), "animadiet_bestscore");
        if(bestscore == -1) {
            bestscore =0;
        }

        String username = getSavedUsernameFromPreferences();
        if(score < bestscore) {
            user_database.updateScore(username, "animadiet_score", score);
        } else if (score > bestscore) {
            user_database.updateScore(username, "animadiet_score", score);
            user_database.updateScore(username, "animadiet_bestscore", score);
        } else{
            user_database.updateScore(username, "animadiet_score", score);
        }
    }

    private String getSavedUsernameFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getString("username", "");
    }


}

