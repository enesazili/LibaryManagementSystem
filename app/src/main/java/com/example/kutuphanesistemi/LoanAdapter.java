package com.example.kutuphanesistemi;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LoanAdapter extends RecyclerView.Adapter<LoanAdapter.LoanViewHolder> {

    private Cursor cursor;
    private OnItemClickListener listener; 

    public LoanAdapter(Cursor cursor) {
        this.cursor = cursor;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public LoanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loan, parent, false);
        return new LoanViewHolder(v, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanViewHolder holder, int position) {
        if (cursor == null || !cursor.moveToPosition(position)) {
            return;
        }
        String stuName = cursor.getString(cursor.getColumnIndexOrThrow("stu_name"));
        String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("book_title"));
        String loanDate = cursor.getString(cursor.getColumnIndexOrThrow("loan_date"));
        String duration = cursor.getString(cursor.getColumnIndexOrThrow("duration_days"));

        holder.txtStudentName.setText(stuName);
        holder.txtBookName.setText(bookTitle);
        holder.txtDate.setText("Tarih: " + loanDate);
        holder.txtDuration.setText("Süre: " + duration + " Gün");
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public static class LoanViewHolder extends RecyclerView.ViewHolder {
        TextView txtStudentName, txtBookName, txtDate, txtDuration;

        public LoanViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            txtStudentName = itemView.findViewById(R.id.text_student_name);
            txtBookName = itemView.findViewById(R.id.text_book_name);
            txtDate = itemView.findViewById(R.id.text_date);
            txtDuration = itemView.findViewById(R.id.text_time_left);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(position);
                    }
                }
            });
        }
    }
}