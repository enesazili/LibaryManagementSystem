package com.example.kutuphanesistemi; // Paket adını kontrol et

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class OgrenciEkle extends AppCompatActivity {

    private EditText editName, editNumber, editTc;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int incomingStudentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ogrenci_ekle);

        dbHelper = new DatabaseHelper(this);
        editName = findViewById(R.id.edit_student_name);
        editNumber = findViewById(R.id.edit_student_number);
        editTc = findViewById(R.id.edit_student_tc);
        btnSave = findViewById(R.id.btn_save_student);

        incomingStudentId = getIntent().getIntExtra("STUDENT_ID", -1);

        if (incomingStudentId != -1) {
            prepareForUpdate(); 
        }

        btnSave.setOnClickListener(v -> {
            if (incomingStudentId == -1) {
                String isim = editName.getText().toString();
                String numara = editNumber.getText().toString();
                String tc = editTc.getText().toString();

                if (!isim.isEmpty() && !numara.isEmpty() && !tc.isEmpty()) {
                    long id = dbHelper.ogrenciEkle(isim, numara, tc);

                    if (id != -1) {
                        Toast.makeText(this, "Öğrenci Başarıyla Eklendi!", Toast.LENGTH_SHORT).show();
                        finish(); 
                    } else {
                        Toast.makeText(this, "Hata: Kayıt yapılamadı!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                ogrenciyiGuncelle();
            }
        });

    }
    private void prepareForUpdate() {
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "SELECT * FROM " + DatabaseHelper.OGR_TABLOSU + " WHERE " + DatabaseHelper.OGR_ID + " = ?",
                new String[]{String.valueOf(incomingStudentId)});

        if (cursor.moveToFirst()) {
            editName.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.OGR_AD)));
            editNumber.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.OGR_NO)));
            editTc.setText(cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.OGR_TC)));
            btnSave.setText("BİLGİLERİ GÜNCELLE");
        }
    }

    private void ogrenciyiGuncelle() {
        dbHelper.ogrenciGuncelle(incomingStudentId,
                editName.getText().toString(),
                editNumber.getText().toString(),
                editTc.getText().toString());

        Toast.makeText(this, "Güncellendi!", Toast.LENGTH_SHORT).show();
        finish();
    }
}