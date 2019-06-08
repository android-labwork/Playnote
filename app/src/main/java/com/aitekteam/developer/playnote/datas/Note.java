package com.aitekteam.developer.playnote.datas;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.io.Serializable;

public class Note implements Serializable {
    private String title, date, description;
    private int id;

    // CURSOR MODEL

    public static final String CONTENT_AUTHORITY = "com.aitekteam.developer.playnote";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NOTE = "notes";
    public static final class NoteColumns implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTE);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTE;
        public static final String TABLE_NAME = "notes";
        public static final String _ID = BaseColumns._ID;
        public static final String _TITLE = "title";
        public static final String _DATE = "date";
        public static final String _DESCRIPTION = "description";
    }

    // OBJECT MODEL

    public Note() {
    }

    public Note(int id, String title, String date, String description) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
