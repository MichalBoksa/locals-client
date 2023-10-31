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
import com.example.locals.activities.PlacesList;
import com.example.locals.models.Favourites;

import java.util.List;

public class FavouritesListAdapter extends RecyclerView.Adapter<FavouritesListAdapter.ViewHolder> {

    Context context;
    List<Favourites> favouritesList;

    public FavouritesListAdapter(Context context, List<Favourites> favouritesList) {
        this.context = context;
        this.favouritesList = favouritesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_favourites_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listNameTV.setText(favouritesList.get(position).getName());
        holder.dateTV.setText(favouritesList.get(position).getDate().toString());
        Glide.with(holder.itemView.getContext())
                .load(favouritesList.get(position).getPlaceList().get(0).getImages().get(0))
                .into(holder.listImage);


        //TODO check what's here
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlacesList.class);
               // intent.putExtra("locationId", holder. )
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favouritesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView listNameTV;
        ImageView listImage;
        TextView dateTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listNameTV = itemView.findViewById(R.id.listNameTVHome);
            dateTV = itemView.findViewById(R.id.dateTVHome);
            listImage = itemView.findViewById(R.id.favouritesImageHome);
        }
    }


}
