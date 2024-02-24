package com.example.locals.fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.locals.R;
import com.example.locals.activities.Home;
import com.example.locals.models.Favorites;
import com.example.locals.models.User;
import com.example.locals.retrofit.FavoritesApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.utils.PKCE;
import com.google.gson.Gson;

import java.sql.Date;
import java.util.Calendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFavoritesListFragment extends DialogFragment {

    private ImageView closeFragment;
    private TextView startDateTV;
    private TextView endDateTV;
    private EditText listNameET;
    private Button addListBtn;
    private DatePickerDialog.OnDateSetListener startDateSetListener;
    private DatePickerDialog.OnDateSetListener endDateSetListener;
    private RetrofitService retrofit;
    private Favorites favorites;
    private User user;
    private Gson gson;
    private SharedPreferences sharedPref;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_favorite_list,container,false);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        favorites = new Favorites();
        gson = new Gson();
        sharedPref = getActivity().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        user = gson.fromJson(sharedPref.getString("USER",null), User.class);
        closeFragment = (ImageView) view.findViewById(R.id.closeAddFavoritesList);
        startDateTV = (TextView) view.findViewById(R.id.startDateAddFavoritesListFragmentTV);
        endDateTV = (TextView) view.findViewById(R.id.endDateAddFavoritesListFragmentTV);
        addListBtn = (Button) view.findViewById(R.id.AddFavoritesListFragmentBTN);
        listNameET = (EditText) view.findViewById(R.id.listNameAddFavoritesListFragmentET);

        setOnClickListeners();

        return view;
    }

    private void setOnClickListeners() {
        closeFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        startDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(
                        AddFavoritesListFragment.this.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog,
                        startDateSetListener,
                        year,month,day
                );

                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();
            }
        });

        endDateTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dateDialog = new DatePickerDialog(
                        AddFavoritesListFragment.this.getContext(),
                        android.R.style.Theme_Holo_Light_Dialog,
                        endDateSetListener,
                        year,month,day
                );

                dateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dateDialog.show();
            }
        });

        addListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorites.setName(listNameET.getText().toString());
                favorites.setUserId(user.getId());
                RScall(favorites);
                dismiss();
            }
        });

        startDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = year + "-" + month + "-" + day;
                favorites.setStartDate(Date.valueOf(date));
                startDateTV.setText(date);

            }
        };

        endDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = year + "-" + month + "-" + day;
                favorites.setEndDate(Date.valueOf(date));
                endDateTV.setText(date);
            }
        };

    }

    private void RScall(Favorites favorites) {
        final Call<Void> addNewList = retrofit
                .getRetrofit()
                .create(FavoritesApi.class)
                .addList("Bearer " + PKCE.getAccessToken(this.getContext()), favorites);

        addNewList.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                System.out.println(response);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(AddFavoritesListFragment.this.getActivity(), "call error",Toast.LENGTH_LONG).show();

            }
        });
    }

}
