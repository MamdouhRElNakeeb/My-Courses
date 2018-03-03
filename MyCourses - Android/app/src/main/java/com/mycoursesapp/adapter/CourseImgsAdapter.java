package com.mycoursesapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycoursesapp.R;
import com.mycoursesapp.activity.CourseProfile;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.model.Course;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class CourseImgsAdapter extends RecyclerView.Adapter<CourseImgsAdapter.ViewHolder> {

    Context context;
    ArrayList<String> courseImgsAL;

    public CourseImgsAdapter(Context context, ArrayList<String> courseImgsAL){

        this.context = context;
        this.courseImgsAL = courseImgsAL;

    }

    @Override
    public CourseImgsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_img_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String img = courseImgsAL.get(position);

        System.out.println("image: " + Consts.SERVER + img);
        Picasso.with(context)
                .load(Consts.SERVER + img)
                .placeholder(R.drawable.books_bg)
                .into(holder.courseIV);


    }

    @Override
    public int getItemCount() {
        return courseImgsAL.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        ImageView courseIV;

        public ViewHolder(View itemView) {
            super(itemView);

            courseIV = itemView.findViewById(R.id.courseIV);

        }
    }
}
