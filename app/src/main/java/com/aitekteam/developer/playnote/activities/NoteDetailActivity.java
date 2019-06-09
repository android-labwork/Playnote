package com.aitekteam.developer.playnote.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.aitekteam.developer.playnote.R;
import com.aitekteam.developer.playnote.datas.Note;

public class NoteDetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private TextView title, date, description;
    private Uri uri = null;
    private int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        initialize();

        try {
            id = getIntent().getIntExtra("_ID", -1);
            uri = getIntent().getData();
            if (id != -1 && uri != null) {
                getSupportLoaderManager().initLoader(0, null, this);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initialize() {
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        description = findViewById(R.id.description);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_detail_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.edit) {
            Intent intent = new Intent(NoteDetailActivity.this, NoteControlActivity.class);
            intent.putExtra("_ID", id);
            intent.setData(uri);
            startActivity(intent);
            return true;
        }else if (item.getItemId() == R.id.delete) {
            dialogAction(getString(R.string.msg_delete_note),
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getContentResolver().delete(uri, null, null);
                            finish();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            return true;
        }
        else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        String[] projection = {
                Note.NoteColumns._ID,
                Note.NoteColumns._TITLE,
                Note.NoteColumns._DESCRIPTION,
                Note.NoteColumns._DATE};

        return new CursorLoader(this,
                uri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() == 1) {
            if (data.moveToFirst()) {
                title.setText(data.getString(data.getColumnIndex(Note.NoteColumns._TITLE)));
                date.setText(data.getString(data.getColumnIndex(Note.NoteColumns._DATE)));
                description.setText(data.getString(data.getColumnIndex(Note.NoteColumns._DESCRIPTION)));
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }
}
