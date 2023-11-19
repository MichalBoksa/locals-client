package com.example.locals.activities;

import static java.text.DateFormat.DEFAULT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.locals.R;
import com.example.locals.models.User;
import com.example.locals.retrofit.RetrofitService;
import com.example.locals.retrofit.UserApi;
import com.example.locals.utils.PKCE;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity {

    ImageView backArrow;
    ImageView userImage;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    int PROFILE_IMAGE_SIZE_WIDTH = 150;
    int PROFILE_IMAGE_SIZE_HEIGHT = 150;
    RetrofitService retrofit;
    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            byte[] inputData;
            if (uri == null)
                Toast.makeText(UserProfile.this, "No image Selected", Toast.LENGTH_SHORT).show();
            else {
                InputStream iStream = null;
                try {
                    iStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                try {
                     inputData = getBytes(iStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                userImage.setImageURI(uri);
                updateUserImage(inputData);
            }
        }
    });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        retrofit = new RetrofitService();
        retrofit.initializeRetrofit();
        backArrow = findViewById(R.id.backArrowUserProfile);
        userImage = findViewById(R.id.imageUserProfile);
        setUserData();
        setOnClickListeners();
    }

    private void setUserData() {
      //  usernameTV = findViewById(R.id.usernameTVHome);
        String accessCode = PKCE.getAccessToken(this);
        final Call<User> getUser = retrofit
                .getRetrofit()
                .create(UserApi.class)
                .getUser(accessCode, PKCE.getJWTUser(accessCode));

        getUser.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.body() != null ) {
                    //TODO it can crash cuz of models
                  //  usernameTV.setText(response.body().getName());
                   // String uri = response.body().getImage().substring(1,response.body().getImage().length()-1);
//                    Uri photoUri = Uri.fromFile( new File(uri));
//                    if (response.body().getImage() != null && response.body().getImage().length() > 0) {
//                        Glide.with(getApplicationContext())
//                                .load(new File(uri))
//                                .into(userImage);
//                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(UserProfile.this, "userData call error",Toast.LENGTH_LONG).show();
            }
        });
    }


    public void setOnClickListeners (){
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfile.this,Home.class);
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

    }

    private void updateUserImage(byte[] image) {
        String accessCode = PKCE.getAccessToken(this);
        final Call<ResponseBody> setUserImage = retrofit
                .getRetrofit()
                .create(UserApi.class)
                .saveUserImage(accessCode, PKCE.getJWTUser(accessCode), image);

        setUserImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println(call);
                Toast.makeText(UserProfile.this, "update image call error", Toast.LENGTH_LONG).show();
            }
        });
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}
