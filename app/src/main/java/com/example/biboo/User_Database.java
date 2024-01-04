package com.example.biboo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class User_Database extends SQLiteOpenHelper {

    static final String TABLE_NAME = "user_data";
    static final String DB_name = "BiBoo";

    public User_Database(Context context) {
        super(context, DB_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE " + TABLE_NAME +
                "(username TEXT PRIMARY KEY, password TEXT , name TEXT, age INTEGER, gender TEXT, email TEXT, animafication_score INTEGER,animafication_bestscore INTEGER, animadiet_score INTEGER, animadiet_bestscore INTEGER, animadescript_score INTEGER, animadescript_bestscore INTEGER)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public Cursor getUserdata(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
        return db.rawQuery(query, new String[]{username});
    }

    public void updateScore(String username, String columnName, int newScore) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(columnName, newScore);

        // Update the specific column for the given username
        db.update(TABLE_NAME, values, "username=?", new String[]{username});

        // Close the database
        db.close();
    }

    public int getScore(String username, String columnName) {
        int score = -1;  // Default value if the username or column doesn't exist

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + columnName + " FROM " + TABLE_NAME + " WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor.moveToFirst()) {
            score = cursor.getInt(0);
        }

        // Close the cursor and the database
        cursor.close();
        db.close();

        return score;
    }


}
