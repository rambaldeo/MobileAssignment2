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

public class ViewClickedAddress extends AppCompatActivity {

    private TextView address, longitude, latitude, updatedAddress, updatedLong, updatedLat;
    Button update, delete, cancel;
    private DatabaseConnection db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_clicked_address);
        address = findViewById(R.id.addressFromDB);
        longitude = findViewById(R.id.longitudeFromDB);
        latitude = findViewById(R.id.latitudeFromDB);
        cancel = findViewById(R.id.toList);
        delete = findViewById(R.id.deleteFromDB);
        update = findViewById(R.id.updateTheDB);
        db = new DatabaseConnection(this);



        Intent intent = getIntent();
        GeoLocation clickedAddress = (GeoLocation) intent.getSerializableExtra("GeoLocation");
        if (intent != null && intent.hasExtra("GeoLocation")){
        address.setText(clickedAddress.getAddress());
        longitude.setText(String.valueOf(clickedAddress.getLongitude()));
        latitude.setText(String.valueOf(clickedAddress.getLatitude()));
        }

        //To cancel and go back to the list
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toListPage = new Intent(ViewClickedAddress.this, SeeAddresses.class);
                startActivity(toListPage);
            }
        });


        //to delete the address
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteItem(clickedAddress.getId());
                Intent toListPage = new Intent(ViewClickedAddress.this, SeeAddresses.class);
                Toast.makeText(ViewClickedAddress.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                startActivity(toListPage);
            }
        });

        //To update the note
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatedAddress.findViewById(R.id.addressFromDB);
                String userEnteredAddress = String.valueOf(updatedAddress);
                updatedLat.findViewById(R.id.latitudeFromDB);
                double LatValue = Double.parseDouble(String.valueOf(updatedLat));
                updatedLong.findViewById(R.id.longitudeFromDB);
                double LongValue = Double.parseDouble(String.valueOf(updatedLong));
                //if the address has been changed, find the new coordinates
                // Inside your if statement where the address has changed
                if(userEnteredAddress != String.valueOf(address)){
                    updateCoordinatesFromAddress(clickedAddress.getId(), userEnteredAddress);
                }

                //if the coordinates has been changed
                if (longitude != updatedLong || latitude != updatedLat){
                    String newAddress = getUpdatedAddress(LatValue, LongValue);
                    db.updateItem(clickedAddress.getId(), newAddress, LatValue, LongValue);
                }
                Intent toListPage = new Intent(ViewClickedAddress.this, SeeAddresses.class);
                Toast.makeText(ViewClickedAddress.this, "Successfully Updated", Toast.LENGTH_SHORT).show();
                startActivity(toListPage);
            }
        });

    }

    private void updateCoordinatesFromAddress(long id, String newAddress) {
        long ItemID = id;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(newAddress, 1);
            if (addresses != null && addresses.size() > 0) {
                double newLatitude = addresses.get(0).getLatitude();
                double newLongitude = addresses.get(0).getLongitude();

                db.updateItem(id, newAddress, newLatitude, newLongitude);
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getUpdatedAddress(double latitude, double longitude) {
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