package com.example.kutuphanesistemi;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class OduncEkle extends AppCompatActivity {

    private Spinner spinnerStudents, spinnerBooks;
    private EditText editDuration;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private List<Integer> studentIds = new ArrayList<>();
    private List<Integer> bookIds = new ArrayList<>();
    private int incomingLoanId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odunc_ekle);

        dbHelper = new DatabaseHelper(this);
        spinnerStudents = findViewById(R.id.spinner_students);
        spinnerBooks = findViewById(R.id.spinner_books);
        editDuration = findViewById(R.id.edit_loan_duration);
        btnSave = findViewById(R.id.btn_save_loan);

        loadSpinners();

        incomingLoanId = getIntent().getIntExtra("LOAN_ID", -1);
        if (incomingLoanId != -1) {
            prepareUpdate();
        }

        btnSave.setOnClickListener(v -> {
            if (incomingLoanId == -1) kaydet(); else guncelle();
        });
    }

    private void prepareUpdate() {
        Cursor c = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.ODUNC_TABLOSU + " WHERE " + DatabaseHelper.ODUNC_ID + " = ?", new String[]{String.valueOf(incomingLoanId)});
        if (c.moveToFirst()) {
            editDuration.setText(c.getString(c.getColumnIndexOrThrow(DatabaseHelper.ODUNC_KALAN)));
            int sId = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.ODUNC_OGR_ID));
            int bId = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.ODUNC_KITAP_ID));
            spinnerStudents.setSelection(studentIds.indexOf(sId));
            spinnerBooks.setSelection(bookIds.indexOf(bId));
            btnSave.setText("GÜNCELLE");
        }
        c.close();
    }

    private void kaydet() {
        int sId = studentIds.get(spinnerStudents.getSelectedItemPosition());
        int bId = bookIds.get(spinnerBooks.getSelectedItemPosition());
        String tarih = new java.text.SimpleDateFormat("dd/MM/yyyy").format(new java.util.Date());
        dbHelper.oduncVer(sId, bId, tarih, editDuration.getText().toString());
        finish();
    }

    private void guncelle() {
        int sId = studentIds.get(spinnerStudents.getSelectedItemPosition());
        int bId = bookIds.get(spinnerBooks.getSelectedItemPosition());
        dbHelper.oduncGuncelle(incomingLoanId, sId, bId, editDuration.getText().toString());
        finish();
    }

    private void loadSpinners() {
        Cursor s = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.OGR_TABLOSU, null);
        List<String> sNames = new ArrayList<>();
        while (s.moveToNext()) {
            studentIds.add(s.getInt(0));
            sNames.add(s.getString(1));
        }
        spinnerStudents.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sNames));

        Cursor b = dbHelper.getReadableDatabase().rawQuery("SELECT * FROM " + DatabaseHelper.KITAP_TABLOSU, null);
        List<String> bNames = new ArrayList<>();
        while (b.moveToNext()) {
            bookIds.add(b.getInt(0));
            bNames.add(b.getString(1));
        }
        spinnerBooks.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bNames));
    }
}