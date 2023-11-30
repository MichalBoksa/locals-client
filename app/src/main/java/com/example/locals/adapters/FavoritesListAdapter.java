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
import com.example.locals.activities.FavoritesListDetails;
import com.example.locals.activities.PlacesList;
import com.example.locals.models.Favorites;
import com.example.locals.utils.Utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.ViewHolder> {

    Context context;
    List<Favorites> favoritesList;
    List<String> locationList;

    public FavoritesListAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_favourites_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StringBuffer buffer = new StringBuffer();

        if(favoritesList.get(position).getPlaceIds() != null && !favoritesList.get(position).getPlaceIds().isEmpty()) {
            holder.placesNumberTV.setText(String.valueOf(Arrays.asList(favoritesList.get(position).getPlaceIds().split(",")).size()));
        }
        else {
            holder.placesNumberTV.setText("0");
        }
        holder.listNameTV.setText(favoritesList.get(position).getName());
        buffer.append(
               Utils.fromDateToLocalDate(favoritesList.get(position).getStartDate()))
               .append(" - ")
               .append(
                Utils.fromDateToLocalDate(favoritesList.get(position).getEndDate()));

        holder.dateTV.setText(buffer.toString());

        Glide.with(holder.itemView.getContext())
                .load(favoritesList.get(position).getListImageUrl())
                .into(holder.listImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FavoritesListDetails.class);
                intent.putExtra("LOCATION_ID",favoritesList.get(holder.getAdapterPosition()).getPlaceIds());
                intent.putExtra("LIST_NAME",favoritesList.get(holder.getAdapterPosition()).getName());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView listNameTV;
        TextView placesNumberTV;
        ImageView listImage;
        TextView dateTV;
        TextView ifListEmptyFavoritesListTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listNameTV = itemView.findViewById(R.id.listNameTVHome);
            dateTV = itemView.findViewById(R.id.dateTVHome);
            listImage = itemView.findViewById(R.id.favouritesImageHome);
            placesNumberTV = itemView.findViewById(R.id.savedPlacesNumberHome);
            ifListEmptyFavoritesListTV = itemView.findViewById(R.id.ifListEmptyFavoritesListTV);
        }
    }
}
