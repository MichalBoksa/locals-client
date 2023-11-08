package com.example.locals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locals.R;
import com.example.locals.activities.GuideDetails;

import java.util.List;

public class BulletListAdapter extends RecyclerView.Adapter<BulletListAdapter.ViewHolder> {

    List<String> valueList;
    Context context;

    public BulletListAdapter(Context context, List<String> valueList) {
        this.context = context;
        this.valueList = valueList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_bullet_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.value.setText(valueList.get(position));
    }

    @Override
    public int getItemCount() {
        return valueList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

    TextView value;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            value = itemView.findViewById(R.id.valueBulletList);
        }
    }
}
