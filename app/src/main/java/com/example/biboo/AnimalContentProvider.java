package com.example.biboo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class AnimalContentProvider extends ContentProvider {

    private Animal_Database dbHelper;

    // Define authority and paths
    private static final String AUTHORITY = "com.example.biboo.animalcontentprovider";
    private static final String PATH_ANIMALS = "animals";

    // UriMatcher instance
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    private static final int ANIMALS = 100;
    private static final int ANIMAL_ID = 101;

    static {
        uriMatcher.addURI(AUTHORITY, PATH_ANIMALS, ANIMALS);
        uriMatcher.addURI(AUTHORITY, PATH_ANIMALS + "/#", ANIMAL_ID);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new Animal_Database(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        // Get the database instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Match the URI to determine the operation
        int match = uriMatcher.match(uri);

        Cursor cursor;

        switch (match) {
            case ANIMALS:
                // Handle query for the list of animals
                cursor = db.query("animal_data", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case ANIMAL_ID:
                // Handle query for a specific animal
                long animalId = ContentUris.parseId(uri);
                cursor = getAnimal((int) animalId, projection, selection, selectionArgs, sortOrder, db);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        // Set a notification URI so that the cursor is updated if the data changes
        Context context = getContext();
        if (context != null) {
            cursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return cursor;
    }

    private Cursor getAnimal(int animalId, String[] projection, String selection, String[] selectionArgs, String sortOrder, SQLiteDatabase db) {
        // Build the WHERE clause based on the provided selection and selectionArgs
        String whereClause = (selection == null) ? "animal_id = ?" : "animal_id = ? AND " + selection;
        String[] whereArgs = (selectionArgs == null) ? new String[]{String.valueOf(animalId)} : new String[]{String.valueOf(animalId), selectionArgs[0]};

        // Perform the query on the database
        return db.query("animal_data", projection, whereClause, whereArgs, null, null, sortOrder);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long id = db.insert("your_table_name", null, values);

        if (id == -1) {
            Log.e(String.valueOf(this), "Failed to insert row for " + uri);
            return null;
        }

        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsDeleted;

        switch (uriMatcher.match(uri)) {
            case ANIMALS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = db.delete("animal_data", selection, selectionArgs);
                break;
            case ANIMAL_ID:
                // Delete a single row given by the ID in the URI
                long animalId = ContentUris.parseId(uri);
                rowsDeleted = db.delete("animal_data", "animal_id=?", new String[]{String.valueOf(animalId)});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (rowsDeleted != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int rowsUpdated;

        switch (uriMatcher.match(uri)) {
            case ANIMALS:
                // Update all rows that match the selection and selection args
                rowsUpdated = db.update("animal_data", values, selection, selectionArgs);
                break;
            case ANIMAL_ID:
                // Update a single row given by the ID in the URI
                long animalId = ContentUris.parseId(uri);
                rowsUpdated = db.update("animal_data", values, "animal_id=?", new String[]{String.valueOf(animalId)});
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (rowsUpdated != 0) {
            Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }


}