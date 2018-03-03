package com.mycoursesapp.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.adapter.CertificatesAdapter;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.R;
import com.mycoursesapp.adapter.CategoriesAdapter;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.helper.Utils;
import com.mycoursesapp.model.Category;
import com.mycoursesapp.model.Certificate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by mamdouhelnakeeb on 2/8/18.
 */

public class CompleteReg extends BaseActivity {

    Spinner catSpinner;
    EditText mobileET;
    Button addCrBtn, compRegBtn;

    Toolbar toolbar;

    ArrayList<Category> categoryArrayList;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    RecyclerView certificatesRV;
    CertificatesAdapter certificatesAdapter;
    ArrayList<Certificate> certificateArrayList;

    String certificatesStr = "";

    private ProgressDialog pDialog;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.complete_reg;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupToolbar();
        getCategories();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        certificatesRV = findViewById(R.id.certificatesRV);

        mobileET = findViewById(R.id.mobileET);
        addCrBtn = findViewById(R.id.addCrtBtn);
        compRegBtn = findViewById(R.id.compRegBtn);

        certificatesRV = findViewById(R.id.certificatesRV);
        setupRV();

        prefs = getSharedPreferences("User", MODE_PRIVATE);
        editor = prefs.edit();

        editor.putBoolean("login", false);
        editor.apply();


        compRegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    completeReg();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        addCrBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AlertDialog.Builder builder = new AlertDialog.Builder(CompleteReg.this);

                final View dialogView = CompleteReg.this.getLayoutInflater().inflate(R.layout.certificate_add_dialog, null);
                builder.setView(dialogView);

                final AlertDialog alert = builder.create();
                alert.getWindow().setLayout(600, 400);
                Button dialogButton = dialogView.findViewById(R.id.addCrtBtn);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addUserCertificate(alert);
                    }
                });

                alert.show();





//                // custom dialog
//                final Dialog dialog = new Dialog(CompleteReg.this);
//                dialog.setContentView(R.layout.certificate_add_dialog);
//                Button dialogButton = dialog.findViewById(R.id.addCrtBtn);
//                // if button is clicked, close the custom dialog
//                dialogButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//                        addUserCertificate(dialog);
//                    }
//                });
//
//                dialog.show();

            }
        });
    }

    private void setupRV(){

        certificateArrayList = new ArrayList<>();
        certificatesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        certificatesAdapter = new CertificatesAdapter(this, certificateArrayList);
        certificatesRV.setAdapter(certificatesAdapter);
    }

    private void setupToolbar(){

        TextView toolbarTV = findViewById(R.id.toolbarTV);

        toolbarTV.setText(R.string.comp_reg);
        findViewById(R.id.homeIB).setVisibility(View.GONE);

    }

    private void setupSpinner(){

        catSpinner = findViewById(R.id.catSpinner);
        catSpinner.setAdapter(new CategoriesAdapter(this, categoryArrayList));

    }

    private void getCategories(){

        CallAPI callAPI = new CallAPI();
        categoryArrayList = new ArrayList<>();

        String url = Consts.CATEGORIES;

        Log.d("url", url);

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(CompleteReg.this, "Network Error, Try again", Toast.LENGTH_SHORT).show();
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
                            categoryArrayList.add(new Category(Integer.parseInt(jsonObject.get("id").toString()), jsonObject.get("category").toString()));

                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {

                                setupSpinner();

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    // Request not successful
                }
            }
        });
    }

    private void completeReg() throws IOException, JSONException {

        if (!Utils.isNetworkConnected(this)){
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        final String mobile = mobileET.getText().toString();

        if (mobile.isEmpty()){
            Toast.makeText(this,"Please enter your Mobile Number", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Consts.COMPLETE_PROFILE + prefs.getInt("id", 0) + "/";
        Log.d("url", url);

        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < categoryArrayList.size(); i++){

            if (categoryArrayList.get(i).selected)
                jsonArray.put(categoryArrayList.get(i).id);
        }

        if (jsonArray.length() == 0){
            Toast.makeText(this,"Please select your Field of Study", Toast.LENGTH_SHORT).show();
            return;
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("mobile", mobile);
        jsonObject.put("fieldOfStudy", jsonArray);
        jsonObject.put("certificate", cerArrToStr());

        System.out.println(String.valueOf(jsonObject.toString()));

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

                                    Toast.makeText(getApplicationContext(), "Your profile is updated", Toast.LENGTH_SHORT).show();
                                    editor.putString("mobile", mobile);
                                    editor.putBoolean("login", true);
                                    editor.apply();

                                    Intent intent = new Intent(getBaseContext(), Home.class);
                                    startActivity(intent);
                                    finish();
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

    private void addUserCertificate(final Dialog dialog){

        final String cerName = ((EditText) dialog.findViewById(R.id.cerNameET)).getText().toString().trim();
        final String cerUrl = ((EditText) dialog.findViewById(R.id.cerUrlET)).getText().toString().trim();

        if (cerName.isEmpty()){
            Toast.makeText(getBaseContext(), "Please enter certificate name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (cerUrl.isEmpty()){
            Toast.makeText(getBaseContext(), "Please enter certificate link", Toast.LENGTH_SHORT).show();
            return;
        }

        certificateArrayList.add(new Certificate(cerName, cerUrl));
        certificatesAdapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    private String cerArrToStr(){

        certificatesStr = "";

        StringBuilder stringBuilder = new StringBuilder(certificatesStr);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.done_menu, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                // do your stuff here, eg: finish();
//                finish();
//                return true;
//
//            case R.id.action_menu_done:
//                try {
//                    completeReg();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
