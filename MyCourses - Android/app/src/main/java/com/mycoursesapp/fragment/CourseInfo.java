package com.mycoursesapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycoursesapp.R;
import com.mycoursesapp.model.Center;
import com.mycoursesapp.model.Course;
import com.mycoursesapp.model.SubCourse;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class CourseInfo extends Fragment {

    public Course course;
    public SubCourse subCourse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.course_info, container, false);

        TextView infoTV = view.findViewById(R.id.infoTV);
        TextView instructorTV = view.findViewById(R.id.instructorTV);
        TextView startingDateTV = view.findViewById(R.id.startingDateTV);
        TextView feesTV = view.findViewById(R.id.feesTV);

        if (subCourse == null || subCourse.info.isEmpty())
            infoTV.setVisibility(View.GONE);

        infoTV.setText(subCourse.info);
        instructorTV.setText(subCourse.instructorName);
        startingDateTV.setText(subCourse.startingDateArrayList.get(0).date);
        feesTV.setText(String.valueOf(subCourse.fees));

        return view;
    }
}
