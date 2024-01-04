package com.example.biboo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login_activity extends AppCompatActivity {

    EditText username, password;
    TextView forgot, privacy, terms;
    Button login, signup;
    static User_Database user_database;
    Cursor cursor;
    Prompt_Dialogs promptDialogs;
    private static final int SIGNUP_REQUEST_CODE = 100;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());
        promptDialogs = new Prompt_Dialogs(this);

        username = findViewById(R.id.login_username_edittext);
        password = findViewById(R.id.login_password_edittext);
        login = findViewById(R.id.login_btn);
        signup = findViewById(R.id.signup_btn);
        login.setOnClickListener(v-> loginaccount());
        signup.setOnClickListener(v-> signupaccount());


        user_database = new User_Database(this);
        username.requestFocus();
    }






    // to Login btn and validation
    void loginaccount(){
        String user = username.getText().toString();
        String pass = password.getText().toString();

        if (user.isEmpty() || pass.isEmpty()){
            clear();
            promptDialogs.statusDialogs("ERROR!", "Please enter all the missing fields!");
            promptDialogs.show();




        }else {
            SQLiteDatabase db = user_database.getReadableDatabase();
            cursor = db.rawQuery("SELECT * FROM " + User_Database.TABLE_NAME + " WHERE username = ?", new String[]{user});

            if (cursor.getCount() == 0){
                clear();
                if(!isFinishing()) {
                    promptDialogs.statusDialogs("ERROR!", "Incorrect Username and/or Password!");
                        promptDialogs.show();

                }


            } else {
                cursor.moveToFirst();
                if (cursor.getString(1).equals(pass)){
                    // Save username in shared preferences
                    saveUsernameInPreferences(user);
                    Intent homepage = new Intent(Login_activity.this, Homepage_activity.class);
                    startActivity(homepage);
                    finish();
                } else {
                    clear();
                    promptDialogs.statusDialogs("ERROR!", "Incorrect Username and/or Password!");
                            promptDialogs.show();
                }
            }

            cursor.close();
            db.close();
        }
    }

    //for signup
    private void signupaccount() {
        Intent signup = new Intent(Login_activity.this, SignUp_activity.class);
        startActivityForResult(signup, SIGNUP_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGNUP_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                // Extract the username from the signup activity result
                String usernameFromSignup = data.getStringExtra("username");

                // Save the username in shared preferences
                saveUsernameInPreferences(usernameFromSignup);

                // Navigate to the homepage activity
                Intent homepage = new Intent(Login_activity.this, Homepage_activity.class);
                startActivity(homepage);
            }
        }
    }
    private void saveUsernameInPreferences(String username) {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("username", username);
        editor.apply();
    }



    //clearing all inputs in edittext fields
    public void clear(){
        username.setText("");
        password.setText("");
        username.requestFocus();
    }
}