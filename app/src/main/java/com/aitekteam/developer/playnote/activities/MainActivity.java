package com.aitekteam.developer.playnote.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.Group;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.aitekteam.developer.playnote.R;
import com.aitekteam.developer.playnote.adapters.NotesAdapter;
import com.aitekteam.developer.playnote.datas.Note;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private Group iconNothing;
    private RecyclerView listNote;
    private NotesAdapter adapter;
    private ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        initialize();
        getSupportLoaderManager().initLoader(0, null, this);
    }

    private void initialize() {
        iconNothing = findViewById(R.id.group);
        listNote = findViewById(R.id.list_note);
        listNote.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        adapter = new NotesAdapter(this, null, new NotesAdapter.OnSelectedItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Cursor cursor = adapter.getItem(position);
                if (cursor != null && cursor.getCount() != 0) {
                    int id = cursor.getInt(cursor.getColumnIndex(Note.NoteColumns._ID));

                    if (actionMode != null) {
                        adapter.toggleSelected(String.valueOf(id));
                        if (adapter.getSelectedCount() == 0)
                            actionMode.finish();
                        else
                            actionMode.invalidate();
                        return;
                    }

                    Intent intent = new Intent(MainActivity.this, NoteDetailActivity.class);
                    intent.putExtra("_ID", id);

                    Uri currentPetUri = ContentUris.withAppendedId(Note.NoteColumns.CONTENT_URI, id);
                    intent.setData(currentPetUri);
                    startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(int position) {
                Cursor cursor = adapter.getItem(position);
                if (cursor != null && cursor.getCount() != 0) {
                    if (actionMode != null) return false;
                    int id = cursor.getInt(cursor.getColumnIndex(Note.NoteColumns._ID));
                    adapter.toggleSelected(String.valueOf(id));
                    actionMode = startSupportActionMode(mActionModeCallback);
                    return true;
                }
                return false;
            }
        });
        listNote.setAdapter(adapter);
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.main_toolbar_action_mode, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(String.valueOf(adapter.getSelectedCount()));
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (adapter.getSelectedCount() == 1) item.setVisible(true);
            else item.setVisible(false);

            switch (item.getItemId()) {
                case R.id.delete:
//                    removeHistory();
                    return true;
                case R.id.edit:
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            adapter.resetSelected();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_note) {
            startActivity(new Intent(MainActivity.this, NoteControlActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                Note.NoteColumns.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if (data != null && data.getCount() != 0) {
            listNote.setVisibility(View.VISIBLE);
            iconNothing.setVisibility(View.GONE);
            adapter.swapCursor(data);
        }
        else {
            listNote.setVisibility(View.GONE);
            iconNothing.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
