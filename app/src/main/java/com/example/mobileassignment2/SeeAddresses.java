package com.example.mobileassignment2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SeeAddresses extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerViewAdapter adapter;
    DatabaseConnection dbConnection;
    Button addEntryButton;
    private EditText searchBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_addresses);


        recyclerView = findViewById(R.id.recyclerViewInSeeAddress);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbConnection = new DatabaseConnection(this);
        List<GeoLocation> locationsList = dbConnection.getAllLocations();

        adapter = new RecyclerViewAdapter(this, locationsList);
        recyclerView.setAdapter(adapter);


        searchBox = findViewById(R.id.searchEditText);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString().trim();
                if (!query.isEmpty()){
                    List<GeoLocation> filteredList = dbConnection.searchQuery(query);
                    adapter.setLocationslist(filteredList);
                    adapter.notifyDataSetChanged();
                } else {
                    List<GeoLocation> allLocations = dbConnection.getAllLocations();
                    adapter.setLocationslist(allLocations);
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        addEntryButton = findViewById(R.id.addNewAddressButton);
        addEntryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addNewEntry = new Intent(SeeAddresses.this, AddEntry.class);
                startActivity(addNewEntry);
            }
        });


    }


}
