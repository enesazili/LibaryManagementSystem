package com.example.kutuphanesistemi;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Kutuphane.db";
    private static final int DATABASE_VERSION = 1;

    public static final String OGR_TABLOSU = "ogrenci";
    public static final String OGR_ID = "id";
    public static final String OGR_AD = "isim";
    public static final String OGR_NO = "numara";
    public static final String OGR_TC = "tc";

    public static final String KITAP_TABLOSU = "kitap";
    public static final String KITAP_ID = "id";
    public static final String KITAP_AD = "kitapIsim";
    public static final String KITAP_YAZAR = "yazar";
    public static final String KITAP_ADET = "adet";
    public static final String KITAP_ISBN = "isbn";

    public static final String ODUNC_TABLOSU = "odunc";
    public static final String ODUNC_ID = "id";
    public static final String ODUNC_OGR_ID = "ogrId";
    public static final String ODUNC_KITAP_ID = "kitapId";
    public static final String ODUNC_VERILEN = "verilenTarih";
    public static final String ODUNC_KALAN = "kalanGun";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + OGR_TABLOSU + " (" + OGR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + OGR_AD + " TEXT, " + OGR_NO + " TEXT, " + OGR_TC + " TEXT)");
        db.execSQL("CREATE TABLE " + KITAP_TABLOSU + " (" + KITAP_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KITAP_AD + " TEXT, " + KITAP_YAZAR + " TEXT, " + KITAP_ADET + " INTEGER, " + KITAP_ISBN + " TEXT)");
        db.execSQL("CREATE TABLE " + ODUNC_TABLOSU + " (" + ODUNC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + ODUNC_OGR_ID + " INTEGER, " + ODUNC_KITAP_ID + " INTEGER, " + ODUNC_VERILEN + " TEXT, " + ODUNC_KALAN + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ODUNC_TABLOSU);
        db.execSQL("DROP TABLE IF EXISTS " + KITAP_TABLOSU);
        db.execSQL("DROP TABLE IF EXISTS " + OGR_TABLOSU);
        onCreate(db);
    }

    public long ogrenciEkle(String ad, String no, String tc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(OGR_AD, ad); v.put(OGR_NO, no); v.put(OGR_TC, tc);
        return db.insert(OGR_TABLOSU, null, v);
    }

    public void ogrenciGuncelle(int id, String ad, String no, String tc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(OGR_AD, ad); v.put(OGR_NO, no); v.put(OGR_TC, tc);
        db.update(OGR_TABLOSU, v, OGR_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public long kitapEkle(String ad, String yazar, int adet, String isbn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KITAP_AD, ad); v.put(KITAP_YAZAR, yazar); v.put(KITAP_ADET, adet); v.put(KITAP_ISBN, isbn);
        return db.insert(KITAP_TABLOSU, null, v);
    }

    public void kitapGuncelle(int id, String ad, String yazar, int adet, String isbn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(KITAP_AD, ad); v.put(KITAP_YAZAR, yazar); v.put(KITAP_ADET, adet); v.put(KITAP_ISBN, isbn);
        db.update(KITAP_TABLOSU, v, KITAP_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public long oduncVer(int ogrId, int ktpId, String tarih, String sure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(ODUNC_OGR_ID, ogrId); v.put(ODUNC_KITAP_ID, ktpId); v.put(ODUNC_VERILEN, tarih); v.put(ODUNC_KALAN, sure);
        return db.insert(ODUNC_TABLOSU, null, v);
    }

    public void oduncGuncelle(int id, int ogrId, int ktpId, String sure) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(ODUNC_OGR_ID, ogrId); v.put(ODUNC_KITAP_ID, ktpId); v.put(ODUNC_KALAN, sure);
        db.update(ODUNC_TABLOSU, v, ODUNC_ID + " = ?", new String[]{String.valueOf(id)});
    }
}