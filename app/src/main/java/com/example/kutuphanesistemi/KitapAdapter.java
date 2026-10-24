package com.example.kutuphanesistemi;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class KitapAdapter extends RecyclerView.Adapter<KitapAdapter.BookViewHolder> {

    private Cursor cursor;

    public KitapAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(v);
    }

    public interface OnItemClickListener {
        void onItemClick(int bookId);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        String title = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KITAP_AD));
        String author = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KITAP_YAZAR));
        String isbn = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KITAP_ISBN));

        holder.txtTitle.setText(title);
        holder.txtAuthor.setText(author);
        holder.txtIsbn.setText("ISBN: " + isbn);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KITAP_ID));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(id);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtAuthor, txtIsbn;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txt_list_book_title);
            txtAuthor = itemView.findViewById(R.id.txt_list_book_author);
            txtIsbn = itemView.findViewById(R.id.txt_list_book_isbn);
        }
    }
}