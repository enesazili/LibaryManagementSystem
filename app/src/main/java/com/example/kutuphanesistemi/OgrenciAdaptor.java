package com.example.kutuphanesistemi;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OgrenciAdaptor extends RecyclerView.Adapter<OgrenciAdaptor.StudentViewHolder> {

    private Cursor cursor;



    public OgrenciAdaptor(Cursor cursor) {
        this.cursor = cursor;
    }

    public interface OnItemClickListener {
        void onItemClick(int studentId);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.OGR_AD));

        String number = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.OGR_NO));

        String tc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.OGR_TC));

        holder.txtName.setText(name);
        holder.txtNumber.setText("No: " + number);
        holder.txtTc.setText("TC: " + tc);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.OGR_ID));

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(id);
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtNumber, txtTc;
        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txt_list_stu_name);
            txtNumber = itemView.findViewById(R.id.txt_list_stu_number);
            txtTc = itemView.findViewById(R.id.txt_list_stu_tc);
        }
    }
}