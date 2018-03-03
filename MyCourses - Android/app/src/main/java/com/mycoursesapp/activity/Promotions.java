package com.mycoursesapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.mycoursesapp.adapter.PromoCodesAdapter;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
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
 * Created by mamdouhelnakeeb on 2/13/18.
 */

public class Promotions extends BaseActivity {


    RecyclerView promoCodesRV;
    PromoCodesAdapter promoCodesAdapter;
    ArrayList<PromoCode> promoCodeArrayList;

    FloatingActionButton addPromoFAB;


    private ProgressDialog pDialog;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.promotions_activity;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        promoCodesRV = findViewById(R.id.promoCodesRV);
        addPromoFAB = findViewById(R.id.addPromoFAB);

        setupToolbar();

        addPromoFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openQRScanner();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        getUserPromos();
    }

    private void setupRV(){

        promoCodesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        promoCodesAdapter = new PromoCodesAdapter(this, promoCodeArrayList);
        promoCodesRV.setAdapter(promoCodesAdapter);
    }

    public void getUserPromos() {

        CallAPI callAPI = new CallAPI();
        promoCodeArrayList = new ArrayList<>();

        String url = Consts.PROMO_USER
                + String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getInt("id", 0)) + "/";

        Log.d("url", url);

        pDialog.setMessage("Loading Promotions");
        showDialog();

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong


                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();
                        Toast.makeText(Promotions.this, "Network Error, Try again", Toast.LENGTH_SHORT).show();
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

                        for (int i = jsonArray.length() - 1; i > 0; i--){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            JSONObject promoObj = jsonObject.getJSONObject("promoCode");

                            promoCodeArrayList
                                    .add(new PromoCode(
                                            Integer.parseInt(promoObj.get("discount").toString()),
                                            promoObj.get("promoCode").toString(),
                                            Integer.parseInt(promoObj.get("id").toString()))
                                    );

                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {


                                if(promoCodeArrayList.size() != 0) {

                                    setupRV();

                                } else {
                                    Toast.makeText(Promotions.this, "No Promotions Found",Toast.LENGTH_SHORT).show();
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

        toolbarTV.setText(R.string.promotions);
    }

    private void openQRScanner(){

        askForPermission(Manifest.permission.CAMERA,11);
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, QRScanner.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){

            if (requestCode == 11){
                Intent intent = new Intent(this, QRScanner.class);
                startActivity(intent);
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
