package com.example.kutuphanesistemi;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private DatabaseHelper dbHelper;
    private LoanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(item -> {
            handleMenuClick(item.getItemId());
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        recyclerView = findViewById(R.id.recyclerView_loans);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLoanList();

        findViewById(R.id.fab_add_loan).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, OduncEkle.class));
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                    setEnabled(true);
                }
            }
        });
    }

    private void handleMenuClick(int id) {
        if (id == R.id.nav_add_student) {
            startActivity(new Intent(this, OgrenciEkle.class));
        } else if (id == R.id.nav_student_list) {
            startActivity(new Intent(this, OgrenciListele.class));
        } else if (id == R.id.nav_add_book) {
            startActivity(new Intent(this, KitapEkle.class));
        } else if (id == R.id.nav_book_list) {
            startActivity(new Intent(this, KitapListele.class));
        } else if (id == R.id.nav_add_borrow) {
            startActivity(new Intent(this, OduncEkle.class));
        }
    }

    private void refreshLoanList() {
        String query = "SELECT l." + DatabaseHelper.ODUNC_ID +
                ", s." + DatabaseHelper.OGR_AD + " AS stu_name" +
                ", b." + DatabaseHelper.KITAP_AD + " AS book_title" +
                ", l." + DatabaseHelper.ODUNC_VERILEN + " AS loan_date" +
                ", l." + DatabaseHelper.ODUNC_KALAN + " AS duration_days" +
                " FROM " + DatabaseHelper.ODUNC_TABLOSU + " l " +
                " INNER JOIN " + DatabaseHelper.OGR_TABLOSU + " s ON l." + DatabaseHelper.ODUNC_OGR_ID + " = s." + DatabaseHelper.OGR_ID +
                " INNER JOIN " + DatabaseHelper.KITAP_TABLOSU + " b ON l." + DatabaseHelper.ODUNC_KITAP_ID + " = b." + DatabaseHelper.KITAP_ID;

        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(query, null);
        adapter = new LoanAdapter(cursor);

        adapter.setOnItemClickListener(position -> {
            cursor.moveToPosition(position);
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ODUNC_ID));
            Intent i = new Intent(MainActivity.this, OduncEkle.class);
            i.putExtra("LOAN_ID", id);
            startActivity(i);
        });

        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshLoanList();
    }
}