package com.mycoursesapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mycoursesapp.R;
import com.mycoursesapp.activity.CenterProfile;
import com.mycoursesapp.adapter.CoursesAdapter;
import com.mycoursesapp.model.Course;
import com.mycoursesapp.model.SubCourse;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/9/18.
 */

public class Courses extends Fragment {

    RecyclerView recyclerView;

    public ArrayList<SubCourse> subCourseArrayList;
    CoursesAdapter coursesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rv_fragment, container, false);

        recyclerView = view.findViewById(R.id.listRV);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        coursesAdapter = new CoursesAdapter(getActivity(), subCourseArrayList);
        recyclerView.setAdapter(coursesAdapter);

        return view;
    }

}
