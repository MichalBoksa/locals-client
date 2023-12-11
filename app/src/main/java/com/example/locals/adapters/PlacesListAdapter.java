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
import com.example.locals.models.Place;

import java.util.List;

public class PlacesListAdapter extends RecyclerView.Adapter<PlacesListAdapter.ViewHolder> {

    Context context;
    List<LocationDetails> placeList;

    public PlacesListAdapter(Context context, List<LocationDetails> placeList) {
        this.context = context;
        this.placeList = placeList;
    }


    @NonNull
    @Override
    public PlacesListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //TODO check this
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_place_list, parent, false);
        return new PlacesListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesListAdapter.ViewHolder holder, int position) {
        holder.placeNameTV.setText(placeList.get(position).getName());
        holder.cityNameTV.setText(placeList.get(position).getAddress_obj().getCity());
        holder.ratingTV.setText(placeList.get(position).getRating().toString());
        holder.opinionsNumberTV.setText(placeList.get(position).getNum_reviews().toString());
        Glide.with(holder.itemView.getContext())
                .load(placeList.get(position).getImagesURLList().get(0))
                .into(holder.placeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlaceDetails.class);
                intent.putExtra("LOCATION_ID",placeList.get(holder.getAdapterPosition()).getLocation_id());
                intent.putExtra("CITY_NAME", placeList.get(holder.getAdapterPosition()).getAddress_obj().getCity());
                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return placeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView placeNameTV;
        TextView cityNameTV;
        TextView ratingTV;
        TextView opinionsNumberTV;
        ImageView placeImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            placeNameTV = itemView.findViewById(R.id.placeNameTVPlaceList);
            cityNameTV = itemView.findViewById(R.id.placeCityTVPlaceList);
            ratingTV = itemView.findViewById(R.id.ratingTVPlaceList);
            placeImage = itemView.findViewById(R.id.placeImagePlaceList);
            opinionsNumberTV = itemView.findViewById(R.id.opinionsNumberPlaceListTV);
        }
    }
}
