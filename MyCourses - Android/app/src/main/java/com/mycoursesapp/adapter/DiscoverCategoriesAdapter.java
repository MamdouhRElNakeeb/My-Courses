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
import com.mycoursesapp.activity.CoursesParent;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.model.Category;
import com.mycoursesapp.model.Course;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class DiscoverCategoriesAdapter extends RecyclerView.Adapter<DiscoverCategoriesAdapter.ViewHolder> {

    Context context;
    ArrayList<Category> categoryArrayList;

    public DiscoverCategoriesAdapter(Context context, ArrayList<Category> categoryArrayList){

        this.context = context;
        this.categoryArrayList = categoryArrayList;

    }

    @Override
    public DiscoverCategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_parent_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Category category = categoryArrayList.get(position);

        String catName = category.name.replace("_", " ");

        holder.courseNameTV.setText(catName);

        System.out.println("image: " + Consts.SERVER + category.img);
        Picasso.with(context)
                .load(Consts.SERVER + category.img)
                .placeholder(R.drawable.bg1)
                .into(holder.courseIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CoursesParent.class);
                intent.putExtra("filter", category.name);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
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
