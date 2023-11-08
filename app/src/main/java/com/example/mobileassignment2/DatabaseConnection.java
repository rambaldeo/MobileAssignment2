package com.example.mobileassignment2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseConnection extends SQLiteOpenHelper implements Serializable {

public static final String GeoDatabase = "GeoLocationDatabase";
public static final String item_ID = "ID";
public static String item_Address = "ADDRESS";
public static String item_Latitude = "LATITUDE";
public static String item_Longitude = "LONGITUDE";
public static String item_added = "ADDBUTTON";
SQLiteDatabase GeoDB;
Cursor cursor;

    public DatabaseConnection(@Nullable Context context) {
        super(context, "GeoLocation.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + GeoDatabase + "(" + item_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + item_Address + " TEXT, " + item_Latitude + " REAL, " + item_Longitude + " REAL)";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //Retrieve the information
    public List<GeoLocation> getAllLocations() {
        List<GeoLocation> locationsList = new ArrayList<>();

        String query = "SELECT * FROM " + GeoDatabase;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String address = cursor.getString(cursor.getColumnIndex(item_Address));
                @SuppressLint("Range") double latitude = cursor.getDouble(cursor.getColumnIndex(item_Latitude));
                @SuppressLint("Range") double longitude = cursor.getDouble(cursor.getColumnIndex(item_Longitude));

                GeoLocation location = new GeoLocation(address, latitude, longitude);
                locationsList.add(location);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return locationsList;
    }

    public List<GeoLocation> searchQuery() {
        List <GeoLocation> returnList = new ArrayList<>();


        return returnList;
    }

    public void deleteItem(long id) {
        GeoDB = this.getWritableDatabase();
        GeoDB.delete(GeoDatabase, item_ID + "=?", new String[]{String.valueOf(id)});
    }


    public void updateItem(long id, String newAddress, Double updatedLat, Double updatedLong) {
        GeoDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(item_Address, newAddress);
        values.put(item_Latitude, updatedLat);
        values.put(item_Longitude, updatedLong);

        GeoDB.update(GeoDatabase, values, item_ID + "=?", new String[]{String.valueOf(id)});
    }

    public void addEntry(String newAddress, double newLatitude, double newLongitude) {
        GeoDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(item_Address, newAddress);
        values.put(item_Longitude, newLongitude);
        values.put(item_Latitude, newLatitude);

        GeoDB.insert(GeoDatabase, null, values);
    }

    public List<GeoLocation> searchQuery(String query) {
        List<GeoLocation> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();

        String selection = item_Address + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        Cursor cursor = db.query(GeoDatabase, null, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String address = cursor.getString(cursor.getColumnIndex(item_Address));
                double latitude = cursor.getDouble(cursor.getColumnIndex(item_Latitude));
                double longitude = cursor.getDouble(cursor.getColumnIndex(item_Longitude));

                GeoLocation location = new GeoLocation(address, latitude, longitude);
                returnList.add(location);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }
}
