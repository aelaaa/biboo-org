package com.example.biboo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private Login_activity db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


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

        db = new Login_activity();

        Sign_Up();
        setDate();

    }


    public void setDate() {

        btnBirthdate.setOnClickListener(V -> {

            Calendar calendar = Calendar.getInstance();
            int currentYear = calendar.get(Calendar.YEAR);
            int currentMonth = calendar.get(Calendar.MONTH);
            int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    (DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) -> {
                        // Handle the selected date
                        String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, year);
                        btnBirthdate.setText(selectedDate);
                    },
                    currentYear,
                    currentMonth,
                    currentDay
            );
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    public void Sign_Up(){

        btnsignup.setOnClickListener(V -> {

            int selectedGenderId = rdg_gender.getCheckedRadioButtonId();
            String gender;
            if (selectedGenderId == R.id.rdbtn_male) {
                gender = "Male";
            } else {
                gender = "Female";
            }

            String name = signup_name.getText().toString();
            String username = signup_username.getText().toString();
            String email = signup_email.getText().toString();
            String password = signup_password.getText().toString();
            String confirmPassword = signup_confirmpassword.getText().toString();
            String birthdate = btnBirthdate.getText().toString();

            if (name.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ||
                birthdate.isEmpty() || (selectedGenderId == -1) ) {

                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                return;
            } else {

                if (password.equals(confirmPassword)) {
                    cursor = Login_activity.db.rawQuery("SELECT * FROM BIBOO WHERE Username = ?",
                            new String[]{username});
                    if (cursor.getCount() > 0) {
                        // User with the same username already exists
                        Toast.makeText(this, "Username already exist! Try again", Toast.LENGTH_SHORT).show();
                        clear();
                        signup_username.requestFocus();

                    } else {
                        // Insert the user into the database
                        Login_activity.db.execSQL("INSERT INTO BIBOO(Username, Password)" +
                                "VALUES('" + username + "'" + "," + "'" + password + "')");
                        Intent h = new Intent(SignUp_activity.this, Login_activity.class);
                        h.putExtra("username", username.toString());
                        clear();
                        startActivity(h);
                        finish();
                    }
                    cursor.close();
                    } else {
                    Toast.makeText(this, "Password do not match! Try again", Toast.LENGTH_SHORT).show();
                    clear();
                    signup_username.requestFocus();

                }
            }

        });
    }

    public void clear(){

        signup_name.setText("");
        signup_email.setText("");
        signup_username.setText("");
        signup_password.setText("");
        signup_confirmpassword.setText("");
        btnBirthdate.setText("");
        rdg_gender.clearCheck();


    }
}

//    db.execSQL("INSERT INTO StudentProfileDB (Employee_ID, Employee_Name)
//    VALUES " +
//            "('K11937426', 'Khyle Ushuaia Noblezada')");

//     Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
//     return;


