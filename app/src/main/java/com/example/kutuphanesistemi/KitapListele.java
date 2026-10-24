package com.example.kutuphanesistemi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class KitapListele extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private KitapAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_listele);

        recyclerView = findViewById(R.id.rv_book_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.KITAP_TABLOSU, null);
        adapter = new KitapAdapter(cursor);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(bookId -> {
            Intent intent = new Intent(KitapListele.this, KitapEkle.class);
            intent.putExtra("BOOK_ID", bookId); 
            startActivity(intent);
        });
    }
}