package com.example.locals.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locals.R;
import com.example.locals.activities.FavoritesListDetails;
import com.example.locals.activities.GuideDetails;
import com.example.locals.activities.UserProfile;
import com.example.locals.models.Booking;
import com.example.locals.models.Guide;
import com.example.locals.models.User;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;
import com.example.locals.utils.Utils;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BookingListAdapter extends RecyclerView.Adapter<BookingListAdapter.ViewHolder>{
    private List<Booking> bookings;
    private Context context;
    private User user;
    private RetrofitService retrofit;
    private Guide guide;

    public BookingListAdapter(Context context, List<Booking> valueList, User user) {
        this.context = context;
        this.bookings = valueList;
        this.user = user;
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
        RSGuideCall(bookings.get(position).getGuideId(),holder,position);
        buffer.append(
                Utils.fromDateToLocalDate(bookings.get(position).getDate()));
        holder.dateTV.setText(buffer.toString());
        holder.nameTV.setText("John");
        holder.messageTV.setText(bookings.get(position).getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GuideDetails.class);
                intent.putExtra("GUIDE_ID", bookings.get(holder.getAdapterPosition()).getGuideId());
                context.startActivity(intent);
            }
        });
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
            nameTV = itemView.findViewById(R.id.guideNameBookingList);
            messageTV = itemView.findViewById(R.id.messageBookingListTV);
        }
    }

    public void RSGuideCall(int id,@NonNull BookingListAdapter.ViewHolder holder, int position) {
        String accessCode = PKCE.getAccessToken(context);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();

       final Call<Guide> getGuide = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .getGuideDetails("Bearer " + accessCode,id);

        getGuide.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                if (response.body() != null ){
                    holder.nameTV.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(context, "Guide booking list call error",Toast.LENGTH_LONG).show();            }
        });

    }

}
