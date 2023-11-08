package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.*;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    private static final int PICK_FILE_REQUEST_CODE = 1;
    Button addEntry;
    DatabaseConnection dbConnection;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button nextPage = findViewById(R.id.toList);
        addEntry = findViewById(R.id.addEntryButton);
        dbConnection = new DatabaseConnection(this);

        addEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addPage = new Intent(MainActivity.this, AddEntry.class);
                startActivity(addPage);
            }
        });

        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent next = new Intent(MainActivity.this, SeeAddresses.class);
                next.putExtra("dbConnection", dbConnection);
                startActivity(next);
            }
        });

        Button importButton = findViewById(R.id.toImportList);
        importButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importFromFile();
                Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
            }//onClick method

        });


    }//onCreate

    public void importFromFile() {
        // Read and import file here
        InputStream inputStream = getResources().openRawResource(R.raw.long_and_lat);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                double latitude = Double.parseDouble(parts[0]);
                double longitude = Double.parseDouble(parts[1]);

                ContentValues values = new ContentValues();
                values.put(DatabaseConnection.item_Address, getCompleteAddress(latitude, longitude));
                values.put(DatabaseConnection.item_Latitude, latitude);
                values.put(DatabaseConnection.item_Longitude, longitude);

                long newRowId = dbConnection.getWritableDatabase().insert(DatabaseConnection.GeoDatabase, null, values);
            }//while loop
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//finally
    }
    public String getCompleteAddress(double latitude, double longitude) {
        String address = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try{
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null){
                Address returnedAddress = addresses.get(0);
                StringBuilder strToReturn = new StringBuilder("");

                for (int i=0; i <= returnedAddress.getMaxAddressLineIndex(); i++){
                    strToReturn.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                address = strToReturn.toString();
            }//if statement
        }catch (Exception e){
            e.printStackTrace();
        }
        return address;
    }


}//MainActivity