package com.mycoursesapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mycoursesapp.R;
import com.mycoursesapp.model.Category;
import com.mycoursesapp.model.PromoCode;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/8/18.
 */

public class PromoCodesSpinnerAdapter extends BaseAdapter {

    LayoutInflater inflator;
    private Context context;
    private ArrayList<PromoCode> promoCodeArrayList;

    public PromoCodesSpinnerAdapter(Context context, ArrayList<PromoCode> promoCodeArrayList){
        this.promoCodeArrayList = promoCodeArrayList;
        this.context = context;

        inflator = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return promoCodeArrayList.size() + 1;
    }

    @Override
    public Object getItem(int i) {
        return promoCodeArrayList.get(i);
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

            catTV.setText("Choose promo code and apply");

            return view;
        }
        else {
            final int pos = i - 1;
            view = inflator.inflate(R.layout.promocode_spinner_item, null);

            TextView codeTV = view.findViewById(R.id.codeTV);
            codeTV.setText(promoCodeArrayList.get(pos).code);

            TextView discountTV = view.findViewById(R.id.discountTV);
            discountTV.setText(" : " + String.valueOf(promoCodeArrayList.get(pos).discount) + " % discount");



            return view;
        }


    }
}
