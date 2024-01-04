package com.example.biboo;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import android.os.Handler;


/** @noinspection ALL*/
public class Animafication extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, Prompt_Dialogs.ResetGameActivity {

    private ImageView info, hint, settings, animal;
    private TextView score_update, animafication_score, game_level, jumble_word;
    private EditText animal_guess;
    private Button submit;
    private String originalAnimalName, jumbledAnimalName, hint_description;
    private List<Integer> allAnimalId = new ArrayList<>();
    private List<Animal_Model> animalData = new ArrayList<>();
    private Animal_Database animalDatabase;
    private int currentLevel = 0;
    private int sublevel = 0;
    private int win, winstreak, losestreak;
    private int currentAnimalIndex = 0;
    private int score = 0;
    private int bestscore = 0;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final int ANIMAL_LOADER_ID = 1;
    Prompt_Dialogs promptDialogs;
    private static final String CONTENT_PROVIDER_URI = "content://com.example.biboo.animalcontentprovider/animals/";
    private Vibrator vibrator;
    private User_Database user_database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animafication);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());
        promptDialogs = new Prompt_Dialogs(this);
        user_database = new User_Database(this);
        initializeViews();
        win = 0; winstreak = 0; losestreak = 0;
        animalDatabase = new Animal_Database(getApplicationContext());
        animalData = new ArrayList<>();
        AnimalSetting();
        updateJumbledWord();


        settings.setOnClickListener(v -> {
            // Call the showSettingsDialog method when the settings icon is clicked
            promptDialogs.showSettingsDialog(getSupportFragmentManager());
        });

        info.setOnClickListener(v-> {
            promptDialogs.infoDialog();
            promptDialogs.show();
        });


        submit.setOnClickListener(v -> {
            if (originalAnimalName != null && originalAnimalName.equalsIgnoreCase(animal_guess.getText().toString().trim())) {
                win++; winstreak++;
                losestreak = 0;
                score_update.setVisibility(View.VISIBLE);
                updateJumbledWord();
                animal_guess.setText("");

                runOnUiThread(() -> {
                    score_update.setVisibility(View.VISIBLE);
                    // Update the score and setText
                    PointingSystem(winstreak);
                    // Use a Handler to delay hiding the score_update TextView
                    new Handler().postDelayed(() -> score_update.setVisibility(View.INVISIBLE), 2000);
                });

                // for debugging
                Log.d( "Animafication", "winstreak: " + winstreak +"| Losestreak: " + losestreak );
            } else {
                losestreak++;
                winstreak = 0;
                vibrate();
                Log.d( "Animafication", "winstreak: " + winstreak +"| Losestreak: " + losestreak );
                Toast.makeText(this, "Animal guess is wrong!", Toast.LENGTH_SHORT).show();

                // Update the hint button click listener based on the new value of losestreak
                if (losestreak >= 3) {
                    hint.setClickable(true);
                    hint.setOnClickListener(hintClickListener);
                } else {
                    hint.setClickable(false);
                    hint.setOnClickListener(null);
                }
            }
        });

        getSupportLoaderManager().initLoader(ANIMAL_LOADER_ID, null, this);

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
        // Update animafication_score with the current score
        animafication_score.setText(String.valueOf(score));
    }
    private void initializeViews() {
        info = findViewById(R.id.imgInfo);
        hint = findViewById(R.id.imgBulbHint);
        settings = findViewById(R.id.imgSettings);
        animal = findViewById(R.id.imgAnimafication);
        animafication_score = findViewById(R.id.tvScore);
        score_update = findViewById(R.id.tvscore_show);
        game_level = findViewById(R.id.tvLevel_count);
        jumble_word = findViewById(R.id.tvJumbleeWord);
        animal_guess = findViewById(R.id.edtCorrectWord);
        submit = findViewById(R.id.btnSubmitFixJumble);
        Glide.init(this, new GlideBuilder());
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        animafication_score.setText("0");
        score_update.setVisibility(View.INVISIBLE);
    }

    private void updateJumbledWord() {
        List<Integer> animalIds = allAnimalId;

        if (!animalIds.isEmpty()) {
            int currentAnimalId = animalIds.get(currentAnimalIndex);

            Bundle queryBundle = new Bundle();
            queryBundle.putInt("animalId", currentAnimalId);
            Log.d("Animafication", "Updating jumbled word for animalId: " + currentAnimalId);

            getSupportLoaderManager().restartLoader(ANIMAL_LOADER_ID, queryBundle, this);

        }
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

    public static String shuffleAnimalName(String input) {
        // Split the input into words
        String[] words = input.split("\\s+");

        // Shuffle each word independently
        List<String> shuffledWords = Arrays.stream(words)
                .map(word -> (word.length() > 1) ? shuffleWord(word) : word)
                .collect(Collectors.toList());

        // Join the shuffled words back into a single string
        return String.join(" ", shuffledWords);
    }

    public static String shuffleWord(String word) {
        // Convert the string to a list of characters
        List<Character> charList = word.chars().mapToObj(c -> (char) c).collect(Collectors.toList());

        // Shuffle the list
        Collections.shuffle(charList);

        // Convert the list back to a string
        return charList.stream().map(String::valueOf).collect(Collectors.joining());
    }

    public static String reshuffleWord(String word) {
        String shuffledWord;
        do {
            shuffledWord = shuffleWord(word);
        } while (shuffledWord.equals(word));
        return shuffledWord;
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
            originalAnimalName = animal.getAnimal_Name();
            jumbledAnimalName = shuffleAnimalName(originalAnimalName);
            hint_description = animal.getDescription();

            // Log statements to check if onLoadFinished is being called and the values
            Log.d("Animafication", "onLoadFinished - Original Animal Name: " + originalAnimalName);
            Log.d("Animafication", "onLoadFinished - Jumbled Animal Name: " + jumbledAnimalName);

            currentLevel = (currentLevel + 1) % 10;
            // Use runOnUiThread to update the UI on the main thread
            runOnUiThread(() -> {
                jumble_word.setText(jumbledAnimalName.toUpperCase());
                game_level.setText("Level " + sublevel + ":" + currentLevel);
                loadAnimalImage(animal.getImageSource());
            });

            if (currentLevel == 9) {
                // If currentLevel is back to 0, increment the sub-level
                sublevel++;
            }
            if (currentLevel == 10 && sublevel == 9){
                allAnimalId.clear();
                AnimalSetting();
                updateJumbledWord();
                promptDialogs.CongratsDialogs("CONGRATULATIONS", "You have completed all the levels! You will now return to the homescreen");
                promptDialogs.show();
            }
        } else {
            Log.e("Animafication", "Failed to load animal.");
        }
    }

    private View.OnClickListener hintClickListener = v -> {
        promptDialogs.HintDialogforAnimafication("HINT!", hint_description);
        promptDialogs.show();
    };


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == ANIMAL_LOADER_ID && args != null) {
            int animalId = args.getInt("animalId", -1);
            return new AnimalLoader(this, animalId);
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
        runOnUiThread(() -> {});
    }


    @Override
    public void onResetGameActivity() {

        currentLevel = 0;
        sublevel = 0;
        currentAnimalIndex = 0;

        AnimalSetting();
        updateJumbledWord();
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

    private void vibrate() {
        // Check if the device has a vibrator
        if (vibrator.hasVibrator()) {
            // Vibrate for 500 milliseconds
            vibrator.vibrate(500);
        }
    }

    private void saveScoreToDatabase() {

        bestscore = user_database.getScore(getSavedUsernameFromPreferences(), "animafication_bestscore");
        if(bestscore == -1) {
            bestscore =0;
        }

        String username = getSavedUsernameFromPreferences();
        if(score < bestscore) {
            user_database.updateScore(username, "animafication_score", score);
        } else if (score > bestscore) {
            user_database.updateScore(username, "animafication_score", score);
            user_database.updateScore(username, "animafication_bestscore", score);
        } else{
            user_database.updateScore(username, "animafication_score", score);
        }
    }

    private String getSavedUsernameFromPreferences() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return preferences.getString("username", "");
    }
}

