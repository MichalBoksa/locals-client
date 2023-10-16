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
import com.example.locals.models.City;

import java.util.List;


public class CityHomeAdapter extends RecyclerView.Adapter<CityHomeAdapter.ViewHolder> {
    Context context;
    List<City> cityList;

    public CityHomeAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_city_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.cityNameTV.setText(cityList.get(position).getCityName());
        Glide.with(holder.itemView.getContext())
                .load(cityList.get(position).getImageURL())
                .into(holder.cityImage);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView cityNameTV;
        ImageView cityImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cityNameTV = itemView.findViewById(R.id.cityNameTVHome);
            cityImage = itemView.findViewById(R.id.cityImageHome);
        }
    }
}
