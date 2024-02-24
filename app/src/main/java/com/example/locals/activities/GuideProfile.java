package com.example.locals.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locals.MainActivity;
import com.example.locals.R;

import com.example.locals.adapters.BookingListGuideAdapter;
import com.example.locals.fragments.UpdateWhatToOfferFragment;
import com.example.locals.fragments.UpdateAboutMeFragment;
import com.example.locals.fragments.UpdateEmailFragment;
import com.example.locals.fragments.UpdateLanguagesFragment;
import com.example.locals.fragments.UpdatePhoneFragment;
import com.example.locals.fragments.UpdatePriceFragment;
import com.example.locals.models.Booking;
import com.example.locals.models.Guide;
import com.example.locals.models.User;
import com.example.locals.retrofit.BookingApi;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;
import com.google.gson.Gson;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuideProfile extends AppCompatActivity {

    private String REDIRECT_URI ="urltocallback://guide_profile";
    private ImageView backArrow;
    private CircleImageView userImage;
    private TextView usernameTV;
    private TextView editEmailTV;
    private TextView editPhoneTV;
    private TextView userIdTV;
    private TextView userEmailTV;
    private TextView userPhoneTV;
    private TextView aboutMeTV;
    private TextView whatToOfferTV;
    private TextView priceTV;
    private TextView languagesTV;
    private TextView logoutTV;
    private TextView deleteTV;
    private String userPhone;
    private RetrofitService retrofit;
    private RetrofitService retrofitAuth;
    private UpdateEmailFragment emailDialogFragment;
    private UpdatePhoneFragment phoneDialogFragment;
    private UpdateAboutMeFragment aboutMeDialogFragment;
    private UpdateWhatToOfferFragment whatToOfferDialogFragment;
    private UpdatePriceFragment priceDialogFragment;
    private UpdateLanguagesFragment languagesDialogFragment;
    private Bundle bundleArgs;

    private RecyclerView bookingRecyclerView;
    private BookingListGuideAdapter bookingAdapter;
    private User user;
    private Guide guide;

    private SharedPreferences sharedPref;
    private  Gson gson;
    private ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {

            if (uri == null)
                Toast.makeText(GuideProfile.this, "No image Selected", Toast.LENGTH_SHORT).show();
            else {

                int gfgFlagger = Intent.FLAG_GRANT_READ_URI_PERMISSION;
                GuideProfile.this.getContentResolver().takePersistableUriPermission(uri, gfgFlagger);
                userImage.setImageURI(uri);
                updateUserImage(uri.toString());
            }
        }
    });

    @Override
    protected void onResume() {
        super.onResume();
        PKCE.AuthorizationTokenResume(this,REDIRECT_URI);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_profile);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        retrofitAuth = new RetrofitService();
        retrofitAuth.initializeRetrofitAuth();
        if(PKCE.isJWTexpired(this)) {
            PKCE.refreshToken(this, REDIRECT_URI);
        }
        backArrow = findViewById(R.id.backArrowGuideProfile);
        userImage = findViewById(R.id.imageGuideProfile);
        usernameTV = findViewById(R.id.usernameGuideProfileTV);
        userIdTV = findViewById(R.id.userIdGuideProfileTV);
        userEmailTV = findViewById(R.id.emailGuideProfile);
        userPhoneTV = findViewById(R.id.phoneNumberGuideProfile);
        editEmailTV = findViewById(R.id.updateEmailGuideProfile);
        editPhoneTV = findViewById(R.id.updatePhoneGuideProfile);
        aboutMeTV = findViewById(R.id.aboutMeGuideProfile);
        whatToOfferTV = findViewById(R.id.whatToOfferGuideProfile);
        priceTV = findViewById(R.id.priceGuideProfile);
        languagesTV = findViewById(R.id.languagesGuideProfile);
        logoutTV = findViewById(R.id.logoutGuideProfile);
        deleteTV =findViewById(R.id.deleteAccountGuideProfile);
        bundleArgs = new Bundle();
        gson = new Gson();
        sharedPref = GuideProfile.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        user = gson.fromJson(sharedPref.getString("USER",null), User.class);

        //TODO delete maybe later
        setUserData();
        setGuideData();
        setBookings();
        setOnClickListeners();
    }

    private void setUserData() {
        //TODO change to sharedprefs
        String accessCode = PKCE.getAccessToken(this);
        final Call<User> getUser = retrofitAuth
                .getRetrofit()
                .create(UserApi.class)
                .getUser("Bearer " + accessCode, user.getEmail());

        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null ) {
                    usernameTV.setText(response.body().getName());
                    userEmailTV.setText(response.body().getEmail());
                    userPhone = response.body().getPhoneNumber();
                    userPhoneTV.setText(userPhone);
                    userIdTV.setText("Id: " + response.body().getId());
                    //TODO add static method
                    if(response.body().getImageUri() != null && response.body().getImageUri().length() > 0) {
                        String b = response.body().getImageUri();
                        b = b.substring(1,b.length()-1);
                        Uri uri = Uri.parse(b);
                        userImage.setImageURI(uri);
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(GuideProfile.this, "userData call error",Toast.LENGTH_LONG).show();
            }
        });
    }


    public void setOnClickListeners (){
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GuideProfile.this,Home.class);
                startActivity(intent);
            }
        });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launcher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }
        });

        editEmailTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailDialogFragment = new UpdateEmailFragment();
                emailDialogFragment.show(getSupportFragmentManager(),"updateEmail");
            }
        });

        editPhoneTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneDialogFragment = new UpdatePhoneFragment(userPhone);
                phoneDialogFragment.show(getSupportFragmentManager(),"updatePhone");
            }
        });


        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PKCE.logoutTokens(GuideProfile.this);
                Intent intent = new Intent(GuideProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        deleteTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteGuide();
                deleteUser();
                PKCE.logoutTokens(GuideProfile.this);
                Intent intent = new Intent(GuideProfile.this, MainActivity.class);
                startActivity(intent);
            }
        });

        whatToOfferTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatToOfferDialogFragment = new UpdateWhatToOfferFragment();
                bundleArgs.putSerializable("GUIDE", guide);
                whatToOfferDialogFragment.setArguments(bundleArgs);
                whatToOfferDialogFragment.show(getSupportFragmentManager(),"whatToOfferDialogFragment");
            }
        });


        aboutMeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                aboutMeDialogFragment = new UpdateAboutMeFragment();
                bundleArgs.putSerializable("GUIDE", guide);
                aboutMeDialogFragment.setArguments(bundleArgs);
                aboutMeDialogFragment.show(getSupportFragmentManager(),"whatToOfferDialogFragment");
            }
        });

        priceTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                priceDialogFragment = new UpdatePriceFragment();
                bundleArgs.putSerializable("GUIDE", guide);
                priceDialogFragment.setArguments(bundleArgs);
                priceDialogFragment.show(getSupportFragmentManager(),"priceDialogFragment");
            }
        });

        languagesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                languagesDialogFragment = new UpdateLanguagesFragment();
                bundleArgs.putSerializable("GUIDE", guide);
                languagesDialogFragment.setArguments(bundleArgs);
                languagesDialogFragment.show(getSupportFragmentManager(),"languagesDialogFragment");
            }
        });


    }

    private void updateUserImage(String image) {
        String accessCode = PKCE.getAccessToken(this);
        final Call<ResponseBody> setUserImage = retrofitAuth
                .getRetrofit()
                .create(UserApi.class)
                .saveUserImage("Bearer " + accessCode, user.getEmail(), image);

        setUserImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(GuideProfile.this, "update image call error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setBookingsRecyclerView(List<Booking> bookingList) {
        bookingRecyclerView = findViewById(R.id.bookingRVGuideProfile);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        bookingRecyclerView.setLayoutManager(layoutManager);
        bookingAdapter = new BookingListGuideAdapter(this, bookingList,user);
        bookingRecyclerView.setAdapter(bookingAdapter);
    }

    private void setBookings() {
        String accessCode = PKCE.getAccessToken(this);
        final Call<List<Booking>> getBookings = retrofit
                .getRetrofit()
                .create(BookingApi.class)
                .getBookings("Bearer " + accessCode, user.getEmail());

        getBookings.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if(response.body() != null ) {
                    setBookingsRecyclerView(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                Toast.makeText(GuideProfile.this, "Bookings call error",Toast.LENGTH_LONG).show();
            }
        });
    }

    //TODO intent guideprofile
    private void setGuideData() {
        String accessCode = PKCE.getAccessToken(this);
        final Call<Guide> getGuide = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .getGuideDetailsByEmail("Bearer " + accessCode, user.getEmail());

        getGuide.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                if(response.body() != null ) {
                    guide = response.body();
                    aboutMeTV.setText(guide.getAboutMe());
                    whatToOfferTV.setText(guide.getWhatToOffer());
                    priceTV.setText(guide.getPrice().toString());
                   // languagesTV.setText(guide.getLanguages().toString());
                }
            }

            @Override
            public void onFailure(Call<Guide> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(GuideProfile.this, "userData call error",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteGuide() {
        String accessCode = PKCE.getAccessToken(this);

        final Call<Void> deleteGuide = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .deleteGuide("Bearer " + accessCode, user.getEmail());

        deleteGuide.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                guide = null;
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(GuideProfile.this, "delete guide error", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void deleteUser() {
        String accessCode = PKCE.getAccessToken(this);
        final Call<Void> deleteUser = retrofit
                .getRetrofit()
                .create(UserApi.class)
                .deleteUser("Bearer " + accessCode, user.getEmail());

        deleteUser.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(GuideProfile.this, "delete guide error", Toast.LENGTH_LONG).show();
            }
        });

    }

}
