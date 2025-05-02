package com.kshprimeindustries.tuneupme.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL("CREATE TABLE customer (\n" +
                "    email TEXT PRIMARY KEY,\n" +
                "    mobile TEXT NOT NULL,\n" +
                "    name TEXT NOT NULL,\n" +
                "    password TEXT NOT NULL,\n" +
                "    profile_image_path TEXT,\n" +
                "    status BOOLEAN NOT NULL,\n" +
                "    verification_code TEXT NOT NULL\n" +
                ");\n");


        sqLiteDatabase.execSQL("CREATE TABLE band (" +
                "    name TEXT NOT NULL, " +
                "    mobile TEXT NOT NULL, " +
                "    description TEXT, " +
                "    price_per_hour TEXT NOT NULL, " +
                "    profile_image_path TEXT, " +
                "    band_type_id TEXT NOT NULL, " +
                "    verification_code TEXT NOT NULL, " +
                "    email TEXT NOT NULL " +
                ");");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("CREATE TABLE IF NOT EXISTS band (" +
                    "    name TEXT NOT NULL, " +
                    "    mobile TEXT NOT NULL, " +
                    "    description TEXT, " +
                    "    price_per_hour TEXT NOT NULL, " +
                    "    profile_image_path TEXT, " +
                    "    band_type_id TEXT NOT NULL, " +
                    "    verification_code TEXT NOT NULL, " +
                    "    email TEXT NOT NULL " +
                    ");");
        }
    }
}
