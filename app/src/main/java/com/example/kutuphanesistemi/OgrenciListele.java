package com.example.kutuphanesistemi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OgrenciListele extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private OgrenciAdaptor adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_listele);

        recyclerView = findViewById(R.id.rv_student_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.OGR_TABLOSU, null);

        adapter = new OgrenciAdaptor(cursor);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(studentId -> {
            Intent intent = new Intent(OgrenciListele.this, OgrenciEkle.class);
            intent.putExtra("STUDENT_ID", studentId); 
            startActivity(intent);
        });
    }
}