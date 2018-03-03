package com.mycoursesapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.adapter.CoursesParentAdapter;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.LocaleManager;
import com.mycoursesapp.model.Category;
import com.mycoursesapp.model.Course;

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

public class CoursesRecommended extends BaseActivity {

    RecyclerView coursesRV, coursesTrendRV;
    ArrayList<Course> courseArrayList, courseTrendArrayList;

    private ProgressDialog pDialog;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.courses_activity;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        setupToolbar();
        getCourses();
    }

    private void setupToolbar(){

        TextView toolbarTV = findViewById(R.id.toolbarTV);

        toolbarTV.setText(R.string.recommended2);

    }

    private void setupCourseTrendRV(){

        coursesTrendRV = findViewById(R.id.coursesTrendRV);

        if (courseTrendArrayList.size() == 0){
            coursesTrendRV.setVisibility(View.GONE);
        }
        else {
            coursesTrendRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
            CoursesParentAdapter coursesParentAdapter = new CoursesParentAdapter(this, courseTrendArrayList);

            coursesTrendRV.setAdapter(coursesParentAdapter);
        }
    }

    private void setupCourseRV(){

        coursesRV = findViewById(R.id.coursesRV);

        coursesRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        CoursesParentAdapter coursesParentAdapter = new CoursesParentAdapter(this, courseArrayList);

        coursesRV.setAdapter(coursesParentAdapter);
    }

    private void handleCoursesJSON(JSONArray jsonArray) throws JSONException {

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

    private void getCourses(){

        pDialog.setMessage("Loading courses");

        showDialog();

        CallAPI callAPI = new CallAPI();
        courseArrayList = new ArrayList<>();
        courseTrendArrayList = new ArrayList<>();

        String url = Consts.RECOMMENDED
                + String.valueOf(getSharedPreferences("User", MODE_PRIVATE).getInt("id", 0)) + "/";

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

    private void handleTrendCoursesJSON(JSONArray jsonArray) throws JSONException {

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

    private void getTrendCourses(){

        CallAPI callAPI = new CallAPI();
        courseArrayList = new ArrayList<>();
        courseTrendArrayList = new ArrayList<>();

        String url = Consts.COURSES;

        Log.d("url", url);

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

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

                        handleTrendCoursesJSON(jsonArray);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {

                                setupCourseTrendRV();

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
