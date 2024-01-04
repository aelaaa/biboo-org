package com.example.biboo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;

public class SignUp_activity extends AppCompatActivity {

    EditText signup_name, signup_email, signup_username, signup_password, signup_confirmpassword;
    Button btnBirthdate, btnsignup;
    RadioGroup rdg_gender;
    RadioButton rdbtn_male, rdbtn_female;
    Cursor cursor;
    User_Database user_database;
    int age;
     Prompt_Dialogs promptDialogs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ImmersiveMode.enableImmersiveMode(getWindow().getDecorView());
        promptDialogs = new Prompt_Dialogs(this);

        signup_name = findViewById(R.id.etx_signup_name);
        signup_email = findViewById(R.id.etx_signup_email);
        signup_username = findViewById(R.id.etx_signup_username);
        signup_password = findViewById(R.id.etx_signup_password);
        signup_confirmpassword = findViewById(R.id.etx_confirm_password);
        btnBirthdate = findViewById(R.id.btn_setDate);
        btnsignup = findViewById(R.id.btn_signUp);
        rdbtn_male = findViewById(R.id.rdbtn_male);
        rdbtn_female = findViewById(R.id.rdbtn_female);
        rdg_gender = findViewById(R.id.btn_grp_Gender);
        user_database = new User_Database(this);



        //button listener actions
        btnBirthdate.setOnClickListener(V -> {
            setDate();
        });
        btnsignup.setOnClickListener(V -> {
            Sign_Up();
        });

    }




    public void setDate() {
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) -> {
                    // Handle the selected date
                    String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);

                    // Calculate age based on the selected date
                    age = calculateAge(year, monthOfYear, dayOfMonth);
                    btnBirthdate.setText(selectedDate);
                },
                currentYear,
                currentMonth,
                currentDay
        );

        // Set the maximum date to the current date to prevent selecting future dates
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private int calculateAge(int birthYear, int birthMonth, int birthDay) {
        Calendar dob = Calendar.getInstance();
        dob.set(birthYear, birthMonth, birthDay);

        Calendar today = Calendar.getInstance();

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    public void Sign_Up(){

            int selectedGenderId = rdg_gender.getCheckedRadioButtonId();
            String gender;
            if (selectedGenderId == R.id.rdbtn_male) {
                gender = "Male";
            }
            else if (selectedGenderId == R.id.rdbtn_female){
                gender = "Female";
            }
            else {gender = "Unknown";}

            String name = signup_name.getText().toString().trim();
            String username = signup_username.getText().toString().trim();
            String email = signup_email.getText().toString().trim();
            String password = signup_password.getText().toString().trim();
            String confirmPassword = signup_confirmpassword.getText().toString().trim();
            String birthdate = btnBirthdate.getText().toString().trim();

        if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || birthdate.isEmpty() || selectedGenderId == -1) {
            clear();
            promptDialogs.statusDialogs("ERROR!", "Please fill in all the fields!");
            promptDialogs.show();
        } else {
            // Check if age is between 0 and 150
            if (age == 0 || age > 150) {
                clear();
                btnBirthdate.requestFocus();
                promptDialogs.statusDialogs("ERROR!", "Invalid age! Please select a valid birthdate.");
                promptDialogs.show();
            } else {
                SQLiteDatabase db = user_database.getWritableDatabase();
                if (password.equals(confirmPassword)) {
                    cursor = db.query(User_Database.TABLE_NAME, null, "username=?", new String[]{username}, null, null, null);
                    if (cursor.getCount() > 0) {
                        clear();
                        signup_username.requestFocus();
                        promptDialogs.statusDialogs("ERROR!", "Username already exists! Try again.");
                        promptDialogs.show();
                    } else {


                        // Insert the user into the database
                        ContentValues values = new ContentValues();
                        values.put("username", username);
                        values.put("password", password);
                        values.put("name", name);
                        values.put("age", age);
                        values.put("gender", gender);
                        values.put("email", email);

                        long result = db.insert(User_Database.TABLE_NAME, null, values);
                        if (result != -1) {
                            // Insert successful
                            // If the signup is successful, set the result and finish the activity
                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("username", username);
                            setResult(RESULT_OK, resultIntent);
                            Intent h = new Intent(SignUp_activity.this, Homepage_activity.class);
                            clear();
                            startActivity(h);
                            finish();
                        } else {
                            // Insert failed
                            clear();
                            promptDialogs.statusDialogs("ERROR!", "Error in inserting the data!");
                            promptDialogs.show();
                        }

                        db.close();
                    }

                } else {
                    clear();
                    signup_username.requestFocus();
                    promptDialogs.statusDialogs("ERROR!", "Password do not match! Try again.");
                    promptDialogs.show();

                }
            }
        }
    }

    public void clear(){

        signup_name.setText("");
        signup_email.setText("");
        signup_username.setText("");
        signup_password.setText("");
        signup_confirmpassword.setText("");
        btnBirthdate.setText("SET DATE");
        rdg_gender.clearCheck();


    }


}

