package com.example.locals.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locals.R;
import com.example.locals.activities.FavoritesListDetails;
import com.example.locals.activities.GuideDetails;
import com.example.locals.activities.UserProfile;
import com.example.locals.fragments.UpdateLanguagesFragment;
import com.example.locals.models.Booking;
import com.example.locals.models.Guide;
import com.example.locals.models.User;
import com.example.locals.retrofit.BookingApi;
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
//TODO Delete if manage authorities, cuz boilerplate code

public class BookingListGuideAdapter extends RecyclerView.Adapter<BookingListGuideAdapter.ViewHolder>{
    private List<Booking> bookings;
    private Context context;
    private User user;
    private RetrofitService retrofit;
    private RetrofitService retrofitAuth;
    private Guide guide;
    private String accessCode;

    public BookingListGuideAdapter(Context context, List<Booking> valueList, User user) {
        this.context = context;
        this.bookings = valueList;
        this.user = user;
    }

    @NonNull
    @Override
    public BookingListGuideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_booking_guide_list,parent,false);
        accessCode = PKCE.getAccessToken(context);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        return new BookingListGuideAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingListGuideAdapter.ViewHolder holder, int position) {
        StringBuffer buffer = new StringBuffer();
        userCall(bookings.get(position).getUserId(),holder,position);
        buffer.append(
                Utils.fromDateToLocalDate(bookings.get(position).getDate()));
        holder.dateTV.setText(buffer.toString());
        holder.nameTV.setText("John");
        holder.messageTV.setText(bookings.get(position).getMessage());

        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RSCallAcceptBooking(bookings.get(holder.getAdapterPosition()).getId());
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
        Button acceptBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.dateBookingList);
            nameTV = itemView.findViewById(R.id.usernameBookingList);
            messageTV = itemView.findViewById(R.id.messageBookingListTV);
            acceptBtn = itemView.findViewById(R.id.buttonAcceptBookingGuideList);
        }

    }

    public void userCall(int id,@NonNull BookingListGuideAdapter.ViewHolder holder, int position) {


        final Call<User> getUser = retrofitAuth
                .getRetrofit()
                .create(UserApi.class)
                .getUserId("Bearer " + accessCode,id);

        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null ){
                    holder.nameTV.setText(response.body().getName());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(context, "Guide booking list call error",Toast.LENGTH_LONG).show();            }
        });

    }

    private void RSCallAcceptBooking(int id) {

        final Call<Void> addNewList = retrofit
                .getRetrofit()
                .create(BookingApi.class)
                .updateBooking("Bearer " + accessCode, id);

        addNewList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "You've just accepted booking!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(context, "call error",Toast.LENGTH_LONG).show();
            }
        });
    }

}
