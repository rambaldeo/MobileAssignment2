package com.example.mobileassignment2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<GeoLocation> locationsList;
    private Context context;

    public RecyclerViewAdapter(Context context, List<GeoLocation> locationsList) {
        this.context = context;
        this.locationsList = locationsList;
    }

    public void setLocationslist(List<GeoLocation> locationsList){
        this.locationsList = locationsList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GeoLocation location = locationsList.get(position);
        holder.addressOutput.setText(location.getAddress());
        holder.coords.setText("Latitude: " + location.getLatitude() + ", Longitude: " + location.getLongitude());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openAddress = new Intent(context, ViewClickedAddress.class);
                openAddress.putExtra("GeoLocation", location);
                context.startActivity(openAddress);
            }
        });
    }//onBindViewHolder

    @Override
    public int getItemCount() {
        return locationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView addressOutput;
        public TextView coords;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            addressOutput = itemView.findViewById(R.id.addressOutput);
            coords = itemView.findViewById(R.id.coords);
        }
    }
}


