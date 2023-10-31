package com.example.locals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.locals.R;
import com.example.locals.models.LocationDetails;

import java.util.List;

public class PlaceDetailsImageAdapter extends RecyclerView.Adapter<PlaceDetailsImageAdapter.ViewHolder> {

    Context context;
    List<String> imageURIs;

    public PlaceDetailsImageAdapter(Context context, List<String> imageURIs) {
        this.context = context;
        this.imageURIs = imageURIs;
    }

    @NonNull
    @Override
    public PlaceDetailsImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.viewholder_place_details_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceDetailsImageAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                .load(imageURIs.get(position))
                .into(holder.placeImage);
    }

    @Override
    public int getItemCount() {
        return imageURIs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView placeImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            placeImage = itemView.findViewById(R.id.imageListPlaceDetails);
        }
    }
}
