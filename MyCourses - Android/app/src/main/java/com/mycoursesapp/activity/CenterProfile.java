package com.mycoursesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.adapter.CoursesAdapter;
import com.mycoursesapp.adapter.VPAdapter;
import com.mycoursesapp.fragment.CenterInfo;
import com.mycoursesapp.fragment.Courses;
import com.mycoursesapp.fragment.Register;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.model.Center;
import com.mycoursesapp.model.StartingDate;
import com.mycoursesapp.model.SubCourse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mamdouhelnakeeb on 2/9/18.
 */

public class CenterProfile extends AppCompatActivity {

    TextView centerNameTV;
    ImageView centerIV;
    TabLayout centerTL;
    ViewPager centersVP;
    ImageButton filterIB;

    ArrayList<SubCourse> subCourseArrayList;
    Center center;

    Courses courses;

    private ProgressDialog pDialog;


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.d("Base", "attachBaseContext");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.center_activity);


        centerNameTV = findViewById(R.id.cNameTV);
        centerIV = findViewById(R.id.centerIV);

        filterIB = findViewById(R.id.filterIB);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("centerID"))
            getCenterData(bundle.getInt("centerID"));

        findViewById(R.id.backLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageButton toolbarIB = findViewById(R.id.toolbarIB);

        if (LocaleManager.getLanguage(this).equals("ar")){
            toolbarIB.setScaleX(-1);
        }

        filterIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CenterProfile.this);

                final View dialogView = getLayoutInflater().inflate(R.layout.subcourses_filter_dialog, null);
                builder.setView(dialogView);

                final AlertDialog alert = builder.create();
                alert.getWindow().setLayout(600, 400);


                dialogView.findViewById(R.id.feesLH).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        filterFeesLowToHigh();
                        alert.dismiss();
                    }
                });

                dialogView.findViewById(R.id.feesHL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        filterFeesHighToLow();
                        alert.dismiss();
                    }
                });

                dialogView.findViewById(R.id.rateLH).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        filterRateLowToHigh();
                        alert.dismiss();
                    }
                });

                dialogView.findViewById(R.id.rateHL).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        filterRateHighToLow();
                        alert.dismiss();
                    }
                });

                alert.show();

            }
        });

    }

    private void filterFeesHighToLow(){

        Collections.sort(subCourseArrayList, SubCourse.feesHL);
        setupViewPager();
    }


    private void filterFeesLowToHigh(){

        Collections.sort(subCourseArrayList, SubCourse.feesLH);
        setupViewPager();
    }

    private void filterRateHighToLow(){

        Collections.sort(subCourseArrayList, SubCourse.rateHL);
        setupViewPager();
    }

    private void filterRateLowToHigh(){

        Collections.sort(subCourseArrayList, SubCourse.rateLH);
        setupViewPager();
    }

    private void setupViewPager(){

        centerTL = findViewById(R.id.centerTL);
        centersVP = findViewById(R.id.centersVP);

        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        courses = new Courses();
        courses.subCourseArrayList = subCourseArrayList;

        CenterInfo centerInfo = new CenterInfo();
        centerInfo.center = center;

        adapter.addFragment(centerInfo, getResources().getString(R.string.info));
        adapter.addFragment(courses, getResources().getString(R.string.courses));
        centersVP.setAdapter(adapter);

        centersVP.setCurrentItem(1);

        centerTL.setupWithViewPager(centersVP);
    }

    private void setupInfo(){

        centerNameTV.setText(center.name);

        Picasso.with(this)
                .load(Consts.SERVER + center.img)
                .placeholder(R.drawable.logo)
                .into(centerIV);
    }

    private void getCourses(JSONArray jsonArray) throws JSONException {

        subCourseArrayList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){

            JSONObject courseObj = jsonArray.getJSONObject(i);
            JSONArray datesArr = courseObj.getJSONArray("dates");
            ArrayList<StartingDate> startingDateArrayList = new ArrayList<>();
            for (int j = 0; j < datesArr.length(); j++){

                String date = datesArr.getJSONObject(j).getString("dates");
                int id = datesArr.getJSONObject(j).getInt("id");

                startingDateArrayList.add(new StartingDate(id, date));
            }
            ArrayList<String> imgAL = new ArrayList<>();
            if (courseObj.has("images")
                    && !courseObj.get("images").toString().equals("null")
                    && courseObj.getJSONArray("images").length() != 0){

                JSONArray imgsArr = courseObj.getJSONArray("images");

                for (int j = 0; j < imgsArr.length(); j++){

                    String img = imgsArr.getJSONObject(j).getString("images");
                    imgAL.add(img);
                }

            }
            else {
                imgAL.add("");
            }

            JSONObject courseParent = courseObj.getJSONObject("course");
            int courseID = courseParent.getInt("id");
            String courseName = courseParent.getString("courseName");

            float rate = 0;
            if (courseObj.has("rate") && !courseObj.get("rate").toString().equals("null"))
                rate = (float) courseObj.getDouble("rate");

            String info = "";
            if (courseObj.has("info") && !courseObj.get("info").toString().equals("null"))
                info = courseObj.get("info").toString();


            int fees = Integer.parseInt(courseObj.getString("fees"));
            String instructorName = courseObj.getString("instructorName");
            int centerID = courseObj.getInt("centre");
            int subCourseID = courseObj.getInt("id");


            subCourseArrayList.add(new SubCourse(
                    courseID, courseName, "", imgAL.get(0), info, subCourseID, centerID, rate, fees, instructorName,
                    startingDateArrayList, imgAL
            ));
        }

    }

    private void getCenterInfo(JSONObject jsonObject) throws JSONException {

        int id = jsonObject.getInt("user");
        double lat = 0, lon= 0;
        if(jsonObject.has("lat") && !jsonObject.get("lat").toString().equals("null"))
            lat = jsonObject.getDouble("lat");
        if(jsonObject.has("lon") && !jsonObject.get("lon").toString().equals("null"))
            lat = jsonObject.getDouble("lon");

        String centerName = jsonObject.getString("centreName");
        String info = jsonObject.getString("info");
        String address = jsonObject.getString("address");
        String img = jsonObject.getString("image");

        center = new Center(id, centerName, info, address, lat, lon, img);


    }

    private void getCenterData(int centerID){

        pDialog.setMessage("Loading center details");

        showDialog();

        CallAPI callAPI = new CallAPI();
        subCourseArrayList = new ArrayList<>();

        String url = Consts.CENTERS_COURSES + centerID + "/";

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

                        JSONObject centerObj = jsonObject.getJSONObject("info");
                        JSONArray coursesArr = jsonObject.getJSONArray("courses");

                        getCourses(coursesArr);
                        getCenterInfo(centerObj);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {

                                setupViewPager();
                                setupInfo();

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

}
