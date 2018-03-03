package com.mycoursesapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.adapter.CertificatesAdapter;
import com.mycoursesapp.adapter.PromoCodesAdapter;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.helper.Utils;
import com.mycoursesapp.model.Certificate;
import com.mycoursesapp.model.PromoCode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by mamdouhelnakeeb on 2/14/18.
 */

public class Certificates extends BaseActivity {

    RecyclerView certificatesRV;
    CertificatesAdapter certificatesAdapter;
    ArrayList<Certificate> certificateArrayList;

    FloatingActionButton addCertificateFAB;

    String certificatesStr = "";

    private ProgressDialog pDialog;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.certificates_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        certificatesRV = findViewById(R.id.certificatesRV);
        addCertificateFAB = findViewById(R.id.addCertificateFAB);

        setupToolbar();

        addCertificateFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(Certificates.this);

                final View dialogView = Certificates.this.getLayoutInflater().inflate(R.layout.certificate_add_dialog, null);
                builder.setView(dialogView);

                final AlertDialog alert = builder.create();
                alert.getWindow().setLayout(600, 400);
                Button dialogButton = dialogView.findViewById(R.id.addCrtBtn);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        try {
                            addUserCertificate(alert);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                alert.show();


//                final Dialog dialog = new Dialog(Certificates.this);
//                dialog.setContentView(R.layout.certificate_add_dialog);
//                Button dialogButton = dialog.findViewById(R.id.addCrtBtn);
//                // if button is clicked, close the custom dialog
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        try {
//                            addUserCertificate(dialog);
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                });
//                dialog.show();

            }
        });

        getUserCertificate();

    }

    private void setupRV(){

        certificatesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        certificatesAdapter = new CertificatesAdapter(this, certificateArrayList);
        certificatesRV.setAdapter(certificatesAdapter);
    }

    public void getUserCertificate() {

        CallAPI callAPI = new CallAPI();
        certificateArrayList = new ArrayList<>();

        String url = Consts.COMPLETE_PROFILE
                + String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getInt("id", 0)) + "/";

        Log.d("url", url);

        pDialog.setMessage("Loading Certificates");
        showDialog();

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong


                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();
                        Toast.makeText(Certificates.this, "Network Error, Try again", Toast.LENGTH_SHORT).show();
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

                        String[] certificates;

                        if (jsonObject.has("certificate")
                                && !jsonObject.get("certificate").toString().equals("null")
                                && !jsonObject.get("certificate").toString().trim().isEmpty()){

                            certificates = jsonObject.getString("certificate").split("\\$");

                            System.out.println(certificates[0]);
                            String[] certificate;

                            for (int i = 0; i < certificates.length; i++){

                                certificate = certificates[i].split("\\^");
//                                System.out.println(certificate[0]);
//                                System.out.println(certificate[1]);

                                certificateArrayList
                                        .add(new Certificate(
                                                certificate[0],
                                                certificate[1])
                                        );
                            }
                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {

                                if(certificateArrayList.size() != 0) {

                                    setupRV();

                                } else {
                                    Toast.makeText(Certificates.this, "No Certificates Found",Toast.LENGTH_SHORT).show();
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

    private void addUserCertificate(final Dialog dialog) throws IOException, JSONException {

        if (!Utils.isNetworkConnected(this)){
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        final String cerName = ((EditText) dialog.findViewById(R.id.cerNameET)).getText().toString().trim();
        final String cerUrl = ((EditText) dialog.findViewById(R.id.cerUrlET)).getText().toString().trim();

        String url = Consts.COMPLETE_PROFILE + String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getInt("id", 0)) + "/";
        Log.d("url", url);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("certificate", cerArrToStr(cerName + " ^ " + cerUrl));

        System.out.println(jsonObject.toString());

        OkHttpClient httpClient = CallAPI.getUnsafeOkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .patch(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //
                    @Override
                    public void run() {

                        Toast.makeText(getBaseContext(), "Network Error, Try again", Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(getApplicationContext(), "An error occurred, Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        if (jsonObject.has("created") && jsonObject.getBoolean("created")) {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getApplicationContext(), "Certificates are updated successfully", Toast.LENGTH_SHORT).show();

                                    certificateArrayList.add(new Certificate(cerName, cerUrl));
                                    certificatesAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });


                        } else {

                            final String err = "An error occurred, Try again later!";

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getBaseContext(), err, Toast.LENGTH_SHORT).show();

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

    private String cerArrToStr(String newCer){

        certificatesStr = "";

        if (certificateArrayList.size() == 0){
            certificatesStr = newCer;
            return certificatesStr;
        }

        StringBuilder stringBuilder = new StringBuilder(certificatesStr);

        stringBuilder.append(newCer);
        stringBuilder.append(" $ ");

        for (int i = 0; i < certificateArrayList.size(); i++){

            stringBuilder.append(certificateArrayList.get(i).name.trim());
            stringBuilder.append(" ^ ");
            stringBuilder.append(certificateArrayList.get(i).url.trim());

            if (i != certificateArrayList.size() - 1)
                stringBuilder.append(" $ ");

        }

        certificatesStr = stringBuilder.toString();

        return certificatesStr;
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
        toolbarTV.setText(R.string.certificates);

    }

}
