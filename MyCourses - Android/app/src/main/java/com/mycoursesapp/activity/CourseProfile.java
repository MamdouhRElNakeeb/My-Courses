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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.adapter.VPAdapter;
import com.mycoursesapp.fragment.*;
import com.mycoursesapp.fragment.Centers;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.model.Category;
import com.mycoursesapp.model.Center;
import com.mycoursesapp.model.Course;
import com.mycoursesapp.model.StartingDate;
import com.mycoursesapp.model.SubCourse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mamdouhelnakeeb on 2/9/18.
 */

public class CourseProfile extends AppCompatActivity {

    TextView courseNameTV, courseSloganTV;
    ImageView courseIV;
    TabLayout courseTL;
    ViewPager coursesVP;
    ImageButton filterIB;

    ArrayList<SubCourse> subCourseArrayList;
    Course course;

    private ProgressDialog pDialog;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
        Log.d("Base", "attachBaseContext");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.course_activity);

        filterIB = findViewById(R.id.filterIB);

        courseNameTV = findViewById(R.id.cNameTV);
        courseSloganTV = findViewById(R.id.cSloganTV);
        courseIV = findViewById(R.id.courseIV);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("courseID"))
            getCourseData(bundle.getInt("courseID"));


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

                AlertDialog.Builder builder = new AlertDialog.Builder(CourseProfile.this);

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

        courseTL = findViewById(R.id.courseTL);
        coursesVP = findViewById(R.id.coursesVP);

        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        com.mycoursesapp.fragment.Centers centers = new Centers();
        centers.subCourseArrayList = subCourseArrayList;

//        CourseInfo courseInfo = new CourseInfo();
//        courseInfo.course = course;

//        adapter.addFragment(courseInfo, "Info");
        adapter.addFragment(centers, getResources().getString(R.string.centers));
        coursesVP.setAdapter(adapter);

        coursesVP.setCurrentItem(0);

        courseTL.setupWithViewPager(coursesVP);
    }

    private void setupInfo(){

        courseNameTV.setText(course.name);
        courseSloganTV.setText(course.slogan);

        Picasso.with(this)
                .load(Consts.SERVER + course.img)
                .placeholder(R.drawable.logo)
                .into(courseIV);
    }

    private void getCenters(JSONArray jsonArray) throws JSONException {

        subCourseArrayList = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i++){

            JSONObject courseObj = jsonArray.getJSONObject(i);
            JSONArray datesArr = courseObj.getJSONArray("dates");
            ArrayList<StartingDate> startingDateArrayList = new ArrayList<>();
            for (int j = 0; j < datesArr.length(); j++){

                String date = datesArr.getJSONObject(j).getString("dates");
                int id = datesArr.getJSONObject(j).getInt("id");

                System.out.println(date);
                startingDateArrayList.add(new StartingDate(id, date));
            }

            JSONArray imgsArr = courseObj.getJSONArray("images");
            ArrayList<String> imgAL = new ArrayList<>();
            for (int j = 0; j < imgsArr.length(); j++){

                String img = imgsArr.getJSONObject(j).getString("images");
                imgAL.add(img);
            }

            if (imgAL.size() == 0)
                imgAL.add("");

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
            String centerName = courseObj.getString("centreName");
            int subCourseID = courseObj.getInt("id");


            subCourseArrayList.add(new SubCourse(
                    courseID, courseName, centerName, imgAL.get(0), info, subCourseID, centerID, rate, fees, instructorName,
                    startingDateArrayList, imgAL, datesArr.toString()
            ));
        }

    }

    private void getCourseInfo(JSONObject jsonObject) throws JSONException {

        int id = jsonObject.getInt("id");

        JSONArray categoriesArr = jsonObject.getJSONArray("categories");
        ArrayList<Category> categoryArrayList = new ArrayList<>();

        for (int j = 0; j < categoriesArr.length(); j++){

            int catID = categoriesArr.getJSONObject(j).getInt("id");
            String category = categoriesArr.getJSONObject(j).getString("category");

            categoryArrayList.add(new Category(catID, category));

        }

        String courseName = jsonObject.getString("courseName");
        String slogan = jsonObject.getString("courseSlogun");
        String img = jsonObject.getString("courseImage");

        course = new Course(id, courseName, slogan, img, categoryArrayList);

    }

    private void getCourseData(int courseID){

        pDialog.setMessage("Loading course details");

        showDialog();

        CallAPI callAPI = new CallAPI();

        String url = Consts.COURSES + "/" + courseID + "/";

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
                        finish();
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

                        JSONObject courseObj = jsonObject.getJSONObject("info");
                        JSONArray centresArr = jsonObject.getJSONArray("subCourses");

                        getCenters(centresArr);
                        getCourseInfo(courseObj);

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
