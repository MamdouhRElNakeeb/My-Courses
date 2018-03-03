package com.mycoursesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycoursesapp.R;
import com.mycoursesapp.activity.CourseProfile;
import com.mycoursesapp.fragment.Courses;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.model.Course;
import com.mycoursesapp.model.SubCourse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class CoursesParentAdapter extends RecyclerView.Adapter<CoursesParentAdapter.ViewHolder> {

    Context context;
    ArrayList<Course> courseArrayList;

    public CoursesParentAdapter(Context context, ArrayList<Course> courseArrayList){

        this.context = context;
        this.courseArrayList = courseArrayList;

    }

    @Override
    public CoursesParentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_parent_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Course course = courseArrayList.get(position);

        holder.courseNameTV.setText(course.name);

        System.out.println("image: " + Consts.SERVER + course.img);
        Picasso.with(context)
                .load(Consts.SERVER + course.img)
                .placeholder(R.drawable.logo)
                .into(holder.courseIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CourseProfile.class);
                intent.putExtra("courseID", course.id);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return courseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView courseNameTV;

        ImageView courseIV;

        public ViewHolder(View itemView) {
            super(itemView);

            courseNameTV = itemView.findViewById(R.id.courseNameTV);
            courseIV = itemView.findViewById(R.id.courseIV);

        }
    }
}
