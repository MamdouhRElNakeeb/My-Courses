package com.mycoursesapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.activity.Home;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.Utils;
import com.mycoursesapp.model.Booking;
import com.mycoursesapp.model.PromoCode;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.ViewHolder> {

    Context context;
    ArrayList<Booking> bookingArrayList;

    public BookingsAdapter(Context context, ArrayList<Booking> bookingArrayList){

        this.context = context;
        this.bookingArrayList = bookingArrayList;

    }

    @Override
    public BookingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final Booking booking = bookingArrayList.get(position);

        Picasso.with(context)
                .load(Consts.SERVER + booking.courseImage)
                .placeholder(R.drawable.books_bg)
                .into(holder.courseImg);

        holder.centerNameTV.setText(booking.centreName);
        holder.courseNameTV.setText(booking.courseName);
        holder.startingDateTV.setText(booking.startDate);

        holder.closeIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(context);
                }
                builder.setTitle("Cancel booking")
                        .setMessage("Are you sure you want to cancel this booking?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete

                                try {
                                    cacnelCourse(holder.getAdapterPosition());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            }
        });

    }

    @Override
    public int getItemCount() {
        return bookingArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView courseImg;
        TextView courseNameTV, centerNameTV, startingDateTV;
        ImageButton closeIB;

        public ViewHolder(View itemView) {
            super(itemView);

            courseImg = itemView.findViewById(R.id.courseImgIV);
            courseNameTV = itemView.findViewById(R.id.courseNameTV);
            centerNameTV = itemView.findViewById(R.id.centerNameTV);
            startingDateTV = itemView.findViewById(R.id.startingDateTV);
            closeIB = itemView.findViewById(R.id.removeIB);

        }
    }


    private void cacnelCourse(int position) throws IOException, JSONException {

        if (!Utils.isNetworkConnected(context)){
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Consts.BOOKING_CANCEL
                + String.valueOf(bookingArrayList.get(position).id) + "/";

        Log.d("url", url);

        JSONObject jsonObject = new JSONObject();

        System.out.println(String.valueOf(jsonObject.toString()));

        OkHttpClient httpClient = CallAPI.getUnsafeOkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .delete(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //
                    @Override
                    public void run() {

                        Toast.makeText(context, "Server Error, Try again Later", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {


                if (!response.isSuccessful()) {

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        //
                        @Override
                        public void run() {

                            Toast.makeText(context, "An error occurred, Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        System.out.println(jsonObject.toString());

                        if (jsonObject.has("deleted") && Boolean.parseBoolean(jsonObject.get("deleted").toString())) {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(context, "Course is removed successfully", Toast.LENGTH_SHORT).show();
                                    bookingArrayList.remove(position);
                                    notifyDataSetChanged();
                                }
                            });


                        } else if (jsonObject.has("error")){

                            // invalid booking => booked before
                            // promo code is expired
                            // promo code is invalid
                            final String err = jsonObject.getString("error");

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else if (jsonObject.has("errors")){

                            // invalid booking => booked before
                            // promo code is expired
                            // promo code is invalid
                            final String err = jsonObject.getString("errors");

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(context, "An error occurred, Try again later!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }
}
