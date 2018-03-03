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
import com.mycoursesapp.activity.BookCourse;
import com.mycoursesapp.activity.CenterProfile;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.model.Center;
import com.mycoursesapp.model.StartingDate;
import com.mycoursesapp.model.SubCourse;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class CentersAdapter extends RecyclerView.Adapter<CentersAdapter.ViewHolder> {

    Context context;
    ArrayList<SubCourse> subCourseArrayList;

    public CentersAdapter(Context context, ArrayList<SubCourse> subCourseArrayList){

        this.context = context;
        this.subCourseArrayList = subCourseArrayList;

    }

    @Override
    public CentersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.center_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CentersAdapter.ViewHolder holder, int position) {

        final SubCourse subCourse = subCourseArrayList.get(position);

        holder.centerNameTV.setText(subCourse.slogan); // TODO: fix this from API
        holder.instructorNameTV.setText(subCourse.instructorName);

        if (subCourse.startingDateArrayList.size() != 0)
            holder.startingDateTV.setText(subCourse.startingDateArrayList.get(0).date);
        else
            holder.startingDateTV.setText("");
        holder.rateTV.setText(String.valueOf(subCourse.rate));
        holder.feesTV.setText(String.valueOf(subCourse.fees) + " L.E");

//        System.out.println("image: " + Consts.SERVER + subCourse.imagesAL.get(0));
//        Picasso.with(context)
//                .load(Consts.SERVER + subCourse.imagesAL.get(0))
//                .placeholder(R.drawable.logo)
//                .into(holder.courseIV);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, CenterProfile.class);
                intent.putExtra("centerID", subCourse.centerID);
                context.startActivity(intent);
            }
        });

        holder.bookIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, BookCourse.class);
                intent.putExtra("subCourseID", subCourse.subCourseID);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCourseArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView centerNameTV, instructorNameTV, startingDateTV, rateTV, feesTV;

        ImageView courseIV;
        ImageButton bookIB;

        public ViewHolder(View itemView) {
            super(itemView);

            centerNameTV = itemView.findViewById(R.id.centerNameTV);
            instructorNameTV = itemView.findViewById(R.id.instructorTV);
            startingDateTV = itemView.findViewById(R.id.startingDateTV);
            rateTV = itemView.findViewById(R.id.rateTV);
            feesTV = itemView.findViewById(R.id.feesTV);
            courseIV = itemView.findViewById(R.id.centerIV);
            bookIB = itemView.findViewById(R.id.bookIB);

        }
    }
}
