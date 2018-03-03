package com.mycoursesapp.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.model.Certificate;
import com.mycoursesapp.model.PromoCode;

import java.util.ArrayList;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class CertificatesAdapter extends RecyclerView.Adapter<CertificatesAdapter.ViewHolder> {

    Context context;
    ArrayList<Certificate> certificateArrayList;

    public CertificatesAdapter(Context context, ArrayList<Certificate> certificateArrayList){

        this.context = context;
        this.certificateArrayList = certificateArrayList;

    }

    @Override
    public CertificatesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.certificate_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Certificate certificate = certificateArrayList.get(position);

        holder.cerNameTV.setText(certificate.name);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    Uri webpage = Uri.parse(certificate.url.trim());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    context.startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request. Please install a web browser or check your URL.",  Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return certificateArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView cerNameTV;

        public ViewHolder(View itemView) {
            super(itemView);

            cerNameTV = itemView.findViewById(R.id.cerNameTV);

        }
    }
}
