package com.aitekteam.developer.playnote.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aitekteam.developer.playnote.R;
import com.aitekteam.developer.playnote.datas.Note;
import com.aitekteam.developer.playnote.helpers.DatePickerHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteControlActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private TextView date;
    private EditText title, description;
    private static final int SAVE_ACTION = 1;
    private static final int SAVE_ACTION_ALERT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_control);

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        initialize();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        date.setText(simpleDateFormat.format(new Date()));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerHelper datePicker= new DatePickerHelper();
                datePicker.setHandler(new DatePickerHelper.DatePickerHandler() {
                    @Override
                    public void onDateSet(int yer, int month, int dayOfMonth) {
                        date.setText("" + yer + "/" + month + "/" + dayOfMonth);
                    }
                });
                datePicker.show(getSupportFragmentManager(), "Set Date");
            }
        });
    }

    private void initialize() {
        date = findViewById(R.id.date);
        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_control_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            saveNote(SAVE_ACTION);
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            saveNote(SAVE_ACTION_ALERT);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNote(int action) {
        String data_title = title.getText().toString().trim();
        String data_desscription = description.getText().toString().trim();
        String data_date = date.getText().toString().trim();

        final ContentValues values = new ContentValues();
        values.put(Note.NoteColumns._TITLE, data_title);
        values.put(Note.NoteColumns._DESCRIPTION, data_desscription);
        values.put(Note.NoteColumns._DATE, data_date);

        if (action == SAVE_ACTION_ALERT) {
            dialogAction(getString(R.string.msg_discard_save),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri newUri = getContentResolver().insert(Note.NoteColumns.CONTENT_URI, values);
                            if (newUri == null)
                                Toast.makeText(NoteControlActivity.this, R.string.msg_insert_failed, Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(NoteControlActivity.this, R.string.msg_insert_success, Toast.LENGTH_SHORT).show();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
        }
        else {
            Uri newUri = getContentResolver().insert(Note.NoteColumns.CONTENT_URI, values);
            if (newUri == null)
                Toast.makeText(this, R.string.msg_insert_failed, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(this, R.string.msg_insert_success, Toast.LENGTH_SHORT).show();
        }
        finish();
    }

    private void dialogAction(String message, DialogInterface.OnClickListener accept,
                              DialogInterface.OnClickListener discard) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton(R.string.action_yes, accept);
        builder.setNegativeButton(R.string.action_no, discard);

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
