package com.example.biboo;

import android.net.Uri;
import android.provider.BaseColumns;

public final class AnimalDataContract {
    private AnimalDataContract() {} // Private constructor to prevent instantiation

    // Content Provider Authority
    public static final String CONTENT_AUTHORITY = "com.example.biboo.animalcontentprovider";

    // Base content URI
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Path for the "animals" directory
    public static final String PATH_ANIMALS = "animals";

    // Content URI for the entire provider
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_ANIMALS);

    // Columns for the "animals" table
    public static class AnimalEntry implements BaseColumns {
        public static final String TABLE_NAME = "animal_data";
        public static final String ANIMAL_ID = "animal_id";
        public static final String ANIMAL_NAME = "animal_name";
        public static final String ANIMAL_SCIENTIFIC_NAME = "scientific_name";
        public static final String ANIMAL_CLASSIFICATION = "classification";
        public static final String ANIMAL_HABITAT = "habitat";
        public static final String ANIMAL_DIET = "diet";
        public static final String ANIMAL_DESCRIPTION = "description";
        public static final String ANIMAL_TRIVIA = "trivia";
        public static final String ANIMAL_IMAGE_SOURCE = "img_src";
    }
}
