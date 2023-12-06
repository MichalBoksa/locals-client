package com.example.locals.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.locals.R;
import com.example.locals.models.Booking;
import com.example.locals.models.Favorites;
import com.example.locals.models.Guide;
import com.example.locals.models.User;
import com.example.locals.retrofit.BookingApi;
import com.example.locals.retrofit.FavoritesApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.PKCE;

import java.sql.Date;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddGuideBookingFragment extends DialogFragment {
    private ImageView closeFragment;
    private TextView dateTV;
    private TextView hourTV;
    private EditText messageET;
    private Button makeBookingBtn;
    private Booking booking;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private RetrofitService retrofit;
    private int hour;
    private int minute;
    private Date date;
    Calendar calendar;
    private TimePickerDialog timePickerDialog;
    int guide_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_add_guide_booking,container,false);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        guide_id = getArguments().getInt("GUIDE_ID");
        booking = new Booking();
        calendar = Calendar.getInstance();
        closeFragment = view.findViewById(R.id.closeCreateLocalsBooking);
        dateTV = (TextView) view.findViewById(R.id.dateCreateLocalsBookingFragmentTV);
        hourTV = (TextView) view.findViewById(R.id.hourCreateLocalsBookingFragmentTV);
        makeBookingBtn = (Button) view.findViewById(R.id.CreateLocalsBookingFragmentBTN);
        messageET = (EditText) view.findViewById(R.id.messageCreateLocalsBookingET);

        setOnClickListeners();
        return view;
    }

    public void setOnClickListeners() {
        closeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        dateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(
                        AddGuideBookingFragment.this.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog,
                        dateSetListener,
                        year,month,day
                );

                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                date = Date.valueOf(year + "-" + month + "-" + day);
                dateTV.setText(year + "-" + month + "-" + day);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,day);
                calendar.set(Calendar.YEAR,year);

            }
        };

        hourTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        hour = selectedHour;
                        minute = selectedMinute;
                        hourTV.setText(String.format(Locale.getDefault(),"%02d:%02d",hour,minute));
                        calendar.set(Calendar.HOUR,hour);
                        calendar.set(Calendar.MINUTE,minute);
                    }
                };

                TimePickerDialog timePickerDialog = new TimePickerDialog(AddGuideBookingFragment.this.getContext(),timeSetListener,hour,minute,true);
                timePickerDialog.setTitle("Select time");
                timePickerDialog.show();
            }
        });

        makeBookingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO change user
                User user = new User();
                user.setEmail("ASD@op.pl");
                user.setId(4);
                booking.setGuideId(guide_id); //setGuide(new Guide(guide_id));
                booking.setUserId(user.getId());// setUser(user);
                booking.setMessage(messageET.getText().toString());
                date.setTime(TimeUnit.MINUTES.toMillis(hour * 60 + minute));
                booking.setDate(calendar.getTime());
                RScall(booking);
            }
        });
    }
    private void RScall(Booking booking) {
        final Call<Void> addNewList = retrofit
                .getRetrofit()
                .create(BookingApi.class)
                .addBooking("Bearer " + PKCE.getAccessToken(this.getContext()),booking);

        addNewList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(call);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(AddGuideBookingFragment.this.getActivity(), "call error",Toast.LENGTH_LONG).show();

            }
        });
    }
}
