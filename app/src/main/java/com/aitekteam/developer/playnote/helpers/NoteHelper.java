package com.aitekteam.developer.playnote.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.aitekteam.developer.playnote.datas.Note.NoteColumns;

import androidx.annotation.Nullable;

public class NoteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "playnote.db";
    private static final int DATABASE_VERSION = 1;


    public NoteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_NOTE_TABLE = "CREATE TABLE " + NoteColumns.TABLE_NAME + " (" +
                NoteColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteColumns._TITLE + " TEXT NOT NULL, " +
                NoteColumns._DATE + " TEXT NOT NULL, " +
                NoteColumns._DESCRIPTION + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_NOTE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
