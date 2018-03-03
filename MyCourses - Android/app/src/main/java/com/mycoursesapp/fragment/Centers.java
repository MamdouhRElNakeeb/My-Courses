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
import com.mycoursesapp.adapter.CentersAdapter;
import com.mycoursesapp.adapter.CoursesAdapter;
import com.mycoursesapp.model.Center;
import com.mycoursesapp.model.StartingDate;
import com.mycoursesapp.model.SubCourse;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/9/18.
 */

public class Centers extends Fragment {

    RecyclerView recyclerView;

    public ArrayList<SubCourse> subCourseArrayList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.rv_fragment, container, false);

        recyclerView = view.findViewById(R.id.listRV);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        CentersAdapter centersAdapter = new CentersAdapter(getActivity(), subCourseArrayList);
        recyclerView.setAdapter(centersAdapter);

        return view;
    }


}
