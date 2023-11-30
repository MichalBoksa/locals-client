package com.example.locals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locals.R;
import com.example.locals.models.Booking;
import com.example.locals.utils.Utils;

import java.util.List;


public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder>{
    private List<Booking> bookings;
    private Context context;

    public BookingListAdapter(Context context, List<Booking> valueList) {
        this.context = context;
        this.bookings = valueList;
    }

    @NonNull
    @Override
    public BookingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_booking_list,parent,false);
        return new BookingListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingListAdapter.ViewHolder holder, int position) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(
                Utils.fromDateToLocalDate(bookings.get(position).getDate()));
        holder.dateTV.setText(buffer.toString());
        holder.nameTV.setText(bookings.get(position).getUser().getName());
        holder.messageTV.setText(bookings.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return bookings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView dateTV;
        TextView nameTV;
        TextView messageTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateBookingList);
            nameTV = itemView.findViewById(R.id.usernameBookingList);
            messageTV = itemView.findViewById(R.id.messageBookingListTV);
        }
    }
}
