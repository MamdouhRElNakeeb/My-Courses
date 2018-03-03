package com.mycoursesapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mycoursesapp.R;
import com.mycoursesapp.model.Category;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/8/18.
 */

public class CategoriesAdapter extends BaseAdapter {

    LayoutInflater inflator;
    private Context context;
    private ArrayList<Category> categoryArrayList;

    public CategoriesAdapter(Context context, ArrayList<Category> categoryArrayList){
        this.categoryArrayList = categoryArrayList;
        this.context = context;

        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return categoryArrayList.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return categoryArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (i == 0){
            view = inflator.inflate(android.R.layout.simple_spinner_item, null);

            TextView catTV = view.findViewById(android.R.id.text1);

            catTV.setText("Choose Fields of study");

            return view;
        }
        else {
            final int pos = i - 1;
            view = inflator.inflate(R.layout.category_item, null);

            String catName = categoryArrayList.get(pos).name.replace("_", " ");
            TextView catTV = view.findViewById(R.id.catTV);
            catTV.setText(catName);

            CheckBox catCB = view.findViewById(R.id.catCB);

            catCB.setChecked(categoryArrayList.get(pos).selected);

            catCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    categoryArrayList.get(pos).selected = b;
                }
            });

            return view;
        }

    }
}
