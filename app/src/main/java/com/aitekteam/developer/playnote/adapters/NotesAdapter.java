package com.aitekteam.developer.playnote.adapters;

import android.content.Context;
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
    private ArrayList<String> ids;
    private Context context;
    public NotesAdapter(Context context, Cursor c, OnSelectedItemClickListener handler) {
        super(c);
        this.context = context;
        this.handler = handler;
        this.ids = new ArrayList<>();
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

        if (getSelectedCount() > 0) {
            holder.itemSelected.setVisibility(View.VISIBLE);
            if (ids.contains(String.valueOf(item.getId()))) holder.itemSelected.setBackgroundColor(
                    context.getResources().getColor(R.color.colorBlueGrey)
            );
            else holder.itemSelected.setBackgroundColor(
                    context.getResources().getColor(R.color.colorLightGrey)
            );
        }
        else holder.itemSelected.setVisibility(View.GONE);
    }

    public void toggleSelected(String id) {
        if (ids.contains(id)) ids.remove(id);
        else ids.add(id);
        notifyDataSetChanged();
    }

    public void resetSelected() {
        ids.clear();
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return ids.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        TextView itemTitle, itemDate, itemDescription;
        View itemSelected;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.item_title);
            itemDate = itemView.findViewById(R.id.item_date);
            itemDescription = itemView.findViewById(R.id.item_desc);
            itemSelected = itemView.findViewById(R.id.item_selected);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            handler.onItemClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            return handler.onItemLongClick(getAdapterPosition());
        }
    }

    @Override
    public Cursor getItem(int position) {
        return super.getItem(position);
    }

    public ArrayList<String> getIds() {
        return ids;
    }

    public interface OnSelectedItemClickListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }
}
