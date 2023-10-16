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
import com.example.locals.models.Favorites;

import java.util.List;

public class FavoritsListAdapter extends RecyclerView.Adapter<FavoritsListAdapter.ViewHolder> {

    Context context;
    List<Favorites> favoritesList;

    public FavoritsListAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_favorites_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.listNameTV.setText(favoritesList.get(position).getName());
        holder.dateTV.setText(favoritesList.get(position).getDate().toString());
        Glide.with(holder.itemView.getContext())
                .load(favoritesList.get(position).getPlaceList().get(0).getImages().get(0))
                .into(holder.listImage);
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView listNameTV;
        ImageView listImage;
        TextView dateTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listNameTV = itemView.findViewById(R.id.listNameTVHome);
            dateTV = itemView.findViewById(R.id.dateTVHome);
            listImage = itemView.findViewById(R.id.favoritesImageHome);
        }
    }
}
