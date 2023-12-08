package com.example.locals.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locals.R;
import com.example.locals.activities.PlaceDetails;
import com.example.locals.models.LocationDetails;
import com.example.locals.models.LocationSearch;
import com.example.locals.models.Place;

import java.util.List;

public class PlaceHomeAdapter extends RecyclerView.Adapter<PlaceHomeAdapter.CitiesHomeViewHolder> {

   private Context context;
   private List<LocationDetails> placeList;

    public PlaceHomeAdapter(Context context, List<LocationDetails> placeList) {
        this.context = context;
        this.placeList = placeList;
    }

    @NonNull
    @Override
    public PlaceHomeAdapter.CitiesHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //TODO check this
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_place_home, parent, false);
        return new CitiesHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesHomeViewHolder holder, int position) {
        holder.titleTV.setText(placeList.get(position).getName());
        holder.cityNameTV.setText(placeList.get(position).getAddress_obj().getCity());
        holder.ratingTV.setText(placeList.get(position).getRating().toString());
        Glide.with(holder.itemView.getContext())
                .load(placeList.get(position).getImagesURLList().get(0))
                .into(holder.placeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlaceDetails.class);
                intent.putExtra("LOCATION_ID", getLocationId(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class CitiesHomeViewHolder extends RecyclerView.ViewHolder{
        TextView titleTV;
        TextView cityNameTV;
        TextView ratingTV;
        ImageView placeImage;

        public CitiesHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTVHome);
            cityNameTV = itemView.findViewById(R.id.placeNameTVHome);
            ratingTV = itemView.findViewById(R.id.ratingTVPlaceList);
            placeImage = itemView.findViewById(R.id.placeImageHome);
        }
    }

    private int getLocationId(int position) {
        return placeList.get(position).getLocation_id();
    }
}
