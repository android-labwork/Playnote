package com.aitekteam.developer.playnote.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.aitekteam.developer.playnote.R;
import com.aitekteam.developer.playnote.helpers.DatePickerHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteControlActivity extends AppCompatActivity {

    private TextView date;
    private EditText title, description;

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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
