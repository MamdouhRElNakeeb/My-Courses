package com.mycoursesapp.fragment;

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
import com.mycoursesapp.R;
import com.mycoursesapp.activity.CompleteReg;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

public class Register extends Fragment {

    EditText nameET, emailET, passET;

    Button registerBtn;
    ImageButton fbIB, twtIB, googleIB;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    public static final String FACEBOOK = "facebook";
    public static final String GOOGLE = "google";
    public static final String TWITTER = "twitter";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.register_fragment, container, false);

        nameET = view.findViewById(R.id.nameET);
        emailET = view.findViewById(R.id.emailET);
        passET = view.findViewById(R.id.passET);

        registerBtn = view.findViewById(R.id.registerBtn);
        fbIB = view.findViewById(R.id.fbIB);
        twtIB = view.findViewById(R.id.twtIB);
        googleIB = view.findViewById(R.id.googleIB);

        setupSocialSignup();

        prefs = getActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        editor = prefs.edit();

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    register();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    private void register() throws IOException, JSONException {


        if (!Utils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        final String name = nameET.getText().toString();
        final String email = emailET.getText().toString().trim();
        String password = passET.getText().toString().trim();

        if (name.isEmpty()){
            Toast.makeText(getActivity(),"Please enter your Name", Toast.LENGTH_SHORT).show();
            return;
        }

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

        String url = Consts.SIGN_UP;
        Log.d("url", url);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("first_name", name);
        jsonObject.put("username", email);
        jsonObject.put("password", password);

        System.out.println(String.valueOf(jsonObject.toString()));

        OkHttpClient httpClient = CallAPI.getUnsafeOkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

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

                        if (jsonObject.has("created") && jsonObject.getBoolean("created")) {

                            final int id = jsonObject.getInt("id");
                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "Registration is successful", Toast.LENGTH_SHORT).show();
                                    editor.putInt("id", id);
                                    editor.putString("name", name);
                                    editor.putString("email", email);
                                    editor.apply();

                                    Intent intent = new Intent(getActivity(), CompleteReg.class);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                }
                            });


                        } else if (jsonObject.has("username")) {

                            final String err = "User is already existed";

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

    private void setupSocialSignup(){

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

    private void handleSocialLogin(String provider, NetworklUser networklUser) {

        Log.d(provider, networklUser.toString());


        if (!Utils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Consts.SIGN_UP_SOCIAL;
        Log.d("url", url);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("provider", provider);
            jsonObject.put("socialID", networklUser.getUserId());
            jsonObject.put("email", networklUser.getEmail());
            jsonObject.put("first_name", networklUser.getFullName());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println(String.valueOf(jsonObject.toString()));

        OkHttpClient httpClient = CallAPI.getUnsafeOkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .header("Content-Type", "application/json")
                .build();

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

                        if (jsonObject.has("created") && jsonObject.getBoolean("created")) {

                            final int id = jsonObject.getInt("id");
                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "Registration is successful", Toast.LENGTH_SHORT).show();
                                    editor.putInt("id", id);
                                    editor.putString("socialID", networklUser.getUserId());
                                    editor.putString("name", networklUser.getFullName());
                                    editor.putString("email", networklUser.getEmail());
                                    editor.putString("img", networklUser.getProfilePictureUrl());
                                    editor.putString("provider", provider);
                                    editor.apply();

                                    Intent intent = new Intent(getActivity(), CompleteReg.class);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                }
                            });


                        }
                        else if (jsonObject.has("errors")) {

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

    private void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

//    private void setupSocialSignup(){
//
//        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        final LoginManager loginManager = LoginManager.getInstance();
//
//        fbIB.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
////                loginManager.logInWithReadPermissions(getActivity(), Arrays.asList("public_profile", "email"));
//
//            }
//        });
//
//        loginManager.registerCallback(callbackManager,
//                new FacebookCallback<LoginResult>() {
//                    @Override
//                    public void onSuccess(LoginResult loginResult) {
//                        // TODO: App code
//                        Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                        handleFacebookAccessToken(loginResult.getAccessToken());
//                    }
//
//                    @Override
//                    public void onCancel() {
//                        // App code
//                        Log.d(TAG, "facebook:onCancel");
//                    }
//
//                    @Override
//                    public void onError(FacebookException exception) {
//                        // App code
//                        Log.d(TAG, "facebook:onError:" + exception.getMessage());
//                    }
//                });
//
//
//
//
//        Twitter.initialize(getActivity());
//        TwitterConfig config = new TwitterConfig.Builder(getActivity())
//                .logger(new DefaultLogger(Log.DEBUG))
//                .twitterAuthConfig(new TwitterAuthConfig(getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
//                        getResources().getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET)))
//                .debug(true)
//                .build();
//        Twitter.initialize(config);
//
//        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
//
//        TwitterAuthClient authClient = new TwitterAuthClient();
//        authClient.requestEmail(session, new com.twitter.sdk.android.core.Callback<String>() {
//            @Override
//            public void success(Result<String> result) {
//
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//
//            }
//        });
//
//
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .build();
//
//        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
//    }


//    private void handleFacebookAccessToken(final AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//
//        GraphRequest request = GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response) {
//
//                try {
//
//                    if (object.has("id")) {
//                        Log.d("id", object.getString("id"));
//                    }
//
//                    if (object.has("name")) {
//                        editor.putString("name", object.getString("name"));
//                        Log.d("name", object.getString("name"));
//                    }
//
//                    if (object.has("email")) {
//                        editor.putString("email", object.getString("email"));
//                        Log.d("email", object.getString("email"));
//                    }
//
//                    editor.putString("fbIDCon", "true");
//                    editor.apply();
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        Bundle parameters = new Bundle();
//        parameters.putString("fields", "id,name,email,picture.width(256).height(256)");
//        request.setParameters(parameters);
//        request.executeAsync();
//
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
