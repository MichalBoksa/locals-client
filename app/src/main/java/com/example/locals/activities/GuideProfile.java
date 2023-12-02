package com.example.locals.activities;

import static android.view.View.inflate;
import static java.text.DateFormat.DEFAULT;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.locals.MainActivity;
import com.example.locals.R;

import com.example.locals.adapters.BookingListAdapter;
import com.example.locals.adapters.GuideListAdapter;
import com.example.locals.databinding.ActivityUserProfileBinding;
import com.example.locals.fragments.AddFavoritesListFragment;
import com.example.locals.fragments.RegisterGuideFragment;
import com.example.locals.fragments.UpdateEmailFragment;
import com.example.locals.fragments.UpdatePhoneFragment;
import com.example.locals.models.Booking;
import com.example.locals.models.Guide;
import com.example.locals.models.User;
import com.example.locals.retrofit.BookingApi;
import com.example.locals.retrofit.GuideApi;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    private TextView becomeLocalTV;
    private TextView logoutTV;
    private String userPhone;
    private RetrofitService retrofit;
    private UpdateEmailFragment emailDialogFragment;
    private UpdatePhoneFragment phoneDialogFragment;
    private RegisterGuideFragment registerGuideFragment;
    private RecyclerView bookingRecyclerView;
    private BookingListAdapter bookingAdapter;
    private User user;
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        if(PKCE.isJWTexpired(this)) {
            PKCE.refreshToken(this, REDIRECT_URI);
        }
        backArrow = findViewById(R.id.backArrowUserProfile);
        userImage = findViewById(R.id.imageUserProfile);
        usernameTV = findViewById(R.id.usernameUserProfileTV);
        userIdTV = findViewById(R.id.userIdUserProfileTV);
        userEmailTV = findViewById(R.id.emailUserProfile);
        userPhoneTV = findViewById(R.id.phoneNumberUserProfile);
        editEmailTV = findViewById(R.id.updateEmailUserProfile);
        editPhoneTV = findViewById(R.id.updatePhoneUserProfile);
        becomeLocalTV = findViewById(R.id.becomeLocalUserProfile);
        logoutTV = findViewById(R.id.logoutUserProfile);
        gson = new Gson();
        sharedPref = GuideProfile.this.getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        user = gson.fromJson(sharedPref.getString("USER",null), User.class);

        setUserData();
        setGuideData();
        setBookings();
        setOnClickListeners();
    }

    private void setUserData() {
        //TODO change to sharedprefs
        String accessCode = PKCE.getAccessToken(this);
        final Call<User> getUser = retrofit
                .getRetrofit()
                .create(UserApi.class)
                .getUser("Bearer " + accessCode, PKCE.getJWTUser(accessCode));

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

        becomeLocalTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerGuideFragment = new RegisterGuideFragment();
                registerGuideFragment.show(getSupportFragmentManager(),"registerGuide");
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

    }

    private void updateUserImage(String image) {
        String accessCode = PKCE.getAccessToken(this);
        final Call<ResponseBody> setUserImage = retrofit
                .getRetrofit()
                .create(UserApi.class)
                .saveUserImage("Bearer " + accessCode, PKCE.getJWTUser(accessCode), image);

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
        bookingRecyclerView = findViewById(R.id.bookingRVUserProfile);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        bookingRecyclerView.setLayoutManager(layoutManager);
        bookingAdapter = new BookingListAdapter(this, bookingList,user);
        bookingRecyclerView.setAdapter(bookingAdapter);
    }

    private void setBookings() {
        String accessCode = PKCE.getAccessToken(this);
        final Call<List<Booking>> getBookings = retrofit
                .getRetrofit()
                .create(BookingApi.class)
                .getBookings("Bearer " + accessCode, PKCE.getJWTUser(accessCode));

        getBookings.enqueue(new Callback<List<Booking>>() {
            @Override
            public void onResponse(Call<List<Booking>> call, Response<List<Booking>> response) {
                if(response.body() != null ) {
                    List<Booking> test = new ArrayList<>();
                    test = response.body()
                            .stream().filter(b -> !b.isAccepted())
                            .collect(Collectors.toList());
                    setBookingsRecyclerView(response.body()
                            .stream().filter(b -> !b.isAccepted())
                            .collect(Collectors.toList()));
                }
            }

            @Override
            public void onFailure(Call<List<Booking>> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(GuideProfile.this, "Bookings call error",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setGuideData() {
        //TODO change to sharedprefs
        String accessCode = PKCE.getAccessToken(this);
        final Call<Guide> getGuide = retrofit
                .getRetrofit()
                .create(GuideApi.class)
                .getGuideDetails("Bearer " + accessCode, PKCE.getJWTUser(accessCode));

        getUser.enqueue(new Callback<Guide>() {
            @Override
            public void onResponse(Call<Guide> call, Response<Guide> response) {
                if(response.body() != null ) {

                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(GuideProfile.this, "userData call error",Toast.LENGTH_LONG).show();
            }
        });
    }

}
