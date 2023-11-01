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
import com.example.locals.activities.GuideDetails;
import com.example.locals.models.Guide;

import java.util.List;

public class GuideListAdapter extends RecyclerView.Adapter<GuideListAdapter.ViewHolder> {
   private Context context;
   private List<Guide> guideList;

    public GuideListAdapter(Context context, List<Guide> guideList) {
        this.context = context;
        this.guideList = guideList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.viewholder_guide_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.guideName.setText(guideList.get(position).getName());
        holder.guideCity.setText(guideList.get(position).getCity());
        holder.guideDesc.setText(guideList.get(position).getAboutMe());
        holder.guidePrice.setText(guideList.get(position).getPrice().toString());

        Glide.with(holder.itemView.getContext())
                .load(guideList.get(position).getImageURL())
                .into(holder.guideImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GuideDetails.class);
                intent.putExtra("GUIDE_ID", getGuideId(holder.getAdapterPosition()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return guideList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView guideImage;
        TextView guideName;
        TextView guideCity;
        TextView guidePrice;
        TextView guideDesc;
        TextView reviewsNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            guideImage = itemView.findViewById(R.id.guideImageGuideList);
            guideName = itemView.findViewById(R.id.guideNameTVGuideList);
            guideCity = itemView.findViewById(R.id.guideCityTVGuideList);
            guidePrice = itemView.findViewById(R.id.guidePriceTVGuideList);
            guideDesc = itemView.findViewById(R.id.descriptionTVGuideList);
            reviewsNumber = itemView.findViewById(R.id.reviewsNumberTVGuideList);
        }
    }

    private int getGuideId(int position) {
        return guideList.get(position).getId();
    }
}
