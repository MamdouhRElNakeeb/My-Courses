package com.mycoursesapp.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.adapter.CoursesParentAdapter;
import com.mycoursesapp.adapter.DiscoverCategoriesAdapter;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
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

public class CategoriesParent extends BaseActivity {

    RecyclerView coursesRV;
    ArrayList<Category> categoryArrayList;

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


        getCategories();

        setupToolbar();
    }

    private void setupToolbar(){

        TextView toolbarTV = findViewById(R.id.toolbarTV);

        toolbarTV.setText(R.string.discover2);
    }

    private void setupCategoriesRV(){

        coursesRV = findViewById(R.id.coursesRV);

//        coursesRV.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        coursesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DiscoverCategoriesAdapter discoverCategoriesAdapter = new DiscoverCategoriesAdapter(this, categoryArrayList);

        coursesRV.setAdapter(discoverCategoriesAdapter);
    }

    private void getCategories(){

        CallAPI callAPI = new CallAPI();
        categoryArrayList = new ArrayList<>();

        pDialog.setMessage("Loading Categories");
        showDialog();
        String url = Consts.CATEGORIES;

        Log.d("url", url);

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(CategoriesParent.this, "Network Error, Try again", Toast.LENGTH_SHORT).show();
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

                            String img = "";
                            if (jsonObject.has("img") && !jsonObject.get("img").toString().equals("null"))
                                img = jsonObject.get("img").toString();

                            categoryArrayList.add(new Category(Integer.parseInt(jsonObject.get("id").toString()), jsonObject.get("category").toString(), img));

                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {

                                setupCategoriesRV();

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
