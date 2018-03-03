package com.mycoursesapp.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.applikeysolutions.library.Authentication;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mycoursesapp.R;
import com.mycoursesapp.adapter.CoursesParentAdapter;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.helper.TwtAPIClient;
import com.mycoursesapp.model.Category;
import com.mycoursesapp.model.Center;
import com.mycoursesapp.model.Course;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.internal.oauth.OAuth2Token;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URLEncoder;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static java.security.AccessController.getContext;

/**
 * Created by mamdouhelnakeeb on 2/8/18.
 */

public class Home extends AppCompatActivity {

    Toolbar toolbar;
    ScrollView homeSV;
    EditText searchCourseET;
    ImageButton searchIB;

    RecyclerView coursesRV;
    ArrayList<Course> courseArrayList;

    private ProgressDialog pDialog;

    MaterialSearchBar searchBar;

    ImageView userIV;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.d("Base", "attachBaseContext");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        coursesRV = findViewById(R.id.coursesRV);
        searchCourseET = findViewById(R.id.searchCourseET);
        homeSV = findViewById(R.id.homeSV);
        searchIB = findViewById(R.id.searchIB);
        searchIB.setEnabled(false);

        setupSearchBar();

        findViewById(R.id.recCoursesCV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CoursesRecommended.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.discoverCoursesCV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CategoriesParent.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.langCoursesCV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CoursesParent.class);
                intent.putExtra("filter", "Language");
                startActivity(intent);
            }
        });

        findViewById(R.id.hdCV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CoursesParent.class);
                intent.putExtra("filter", "Human_Development");
                startActivity(intent);
            }
        });

        findViewById(R.id.nearCentersCV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(getBaseContext(), Centers.class);
//                startActivity(intent);

                askForPermission(Manifest.permission.ACCESS_FINE_LOCATION,12);
            }
        });


        searchCourseET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().isEmpty()){

                    homeSV.setVisibility(View.VISIBLE);
                    coursesRV.setVisibility(View.GONE);
                    searchIB.setEnabled(false);
                    searchIB.setAlpha((float) 0.4);
                }
                else {

                    searchIB.setEnabled(true);
                    searchIB.setAlpha((float) 1.0);
                }
            }
        });

        searchIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String searchStr = searchCourseET.getText().toString().trim();

                if (searchStr.isEmpty()){

                    return;
                }

                getCourses(searchStr);
            }
        });
    }

    private void setupCourseRV(){

        coursesRV.setVisibility(View.VISIBLE);
        homeSV.setVisibility(View.GONE);

        coursesRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        CoursesParentAdapter coursesParentAdapter = new CoursesParentAdapter(this, courseArrayList);

        coursesRV.setAdapter(coursesParentAdapter);
    }

    private void handleCoursesJSON(JSONArray jsonArray) throws JSONException {

        courseArrayList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){

            JSONObject courseObj = jsonArray.getJSONObject(i);

            int id = courseObj.getInt("id");
            String name = courseObj.getString("courseName");
            String slogan = courseObj.getString("courseSlogun");
            String img = courseObj.getString("courseImage");

            JSONArray categoriesArr = courseObj.getJSONArray("categories");
            ArrayList<Category> categoryArrayList = new ArrayList<>();

            for (int j = 0; j < categoriesArr.length(); j++){

                int catID = categoriesArr.getJSONObject(j).getInt("id");
                String category = categoriesArr.getJSONObject(j).getString("category");

                categoryArrayList.add(new Category(catID, category));

            }

            courseArrayList.add(new Course(id, name, slogan, img, categoryArrayList));

        }

    }

    private void getCourses(String searchStr){


        pDialog.setMessage("Searching in courses");

        showDialog();

        CallAPI callAPI = new CallAPI();

        String url = Consts.SEARCH + searchStr;

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
                        JSONArray jsonArray = new JSONArray(responseStr);

                        handleCoursesJSON(jsonArray);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {

                                setupCourseRV();

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

    private void setupToolbar(){

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView toolbarTV = findViewById(R.id.toolbarTV);
        ImageButton toolbarIB = findViewById(R.id.toolbarIB);

        setSupportActionBar(toolbar);
//        toolbarTV.setText("My Courses");
        toolbarIB.setImageResource(R.drawable.side_menu_icn);

        setupSideMenu();

        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        toolbarIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    private void setupSideMenu(){

        NavigationView navigationView = findViewById(R.id.navigation_view);

        userIV = navigationView.getHeaderView(0).findViewById(R.id.userIV);

        ((TextView) navigationView.getHeaderView(0)
                .findViewById(R.id.nameTV))
                .setText(getSharedPreferences("User", MODE_PRIVATE).getString("name", ""));

        String userPhoto = getSharedPreferences("User", MODE_PRIVATE).getString("img", "");
        System.out.println("user img" + userPhoto);

        if (!userPhoto.isEmpty()){

            Picasso.with(this)
                    .load(userPhoto)
                    .placeholder(R.drawable.ic_account_circle_48px)
                    .into(userIV);

        }

        // TODO: get social img direct link
//        if (userPhoto.isEmpty()) {
//            getUserSocialPhoto(getSharedPreferences("User", MODE_PRIVATE).getString("socialImg", ""));
//        }
//        else {
//            Picasso.with(this)
//                    .load(userPhoto)
//                    .placeholder(R.drawable.ic_account_circle_48px)
//                    .into(userIV);
//        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Intent intent = new Intent();

                switch (item.getItemId()){

                    case R.id.nav_home:
                        return true;

                    case R.id.nav_bookings:
                        intent = new Intent(getBaseContext(), Bookings.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_certificates:

                        intent = new Intent(getBaseContext(), Certificates.class);
                        startActivity(intent);

                        return true;

                    case R.id.nav_wallet:
                        return true;

                    case R.id.nav_promo:

                        intent = new Intent(getBaseContext(), Promotions.class);
                        startActivity(intent);

                        return true;

                    case R.id.nav_invite:

                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT,
                                "Hey check My Courses app at: " + Consts.APP_URL);
                        intent.setType("text/plain");
                        startActivity(intent);

                        return true;

                    case R.id.nav_settings:
                        intent = new Intent(getBaseContext(), Settings.class);
                        startActivity(intent);
                        return true;

                    case R.id.nav_contact:

                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

                        final View dialogView = getLayoutInflater().inflate(R.layout.contact_dialog, null);
                        builder.setView(dialogView);

                        final AlertDialog alert = builder.create();

                        dialogView.findViewById(R.id.fbIB).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String FACEBOOK_URL = "https://www.facebook.com/MamdouhRElNakeeb";
                                String FACEBOOK_PAGE_ID = "MamdouhRElNakeeb";
                                String facebookUrl = "";
                                PackageManager packageManager = getPackageManager();
                                try {
                                    int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
                                    if (versionCode >= 3002850) { //newer versions of fb app
                                        facebookUrl = "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                                    } else { //older versions of fb app
                                        facebookUrl = "fb://page/" + FACEBOOK_PAGE_ID;
                                    }
                                } catch (PackageManager.NameNotFoundException e) {
                                    facebookUrl =  FACEBOOK_URL; //normal web url
                                }

                                Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
                                facebookIntent.setData(Uri.parse(facebookUrl));
                                startActivity(facebookIntent);
                            }
                        });

                        dialogView.findViewById(R.id.twtIB).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Intent intent = null;
                                try {
                                    // get the Twitter app if possible
                                    getPackageManager().getPackageInfo("com.twitter.android", 0);
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?user_id=MamdouhELNakeeb"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                } catch (Exception e) {
                                    // no Twitter app, revert to browser
                                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/MamdouhElNakeeb"));
                                }
                                startActivity(intent);

                            }
                        });

                        alert.getWindow().setLayout(600, 400);
                        alert.show();

                        return true;

                    case R.id.nav_logout:

                        String provider = getSharedPreferences("User", MODE_PRIVATE).getString("provider", "email");

                        if (provider.equals("facebook")) {
                            Authentication.getInstance().disconnectFacebook();
                        } else if (provider.equals("google")) {
                            Authentication.getInstance().disconnectGoogle();
                        } else if (provider.equals("twitter")) {
                            Authentication.getInstance().disconnectTwitter();
                        }

                        intent = new Intent(getBaseContext(), Login.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        SharedPreferences.Editor editor = getSharedPreferences("User", MODE_PRIVATE).edit();
                        editor.putBoolean("login", false);
                        editor.clear();
                        editor.apply();
                        startActivity(intent);
                        finish();
                        return true;

                    default:
                        return false;
                }
            }
        });

    }


    private void getUserSocialPhoto(String socialImg){

        CallAPI callAPI = new CallAPI();

        SharedPreferences prefs = getSharedPreferences("User", MODE_PRIVATE);
        Long userID = Long.parseLong(prefs.getString("socialID", ""));
        String username = prefs.getString("username", "");

//        String url = Consts.TWT_IMG
//                + "?screen_name=" + username;
//
//        Log.d("url", url);

        Twitter.initialize(this);
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Consts.TWT_CKEY, Consts.TWT_CSECRET))
                .debug(true)
                .build();
        Twitter.initialize(config);

        TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();

        new TwtAPIClient(session).getCustomService().show(userID)
                .enqueue(new com.twitter.sdk.android.core.Callback<User>() {
                    @Override
                    public void success(Result<User> result) {

                        System.out.println("twt imgg" + result.data.profileImageUrl);

                        Picasso.with(Home.this)
                                .load(result.data.profileImageUrl)
                                .resize(250,250)
                                .placeholder(R.drawable.ic_account_circle_48px)
                                .into(userIV);

                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.e("Failed", exception.toString());
                    }
                });


//        String base64EncodedString =null;
//        try {
//            String encodedConsumerKey = URLEncoder.encode(Consts.TWT_CKEY,"UTF-8");
//            String encodedConsumerSecret = URLEncoder.encode(Consts.TWT_CSECRET,"UTF-8");
//            String authString = encodedConsumerKey +":"+encodedConsumerSecret;
//            base64EncodedString = Base64.encodeToString(authString.getBytes("UTF-8"), Base64.NO_WRAP); //Changed here!!!
//        } catch (Exception ex) {
//            //do nothing for now...
//        }
//
//        OkHttpClient okHttpClient = CallAPI.getUnsafeOkHttpClient();
//
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8")
//                .addHeader("Authorization", "Basic " + base64EncodedString)
//                .build();
//
//        System.out.println(request.toString());
//        System.out.println(base64EncodedString);
//
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//
//                new Handler(Looper.getMainLooper()).post(new Runnable() {
//
//                    @Override
//                    public void run() {
//
//                        Toast.makeText(Home.this, "Network Error, Try again", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//
//                if (response.isSuccessful()){
//                    System.out.println(response.body().string());
//                }
//                else {
//                    System.out.println("Faileeeedzz");
//                }
//            }
//        });

    }

    private void setupSearchBar(){

        setupSideMenu();

        searchBar = findViewById(R.id.searchBar);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        searchBar.setPlaceHolder(getResources().getString(R.string.find_course));

        searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {

                String searchStr = text.toString().trim();

                System.out.println(searchStr);

                if (searchStr.isEmpty()){

                    return;
                }

                getCourses(searchStr);

            }

            @Override
            public void onButtonClicked(int buttonCode) {

                switch (buttonCode){
                    case MaterialSearchBar.BUTTON_NAVIGATION:
                        drawerLayout.openDrawer(Gravity.START);
                        break;
                    case MaterialSearchBar.BUTTON_BACK:
                        homeSV.setVisibility(View.VISIBLE);
                        coursesRV.setVisibility(View.GONE);
                        searchBar.disableSearch();
                        break;
                }

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
//            Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Centers.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(ActivityCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED){

            if (requestCode == 12){
                Intent intent = new Intent(this, Centers.class);
                startActivity(intent);
            }

            Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
