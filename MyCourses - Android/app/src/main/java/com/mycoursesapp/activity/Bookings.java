package com.mycoursesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.adapter.BookingsAdapter;
import com.mycoursesapp.adapter.CertificatesAdapter;
import com.mycoursesapp.adapter.PromoCodesAdapter;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.model.Booking;
import com.mycoursesapp.model.Certificate;
import com.mycoursesapp.model.PromoCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mamdouhelnakeeb on 2/14/18.
 */

public class Bookings extends BaseActivity {

    RecyclerView bookingRV;
    BookingsAdapter bookingsAdapter;
    ArrayList<Booking> bookingArrayList;

    private ProgressDialog pDialog;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.bookings_activity;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bookingRV = findViewById(R.id.bookingsRV);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        setupToolbar();

        getBookings();

    }

    private void setupRV(){

        bookingRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        bookingsAdapter = new BookingsAdapter(this, bookingArrayList);
        bookingRV.setAdapter(bookingsAdapter);
    }

    public void getBookings() {

        CallAPI callAPI = new CallAPI();
        bookingArrayList = new ArrayList<>();

        String url = Consts.BOOKING_USER
                + String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getInt("id", 0)) + "/";

        Log.d("url", url);

        pDialog.setMessage("Loading Bookings");
        showDialog();

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong


                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();
                        Toast.makeText(Bookings.this, "Network Error, Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("response", responseStr);

                    try {
                        JSONArray jsonArray = new JSONArray(responseStr);

                        for (int i = 0; i <jsonArray.length(); i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            bookingArrayList
                                    .add(new Booking(
                                            Integer.parseInt(jsonObject.get("id").toString()),
                                            jsonObject.get("centreName").toString(),
                                            jsonObject.get("courseName").toString(),
                                            jsonObject.get("courseImage").toString(),
                                            jsonObject.get("startData").toString()
                                    ));

                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {


                                if(bookingArrayList.size() != 0) {

                                    setupRV();

                                } else {
                                    Toast.makeText(Bookings.this, "No Bookings Found",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    // Request not successful
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();
                    }
                });
            }
        });

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void setupToolbar(){

        TextView toolbarTV = findViewById(R.id.toolbarTV);

        toolbarTV.setText(R.string.bookings);

    }
}
