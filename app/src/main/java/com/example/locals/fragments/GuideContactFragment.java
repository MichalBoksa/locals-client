package com.example.locals.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.locals.R;
import com.example.locals.activities.GuideDetails;

public class GuideContactFragment extends DialogFragment {
    private ImageView cancelBtn;
    private TextView phone;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_guide_contact, container, false);
        Intent intent = new Intent(getActivity().getApplicationContext(), GuideDetails.class);
        Bundle bundle = getArguments();
        String phoneBundle = bundle.getString("GUIDE_PHONE","");

        cancelBtn = view.findViewById(R.id.closeContactFragment);
        phone = view.findViewById(R.id.guideContactFragmentTV);
//        String phoneBundle = intent.getStringExtra("GUIDE_PHONE");//getExtras().getString("GUIDE_PHONE");
        phone.setText(phoneBundle);


        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

}