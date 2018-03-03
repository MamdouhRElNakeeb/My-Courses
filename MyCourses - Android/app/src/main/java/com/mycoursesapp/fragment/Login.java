package com.mycoursesapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.applikeysolutions.library.Authentication;
import com.applikeysolutions.library.NetworklUser;
import com.mycoursesapp.activity.CompleteReg;
import com.mycoursesapp.activity.Home;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.R;
import com.mycoursesapp.helper.Utils;
import com.mycoursesapp.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

public class Login extends Fragment {

    EditText emailET, passET;

    Button loginBtn;
    ImageButton fbIB, twtIB, googleIB;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    // Social Login
    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";
    public static final String TWITTER = "twitter";


    private ProgressDialog pDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        emailET = view.findViewById(R.id.emailET);
        passET = view.findViewById(R.id.passET);

        loginBtn = view.findViewById(R.id.loginBtn);
        fbIB = view.findViewById(R.id.fbIB);
        twtIB = view.findViewById(R.id.twtIB);
        googleIB = view.findViewById(R.id.googleIB);

        setupSocialLogin();

        prefs = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = prefs.edit();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    login();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    private void login() throws IOException {

        if (!Utils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        final String email = emailET.getText().toString().trim();
        String password = passET.getText().toString().trim();

        if (email.isEmpty()){
            Toast.makeText(getActivity(),"Please enter your Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utils.isValidEmaill(email)){
            Toast.makeText(getActivity(),"Please enter a valid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (password.isEmpty()){
            Toast.makeText(getActivity(),"Please enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Utils.isValidPassword(password)){
            Toast.makeText(getActivity(),"Please enter a valid password", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Consts.LOGIN;
        Log.d("url", url);

        RequestBody formBody = new FormBody.Builder()
                .add("username", email)
                .add("password", password)
                .build();

        OkHttpClient httpClient = CallAPI.getUnsafeOkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //
                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "Network Error, Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (!response.isSuccessful()) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        //
                        @Override
                        public void run() {

                            Toast.makeText(getActivity(), "An error occurred, Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        System.out.println("loginResp: " + jsonObject.toString());

                        if (jsonObject.has("login") && jsonObject.getBoolean("login")) {

                            if((jsonObject.has("is_staff") && jsonObject.getBoolean("is_staff"))
                                    || (jsonObject.has("is_superuser") && jsonObject.getBoolean("is_superuser"))){

                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {

                                        Toast.makeText(getActivity(), "You're not permitted to login here", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                            else {

                                final int id = jsonObject.getInt("id");
                                final String name = jsonObject.getString("first_name");
                                String mobileStr = "";
                                if (jsonObject.has("mobile") && jsonObject.get("mobile") != null){

                                    mobileStr = jsonObject.get("mobile").toString();

                                }
                                final String mobile = mobileStr;

                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {

                                        Toast.makeText(getActivity(), "Login is successful", Toast.LENGTH_SHORT).show();
                                        editor.putInt("id", id);
                                        editor.putString("email", email);
                                        editor.putString("name", name);
                                        editor.putBoolean("login", true);

                                        if(mobile.isEmpty()){
                                            editor.apply();
                                            Intent intent = new Intent(getActivity(), CompleteReg.class);
                                            getActivity().startActivity(intent);
                                        }
                                        else {
                                            editor.putString("mobile", mobile);
                                            editor.apply();
                                            Intent intent = new Intent(getActivity(), Home.class);
                                            getActivity().startActivity(intent);
                                            getActivity().finish();
                                        }

                                    }
                                });
                            }

                        } else if (jsonObject.has("errors")) {

                            final String err = jsonObject.get("errors").toString();

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();

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

    private void setupSocialLogin(){

        fbIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> scopes = Arrays.asList("public_profile", "email");
                Authentication
                        .getInstance()
                        .connectFacebook(scopes)
                        .login()
                        .subscribe(user -> handleSocialLogin(FACEBOOK, user),
                                throwable -> showToast(throwable.getMessage()));

            }
        });

        twtIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Authentication
                        .getInstance()
                        .connectTwitter()
                        .login()
                        .subscribe(networklUser -> handleSocialLogin(TWITTER, networklUser),
                                throwable -> showToast(throwable.getMessage()));

            }
        });

        googleIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                List<String> scopes = Arrays.asList(
                        "https://www.googleapis.com/auth/youtube",
                        "https://www.googleapis.com/auth/youtube.upload"
                );

                Authentication
                        .getInstance()
                        .connectGoogle(scopes)
                        .login()
                        .subscribe(networklUser -> handleSocialLogin(GOOGLE, networklUser),
                                throwable -> showToast(throwable.getMessage()));

            }
        });

    }

    private void handleSocialLogin(String provider, NetworklUser networklUser){

        Log.d(provider, networklUser.toString());

        if (!Utils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Consts.LOGIN_SOCIAL;
        Log.d("url", url);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("provider", provider);
            jsonObject.put("email", networklUser.getEmail());
            jsonObject.put("socialID", networklUser.getUserId());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("jsonObjErr", e.getMessage());
        }

        System.out.println(String.valueOf(jsonObject.toString()));


        RequestBody formBody = new FormBody.Builder()
                .add("provider", provider)
                .add("email", networklUser.getEmail())
                .add("socialID", networklUser.getUserId())
                .build();

        OkHttpClient httpClient = CallAPI.getUnsafeOkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .header("Content-Type", "application/json")
                .build();

        pDialog.setMessage("Signing in with " + provider);
        showDialog();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //
                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "Network Error, Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override public void onResponse(Call call, Response response) throws IOException {

                System.out.println("hi there");

                if (!response.isSuccessful()) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        //
                        @Override
                        public void run() {

                            Toast.makeText(getActivity(), "An error occurred, Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    System.out.println("hi there222");
                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        System.out.println("loginResp: " + jsonObject.toString());

                        if (jsonObject.has("login") && jsonObject.getBoolean("login")) {

                            if((jsonObject.has("is_staff") && jsonObject.getBoolean("is_staff"))
                                    || (jsonObject.has("is_superuser") && jsonObject.getBoolean("is_superuser"))){

                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {

                                        Toast.makeText(getActivity(), "You're not permitted to login here", Toast.LENGTH_SHORT).show();

                                    }
                                });

                            }
                            else {

                                final int id = jsonObject.getInt("id");
                                final String name = jsonObject.getString("first_name");
                                String mobileStr = "";
                                if (jsonObject.has("mobile") && jsonObject.get("mobile") != null){

                                    mobileStr = jsonObject.get("mobile").toString();

                                }
                                final String mobile = mobileStr;

                                new Handler(Looper.getMainLooper()).post(new Runnable() {

                                    @Override
                                    public void run() {

                                        Toast.makeText(getActivity(), "Login is successful", Toast.LENGTH_SHORT).show();
                                        editor.putInt("id", id);
                                        editor.putString("socialID", networklUser.getUserId());
                                        editor.putString("username", networklUser.getUserName());
                                        editor.putString("email", networklUser.getEmail());
                                        editor.putString("name", name);
                                        editor.putString("img", networklUser.getProfilePictureUrl());
                                        editor.putBoolean("login", true);

                                        if(mobile.isEmpty()){
                                            editor.apply();
                                            Intent intent = new Intent(getActivity(), CompleteReg.class);
                                            getActivity().startActivity(intent);
                                        }
                                        else {
                                            editor.putString("mobile", mobile);
                                            editor.apply();
                                            Intent intent = new Intent(getActivity(), Home.class);
                                            getActivity().startActivity(intent);
                                            getActivity().finish();
                                        }

                                    }
                                });
                            }

                        } else if (jsonObject.has("errors")) {

                            final String err = jsonObject.get("errors").toString();

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();

                                }
                            });
                        }


                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
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
