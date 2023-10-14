package com.example.locals.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locals.R;
import com.example.locals.models.CitiesHomeData;

import java.util.List;

public class CitiesHomeAdapter extends RecyclerView.Adapter<CitiesHomeAdapter.CitiesHomeViewHolder> {

    Context context;
    List<CitiesHomeData> cityList;

    @NonNull
    @Override
    public CitiesHomeAdapter.CitiesHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //TODO check this
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_city_home, parent, false);
        return new CitiesHomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CitiesHomeViewHolder holder, int position) {
        holder.titleTV.setText(cityList.get(position).getCityName());
        holder.cityNameTV.setText(cityList.get(position).getCityName());
        holder.ratingTV.setText(cityList.get(position).getRating().toString());
        Glide.with(holder.itemView.getContext())
                .load(cityList.get(position).getImages().get(0))
                .into(holder.cityImage);
    }


    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class CitiesHomeViewHolder extends RecyclerView.ViewHolder{
        TextView titleTV;
        TextView cityNameTV;
        TextView ratingTV;
        ImageView cityImage;

        public CitiesHomeViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTV = itemView.findViewById(R.id.titleTVHome);
            cityNameTV = itemView.findViewById(R.id.citynNameTVHome);
            ratingTV = itemView.findViewById(R.id.citynNameTVHome);
            cityImage = itemView.findViewById(R.id.cityImageHome);
        }
    }
}
