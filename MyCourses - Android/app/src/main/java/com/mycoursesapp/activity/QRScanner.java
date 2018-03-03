package com.mycoursesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.mycoursesapp.R;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.helper.Utils;
import com.mycoursesapp.model.PromoCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by mamdouhelnakeeb on 2/12/18.
 */

public class QRScanner extends AppCompatActivity implements QRCodeReaderView.OnQRCodeReadListener {


    EditText qrCode;
    Button promoCodeBtn;

    private QRCodeReaderView qrCodeReaderView;

    private ProgressDialog pDialog;

    int discount;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.d("Base", "attachBaseContext");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_activity);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        qrCode = findViewById(R.id.promoCodeET);
        promoCodeBtn = findViewById(R.id.promoCodeBtn);

        qrCodeReaderView = findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);

        // Use this function to enable/disable decoding
        qrCodeReaderView.setQRDecodingEnabled(true);

        promoCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String promoCode = qrCode.getText().toString().trim();

                if (promoCode.isEmpty()){
                    Toast.makeText(getBaseContext(), "Please enter a valid Promo Code", Toast.LENGTH_SHORT).show();
                    return;
                }

                checkPromoCode(promoCode);
            }
        });

        setupToolbar();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {

        System.out.println("QRCode: " + text);

        qrCode.setText(text);


    }

    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }

    private void checkPromoCode(final String promoCode){

        pDialog.setMessage("Validating Promo Code");

        showDialog();

        CallAPI callAPI = new CallAPI();

        String url = Consts.PROMO_CODE + promoCode + "/";

        Log.d("url", url);

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();
                        Toast.makeText(getBaseContext(), "Network Error, Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("response", responseStr);

                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);

                        if (jsonObject.has("discount")){

                            discount = jsonObject.getInt("discount");
                            int codeID = jsonObject.getInt("id");
                            addPromoToUser(codeID, discount);

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getBaseContext(), String.valueOf(discount) + " % discount", Toast.LENGTH_SHORT).show();


                                }
                            });
                        }
                        else if (jsonObject.has("errors")) {
                            final String err = jsonObject.getString("errors");

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getBaseContext(), err, Toast.LENGTH_SHORT).show();
                                    discount = 0;
                                }
                            });
                        }

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


    public void addPromoToUser(int codeID, int discount) throws JSONException {

        if (!Utils.isNetworkConnected(this)){
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }


        String url = Consts.PROMO_USER
                + String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getInt("id", 0)) + "/";

        Log.d("url", url);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("promoCode", codeID);
//        jsonObject.put("discount", String.valueOf(discount));

        OkHttpClient httpClient = CallAPI.getUnsafeOkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //
                    @Override
                    public void run() {

                        Toast.makeText(QRScanner.this, "Server Error, Try again Later", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println(response);

                if (!response.isSuccessful()) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        //
                        @Override
                        public void run() {

                            Toast.makeText(QRScanner.this, "An error occurred, Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());


                        if (jsonObject.has("created") && Boolean.parseBoolean(jsonObject.get("created").toString())) {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(QRScanner.this, "Promo Code is added successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });


                        }
                        else {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(QRScanner.this, "You have already taken this promotion!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }


    private void setupToolbar(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTV = findViewById(R.id.toolbarTV);

        setSupportActionBar(toolbar);
        toolbarTV.setText("Promo Code");

        ImageButton toolbarIB = findViewById(R.id.toolbarIB);

        toolbarIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
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
}
