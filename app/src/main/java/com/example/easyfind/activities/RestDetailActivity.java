package com.example.easyfind.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.tinkoff.scrollingpagerindicator.ScrollingPagerIndicator;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.easyfind.R;
import com.example.easyfind.adapter.RestDetailImgAdapter;
import com.example.easyfind.models.Business;
import com.example.easyfind.store.APIClient;
import com.example.easyfind.store.GetDataService;

import java.util.ArrayList;
import java.util.List;

public class RestDetailActivity extends AppCompatActivity {

    private RecyclerView listView;
    private RestDetailImgAdapter detailImgAdapter;
    private TextView rName;
    private TextView rPrice;
    private TextView rDesc;
    private TextView callTV;
    private TextView addressTV;
    private ImageButton callBtn;
    private ImageButton msgBtn;
    private ImageButton mapBtn;
    private GetDataService apiInterface;
    private Business res;
    private String businessId;
    private List<String> imagesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_detail);
        Intent intent = getIntent();
        businessId = intent.getStringExtra("id");
        initView();
        initRecyclerView();
        fetchData();
    }

    private void initRecyclerView() {
        listView = findViewById(R.id.imgRecyclerView);
        detailImgAdapter = new RestDetailImgAdapter(imagesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        listView.setLayoutManager(layoutManager);
        listView.setAdapter(detailImgAdapter);
        ScrollingPagerIndicator recyclerIndicator = findViewById(R.id.indicator);
        recyclerIndicator.attachToRecyclerView(listView);
    }

    private void initView() {
        rName = findViewById(R.id.name);
        rPrice = findViewById(R.id.price);
        rDesc = findViewById(R.id.description);
        callTV = findViewById(R.id.callTxt);
        addressTV = findViewById(R.id.address);
        callBtn = findViewById(R.id.callImgBtn);
        msgBtn = findViewById(R.id.message);
        mapBtn = findViewById(R.id.mapBtn);
        //
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialContactPhone(res.getPhone());
            }
        });

        //
        msgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS("Enquiry!");
            }
        });

        //
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent vAct = new Intent(v.getContext(), MapActivity.class);
                vAct.putExtra("lat", res.getCoordinates().getLatitude());
                vAct.putExtra("long", res.getCoordinates().getLongitude());
                v.getContext().startActivity(vAct);
            }
        });
    }

    public void sendSMS(String message) {
        SmsManager sms = SmsManager.getDefault();
        PendingIntent sentPI;
        String SENT = "SMS_SENT";
        sentPI = PendingIntent.getBroadcast(this, 0,new Intent(SENT), 0);
        sms.sendTextMessage("+16479137475", null, message, sentPI, null);
    }

    private void dialContactPhone(final String phoneNumber) {
        startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phoneNumber, null)));
    }

    private void fetchData() {
        apiInterface = APIClient.getRetrofit().create(GetDataService.class);
        Call<Business> call = apiInterface.getBusinessDetail(businessId);
        call.enqueue(new Callback<Business>() {
            @Override
            public void onResponse(Call<Business> call, Response<Business> response) {
                response.isSuccessful();
                if (response.isSuccessful()) {
                    res = response.body();
                    setupData(res);
                    imagesList.addAll(res.getPhotos());
                    detailImgAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Business> call, Throwable t) {
                call.cancel();
            }
        });
    }

    private void setupData(Business business) {
        rName.setText(business.getName());
        rPrice.setText(business.getPrice());
        rDesc.setText(business.getAlias());
        callTV.setText(business.getPhone());
        addressTV.setText(business.getLocation().getDisplayAddress().toString());
    }
}
