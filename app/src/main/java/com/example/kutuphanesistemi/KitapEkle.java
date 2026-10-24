package com.example.kutuphanesistemi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class KitapEkle extends AppCompatActivity {

    private EditText editTitle, editAuthor, editCount, editIsbn;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int incomingBookId = -1; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kitap_ekle);

        dbHelper = new DatabaseHelper(this);
        editTitle = findViewById(R.id.edit_book_title);
        editAuthor = findViewById(R.id.edit_book_author);
        editCount = findViewById(R.id.edit_book_count);
        editIsbn = findViewById(R.id.edit_book_isbn);
        btnSave = findViewById(R.id.btn_save_book);

        incomingBookId = getIntent().getIntExtra("BOOK_ID", -1);

        if (incomingBookId != -1) {
            kitapBilgileriniDoldur(); 
        }

        btnSave.setOnClickListener(v -> {
            if (incomingBookId == -1) {
                kitapKaydet(); 
            } else {
                kitapGuncelle(); 
            }
        });
    }
    private void kitapBilgileriniDoldur() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHelper.KITAP_TABLOSU + " WHERE " + DatabaseHelper.KITAP_ID + " = ?",
                new String[]{String.valueOf(incomingBookId)});

        if (cursor.moveToFirst()) {
            editTitle.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KITAP_AD)));
            editAuthor.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KITAP_YAZAR)));
            editCount.setText(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.KITAP_ADET))));
            editIsbn.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.KITAP_ISBN)));

            btnSave.setText("KİTABI GÜNCELLE"); 
        }
        cursor.close();
    }

    private void kitapKaydet() {
        String ad = editTitle.getText().toString();
        String yazar = editAuthor.getText().toString();
        String adetStr = editCount.getText().toString();
        String isbn = editIsbn.getText().toString();

        if (!ad.isEmpty() && !adetStr.isEmpty()) {
            int adet = Integer.parseInt(adetStr);

            long id = dbHelper.kitapEkle(ad, yazar, adet, isbn);

            if (id != -1) {
                Toast.makeText(this, "Kitap Başarıyla Eklendi!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Hata: Kitap eklenemedi!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Kitap adı ve adet zorunludur!", Toast.LENGTH_SHORT).show();
        }
    }

    private void kitapGuncelle() {
        String ad = editTitle.getText().toString();
        String yazar = editAuthor.getText().toString();
        int adet = Integer.parseInt(editCount.getText().toString());
        String isbn = editIsbn.getText().toString();

        dbHelper.kitapGuncelle(incomingBookId, ad, yazar, adet, isbn);
        Toast.makeText(this, "Kitap güncellendi!", Toast.LENGTH_SHORT).show();
        finish();
    }
}