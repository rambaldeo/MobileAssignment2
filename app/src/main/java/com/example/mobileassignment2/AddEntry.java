package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AddEntry extends AppCompatActivity {
    Button submit, toHomeClick;
    private TextView userEnteredAddress, userEnteredLongitude, userEnteredLatitude;
    DatabaseConnection db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        userEnteredAddress = findViewById(R.id.addressToBeEntered);
        userEnteredLatitude = findViewById(R.id.latitudeToBeEntered);
        userEnteredLongitude = findViewById(R.id.longitudeToBeEntered);
        db = new DatabaseConnection(this);

        submit = findViewById(R.id.addEntryButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newAddress = userEnteredAddress.getText().toString();

                double LongValue = Double.parseDouble(String.valueOf(userEnteredLongitude));
                double LatValue = Double.parseDouble(String.valueOf(userEnteredLatitude));
                if(newAddress != null){
                    getCoordinatesFromAddress(newAddress);
                }else if (userEnteredLatitude != null && userEnteredLongitude != null){
                    getTheAddress(LatValue, LongValue);
                    db.addEntry(getTheAddress(LatValue, LongValue), LatValue, LongValue);
                    Toast toast = Toast.makeText(AddEntry.this, "Address has been added", Toast.LENGTH_SHORT);

                }else {
                    Toast.makeText(AddEntry.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
                Intent backtoHome = new Intent(AddEntry.this, SeeAddresses.class);
                startActivity(backtoHome);

            }//onCLick
        });

        toHomeClick = findViewById(R.id.backToHomeButton);
        toHomeClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(AddEntry.this, SeeAddresses.class);
                startActivity(toHome);
            }
        });


    }


    public void getCoordinatesFromAddress(String newAddress) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(newAddress, 1);
            if (addresses != null && addresses.size() > 0) {
                double newLatitude = addresses.get(0).getLatitude();
                double newLongitude = addresses.get(0).getLongitude();

                db.addEntry(newAddress, newLatitude, newLongitude);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getTheAddress(double latitude, double longitude) {
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
}

