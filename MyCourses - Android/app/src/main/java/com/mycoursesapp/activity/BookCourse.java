package com.mycoursesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.adapter.CourseImgsAdapter;
import com.mycoursesapp.adapter.VPAdapter;
import com.mycoursesapp.fragment.CenterInfo;
import com.mycoursesapp.fragment.CourseInfo;
import com.mycoursesapp.fragment.Courses;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.model.Course;
import com.mycoursesapp.model.StartingDate;
import com.mycoursesapp.model.SubCourse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class BookCourse extends BaseActivity {

    TextView courseNameTV, courseSloganTV;
    RecyclerView courseImgsRV;
    CourseImgsAdapter courseImgsAdapter;

    private ProgressDialog pDialog;
    SubCourse subCourse;

    TabLayout courseTL;
    ViewPager bookCourseVP;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.book_course_activity;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        setupToolbar();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null && bundle.containsKey("subCourseID"))
            getSubCourseData(bundle.getInt("subCourseID"));
        else{

            finish();
        }
    }



    private void setupToolbar(){

        TextView toolbarTV = findViewById(R.id.toolbarTV);
        toolbarTV.setText(R.string.book);

    }

    private void setupInfo(){

        courseNameTV = findViewById(R.id.cNameTV);
        courseSloganTV = findViewById(R.id.cSloganTV);
        courseImgsRV = findViewById(R.id.courseImgsRV);

        courseNameTV.setText(subCourse.name);
        courseSloganTV.setText(subCourse.slogan);

        courseImgsRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        courseImgsAdapter = new CourseImgsAdapter(this, subCourse.imagesAL);
        courseImgsRV.setAdapter(courseImgsAdapter);

        if (subCourse.imagesAL.size() > 0)
            autoScrollRV();
    }

    private void setupViewPager(){

        courseTL = findViewById(R.id.courseTL);
        bookCourseVP = findViewById(R.id.coursesVP);

        VPAdapter adapter = new VPAdapter(getSupportFragmentManager());
        com.mycoursesapp.fragment.BookCourse bookCourse = new com.mycoursesapp.fragment.BookCourse();
        bookCourse.subCourse = subCourse;

        CourseInfo courseInfo = new CourseInfo();
        courseInfo.subCourse = subCourse;

        adapter.addFragment(courseInfo, "Info");
        adapter.addFragment(bookCourse, "Book");
        bookCourseVP.setAdapter(adapter);

        bookCourseVP.setCurrentItem(1);

        courseTL.setupWithViewPager(bookCourseVP);
    }

    private void getCourseDetails(JSONArray jsonArray) throws JSONException {

        subCourse = new SubCourse();

        if (jsonArray.length() == 1) {

            JSONObject courseObj = jsonArray.getJSONObject(0);
            JSONArray datesArr = courseObj.getJSONArray("dates");
            ArrayList<StartingDate> startingDateArrayList = new ArrayList<>();
            for (int j = 0; j < datesArr.length(); j++) {

                String date = datesArr.getJSONObject(j).getString("dates");
                int id = datesArr.getJSONObject(j).getInt("id");

                startingDateArrayList.add(new StartingDate(id, date));
            }
            if (startingDateArrayList.size() == 0)
                startingDateArrayList.add(new StartingDate(0, ""));

            JSONArray imgsArr = courseObj.getJSONArray("images");
            ArrayList<String> imgAL = new ArrayList<>();
            for (int j = 0; j < imgsArr.length(); j++) {

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
            int subCourseID = courseObj.getInt("id");

            subCourse = new SubCourse(
                    courseID, courseName, "", imgAL.get(0), info, subCourseID, centerID, rate, fees, instructorName,
                    startingDateArrayList, imgAL
            );
        }
    }

    private void getSubCourseData(int subCourseID){

        pDialog.setMessage("Loading course details");

        showDialog();

        CallAPI callAPI = new CallAPI();

        String url = Consts.SUB_COURSES + subCourseID + "/";

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
                        JSONArray jsonArray = new JSONArray(responseStr);


                        getCourseDetails(jsonArray);

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

    private void autoScrollRV(){

        final int speedScroll = 3000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;
            @Override
            public void run() {
                if(count < courseImgsAdapter.getItemCount()){
                    if(count == courseImgsAdapter.getItemCount()-1){
                        flag = false;
                    }else if(count == 0){
                        flag = true;
                    }
                    if(flag) count++;
                    else count--;

                    courseImgsRV.scrollToPosition(count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };

        handler.postDelayed(runnable, speedScroll);

    }
}
