package com.aitekteam.developer.playnote.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.aitekteam.developer.playnote.datas.Note;
import com.aitekteam.developer.playnote.helpers.NoteHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoteProvider extends ContentProvider {

    private static final int NOTES = 0;
    private static final int NOTE_ITEM = 1;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(Note.CONTENT_AUTHORITY, Note.PATH_NOTE, NOTES);
        uriMatcher.addURI(Note.CONTENT_AUTHORITY, Note.PATH_NOTE + "/#", NOTE_ITEM);
    }
    private NoteHelper helper;

    @Override
    public boolean onCreate() {
        helper = new NoteHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor;
        int uriMatch = uriMatcher.match(uri);
        switch (uriMatch) {
            case NOTES:
                cursor = db.query(Note.NoteColumns.TABLE_NAME, projection, selection, selectionArgs
                , null, null, sortOrder);
                break;
            case NOTE_ITEM:
                selection = Note.NoteColumns._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(Note.NoteColumns.TABLE_NAME, projection, selection, selectionArgs
                , null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return Note.NoteColumns.CONTENT_LIST_TYPE;
            case NOTE_ITEM:
                return Note.NoteColumns.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                String checkTitle = values.getAsString(Note.NoteColumns._TITLE);
                String checkDescription = values.getAsString(Note.NoteColumns._DESCRIPTION);

                if (checkTitle == null)
                    throw new IllegalArgumentException("Note requires valid title");
                else if (checkDescription == null)
                    throw new IllegalArgumentException("Note requires valid description");
                else {
                    SQLiteDatabase db = helper.getWritableDatabase();
                    long insertID = db.insert(Note.NoteColumns.TABLE_NAME, null, values);
                    if (insertID == -1) {
                        throw new IllegalArgumentException("Note is something error");
                    }
                    getContext().getContentResolver().notifyChange(uri, null);
                    return ContentUris.withAppendedId(uri, insertID);
                }
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int rowsDeleted;
        final int match = uriMatcher.match(uri);
        if (match != NOTE_ITEM && match != NOTES) {
            throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (match == NOTE_ITEM) {
            selection = Note.NoteColumns._ID + "=?";
            selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        }
        rowsDeleted = db.delete(Note.NoteColumns.TABLE_NAME, selection, selectionArgs);
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        if (match != NOTE_ITEM && match != NOTES) {
            throw new IllegalArgumentException("Update is not supported for " + uri);
        }
        if (match == NOTE_ITEM) {
            selection = Note.NoteColumns._ID + "=?";
            selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
        }

        if (values.size() == 0) return 0;

        String checkTitle = values.getAsString(Note.NoteColumns._TITLE);
        String checkDescription = values.getAsString(Note.NoteColumns._DESCRIPTION);

        if (checkTitle == null)
            throw new IllegalArgumentException("Note requires valid title");
        else if (checkDescription == null)
            throw new IllegalArgumentException("Note requires valid description");
        else {
            int updateID = db.update(Note.NoteColumns.TABLE_NAME, values, selection, selectionArgs);
            if (updateID == 0) {
                throw new IllegalArgumentException("Note is something error");
            }
            getContext().getContentResolver().notifyChange(uri, null);
            return updateID;
        }
    }
}
