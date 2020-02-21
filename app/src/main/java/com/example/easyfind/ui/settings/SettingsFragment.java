package com.example.easyfind.ui.settings;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.easyfind.R;
import com.example.easyfind.activities.LoginActivity;
import com.example.easyfind.store.UserDataManager;

public class SettingsFragment extends Fragment {

    private Button call;
    private Button email;
    private Button about;
    private Button logout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

    }

    private void initViews(View view){
        call = view.findViewById(R.id.callus);
        email = view.findViewById(R.id.emailus);
        about = view.findViewById(R.id.aboutus);
        logout = view.findViewById(R.id.logout);

        //
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialContactPhone("123123123");

            }
        });

        //
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendEmail();

            }
        });

        //
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                startActivity(browserIntent);

            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogout();
            }
        });
    }

    private void performLogout() {
        UserDataManager.setLoggedIn(getContext(), false);
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    protected void sendEmail() {


        String[] TO = {"someone@gmail.com"};
        String[] CC = {"xyz@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));


        } catch (ActivityNotFoundException ex) {
        }
    }
}
