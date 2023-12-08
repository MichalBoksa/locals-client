package com.example.locals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locals.R;
import com.example.locals.models.Favorites;

import java.util.ArrayList;
import java.util.List;

public class AddSingleElementToFavoriteListAdapter extends RecyclerView.Adapter<AddSingleElementToFavoriteListAdapter.ViewHolder> {
   private List<Favorites> favoritesList;
   private Context context;
   private List<Integer> checkedIds;

    public AddSingleElementToFavoriteListAdapter(List<Favorites> favoritesList, Context context) {
        this.favoritesList = favoritesList;
        this.context = context;
    }

    @NonNull
    @Override
    public AddSingleElementToFavoriteListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_favorites_single_item,parent,false);
        checkedIds = new ArrayList<>();
        return new AddSingleElementToFavoriteListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AddSingleElementToFavoriteListAdapter.ViewHolder holder, int position) {
        holder.listNameTV.setText(favoritesList.get(position).getName());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkedIds.add(favoritesList.get(holder.getAdapterPosition()).getId());
                } else {
                    checkedIds.remove(holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public List<Integer> getCheckedIds() {
        return checkedIds;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView listNameTV;
        CheckBox checkBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            listNameTV = itemView.findViewById(R.id.listNameSingleFavoritesItemAddTV);
            checkBox = itemView.findViewById(R.id.checkBoxSingleFavoritesItemAdd);
        }
    }

}
