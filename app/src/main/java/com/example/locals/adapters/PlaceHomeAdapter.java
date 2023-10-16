package com.example.locals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locals.R;
import com.example.locals.models.Place;

import java.util.List;

public class PlaceHomeAdapter extends RecyclerView.Adapter<PlaceHomeAdapter.CitiesHomeViewHolder> {

    Context context;
    List<Place> placeList;

    public PlaceHomeAdapter(Context context, List<Place> placeList) {
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
        holder.titleTV.setText(placeList.get(position).getPlaceName());
        holder.cityNameTV.setText(placeList.get(position).getPlaceName());
        holder.ratingTV.setText(placeList.get(position).getRating().toString());
        Glide.with(holder.itemView.getContext())
                .load(placeList.get(position).getImages().get(0))
                .into(holder.placeImage);
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
            ratingTV = itemView.findViewById(R.id.ratingTVHome);
            placeImage = itemView.findViewById(R.id.placeImageHome);
        }
    }
}
