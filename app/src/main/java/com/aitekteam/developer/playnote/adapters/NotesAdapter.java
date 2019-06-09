package com.aitekteam.developer.playnote.adapters;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aitekteam.developer.playnote.R;
import com.aitekteam.developer.playnote.datas.Note;
import com.aitekteam.developer.playnote.helpers.BaseCursorAdapterHelper;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotesAdapter extends BaseCursorAdapterHelper<NotesAdapter.ViewHolder> {
    private OnSelectedItemClickListener handler;
    public NotesAdapter(Cursor c, OnSelectedItemClickListener handler) {
        super(c);
        this.handler = handler;
    }

    @Override
    public void swapCursor(Cursor newCursor) {
        super.swapCursor(newCursor);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, Cursor cursor) {
        Note item = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.NoteColumns._ID)),
                cursor.getString(cursor.getColumnIndex(Note.NoteColumns._TITLE)),
                cursor.getString(cursor.getColumnIndex(Note.NoteColumns._DATE)),
                cursor.getString(cursor.getColumnIndex(Note.NoteColumns._DESCRIPTION))
        );
        holder.itemTitle.setText(item.getTitle());
        holder.itemDate.setText(item.getDate());
        if (item.getDescription().length() > 32)
            holder.itemDescription.setText(item.getDescription().substring(0, 30));
        else holder.itemDescription.setText(item.getDescription());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemTitle, itemDate, itemDescription;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDate = itemView.findViewById(R.id.item_date);
            itemDescription = itemView.findViewById(R.id.item_desc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handler.onItemClick(getAdapterPosition());
        }
    }

    @Override
    public Cursor getItem(int position) {
        return super.getItem(position);
    }

    public interface OnSelectedItemClickListener {
        void onItemClick(int position);
    }
}
