package com.mycoursesapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mycoursesapp.R;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.model.PromoCode;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class PromoCodesAdapter extends RecyclerView.Adapter<PromoCodesAdapter.ViewHolder> {

    Context context;
    ArrayList<PromoCode> promoCodeArrayList;

    public PromoCodesAdapter(Context context, ArrayList<PromoCode> promoCodeArrayList){

        this.context = context;
        this.promoCodeArrayList = promoCodeArrayList;

    }

    @Override
    public PromoCodesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.promo_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final PromoCode promoCode = promoCodeArrayList.get(position);

        if (promoCode.discount < 20){
            holder.promoBgIV.setImageResource(R.drawable.promo3);
        }
        else if (promoCode.discount < 50){
            holder.promoBgIV.setImageResource(R.drawable.promo2);
        }
        else {
            holder.promoBgIV.setImageResource(R.drawable.promo1);
        }

        holder.discountTV.setText(String.valueOf(promoCode.discount) + " %");
        holder.promoCodeTV.setText(promoCode.code);

    }

    @Override
    public int getItemCount() {
        return promoCodeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView promoBgIV;
        TextView promoCodeTV, discountTV;

        public ViewHolder(View itemView) {
            super(itemView);

            promoBgIV = itemView.findViewById(R.id.promoBgIV);
            promoCodeTV = itemView.findViewById(R.id.promoCodeTV);
            discountTV = itemView.findViewById(R.id.discountTV);

        }
    }
}
