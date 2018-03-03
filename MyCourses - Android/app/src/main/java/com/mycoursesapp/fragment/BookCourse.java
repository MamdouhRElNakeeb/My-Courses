package com.mycoursesapp.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mycoursesapp.R;
import com.mycoursesapp.activity.CompleteReg;
import com.mycoursesapp.activity.Home;
import com.mycoursesapp.activity.Promotions;
import com.mycoursesapp.adapter.CategoriesAdapter;
import com.mycoursesapp.adapter.PromoCodesSpinnerAdapter;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.Utils;
import com.mycoursesapp.model.Category;
import com.mycoursesapp.model.PromoCode;
import com.mycoursesapp.model.StartingDate;
import com.mycoursesapp.model.SubCourse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
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

public class BookCourse extends Fragment {

    EditText promoCodeET;
    Button promoCodeBtn, bookBtn, callBtn;
    Spinner startingDatesS, promoCodesS;

    LinearLayout promoCodeLL;
    TextView feesTV, discountTV, finalFeesTV;

    public SubCourse subCourse;

    int discount = 0;

    private ProgressDialog pDialog;


    ArrayList<PromoCode> promoCodeArrayList;
    String centerMobile = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.book_course_fragment, container, false);

        // Progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);

        promoCodeET = view.findViewById(R.id.promoCodeET);
        promoCodeBtn = view.findViewById(R.id.promoCodeBtn);
        bookBtn = view.findViewById(R.id.bookBtn);
        callBtn = view.findViewById(R.id.callBtn);

        startingDatesS = view.findViewById(R.id.startingDatesS);
        promoCodesS = view.findViewById(R.id.promoCodesS);

        promoCodeLL = view.findViewById(R.id.promoCodeLL);

        feesTV = view.findViewById(R.id.feesTV);
        discountTV = view.findViewById(R.id.discountTV);
        finalFeesTV = view.findViewById(R.id.finalFeesTV);

        getUserPromos();
        getCenterMobile();

        ArrayList<String> datesAL = new ArrayList<>();
        datesAL.add("Select course starting date");

        for (int i = 0; i < subCourse.startingDateArrayList.size(); i++) {
            datesAL.add(subCourse.startingDateArrayList.get(i).date);
        }

        ArrayAdapter<String> datesArrAdapter = new ArrayAdapter<String> (getActivity(),
                R.layout.spinner_item, datesAL);
        startingDatesS.setAdapter(datesArrAdapter);


        handleDiscount();

        promoCodeET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().isEmpty()){

                    promoCodeBtn.setEnabled(false);
                    promoCodeBtn.setAlpha((float) 0.7);
                }
                else {
                    promoCodeBtn.setEnabled(true);
                    promoCodeBtn.setAlpha(1);
                }
            }
        });

        promoCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkPromoCode(promoCodeArrayList.get(promoCodesS.getSelectedItemPosition() - 1).code);

            }
        });


        bookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    bookCourse();

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (centerMobile.isEmpty()){
                    getCenterMobile();
                    return;
                }

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + centerMobile));
                startActivity(intent);

            }
        });

        promoCodesS.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here

                if (position == 0){
                    promoCodeBtn.setEnabled(false);
                    promoCodeBtn.setAlpha((float) 0.7);
                }
                else {

                    promoCodeBtn.setEnabled(true);
                    promoCodeBtn.setAlpha(1);
                    discount = promoCodeArrayList.get(position - 1).discount;
                    handleDiscount();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        return view;
    }

    private void handleDiscount(){

        feesTV.setText(String.valueOf(subCourse.fees) + " L.E");
        discountTV.setText(String.valueOf(discount) + " %");
        finalFeesTV.setText(String.valueOf(subCourse.fees - subCourse.fees * discount / 100) + " L.E");

        if (discount != 0){
            promoCodeET.setEnabled(false);
            promoCodeBtn.setEnabled(false);
            promoCodeBtn.setAlpha((float) 0.7);
        }
    }

    private void checkPromoCode(String promoCode){

        pDialog.setMessage("Validating Promo Code");

        showDialog();

        CallAPI callAPI = new CallAPI();

        String url = Consts.PROMO_CODE + promoCode + "/";

        Log.d("url", url);

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();
                        Toast.makeText(getActivity(), "Network Error, Try again", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("response", responseStr);

                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);

                        if (jsonObject.has("discount")){

                            discount = jsonObject.getInt("discount");

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), String.valueOf(discount) + " % discount", Toast.LENGTH_SHORT).show();
                                    handleDiscount();

                                }
                            });
                        }
                        else if (jsonObject.has("errors")) {
                            final String err = jsonObject.getString("errors");

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();
                                    discount = 0;
                                    handleDiscount();
                                }
                            });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                    // Request not successful
                }
                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();

                    }
                });
            }
        });

    }

    private void bookCourse() throws IOException, JSONException {

        if (!Utils.isNetworkConnected(getActivity())){
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
            return;
        }
        if (startingDatesS.getSelectedItemPosition() == 0){
            Toast.makeText(getActivity(),"Please select Starting Date", Toast.LENGTH_SHORT).show();
            return;
        }

        final String startingDate = subCourse.startingDateArrayList.get(startingDatesS.getSelectedItemPosition() - 1).date;

        String url = Consts.BOOKING
                + getActivity().getSharedPreferences("User", Context.MODE_PRIVATE)
                .getInt("id", 0) + "/";

        Log.d("url", url);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("subCourse", subCourse.subCourseID);
        jsonObject.put("startingDate", startingDate);
        if (discount != 0)
            jsonObject.put("promoCode", promoCodeArrayList.get(promoCodesS.getSelectedItemPosition() - 1).id);
        else
            jsonObject.put("promoCode", "");

        System.out.println(String.valueOf(jsonObject.toString()));

        OkHttpClient httpClient = CallAPI.getUnsafeOkHttpClient();

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON, jsonObject.toString());

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    //
                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "Server Error, Try again Later", Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(getActivity(), "An error occurred, Try again", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {

                    try {
                        JSONObject jsonObject = new JSONObject(response.body().string());

                        System.out.println(jsonObject.toString());

                        if (jsonObject.has("booking") && Boolean.parseBoolean(jsonObject.get("booking").toString())) {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "Course is booked successfully", Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(getActivity(), Home.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                    getActivity().finish();
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

                                    Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();

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

                                    Toast.makeText(getActivity(), err, Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                        else {

                            new Handler(Looper.getMainLooper()).post(new Runnable() {

                                @Override
                                public void run() {

                                    Toast.makeText(getActivity(), "An error occurred, Try again later!", Toast.LENGTH_SHORT).show();

                                }
                            });
                        }

                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();

                    }
                });
            }
        });

    }

    public void getUserPromos() {

        CallAPI callAPI = new CallAPI();
        promoCodeArrayList = new ArrayList<>();

        String url = Consts.PROMO_USER
                + String.valueOf(getActivity().getSharedPreferences("User", Context.MODE_PRIVATE).getInt("id", 0)) + "/";

        Log.d("url", url);


        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong


                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "Fetching promo codes failed, Try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("response", responseStr);

                    try {
                        JSONArray jsonArray = new JSONArray(responseStr);

                        for (int i = jsonArray.length() - 1; i > 0; i--){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            JSONObject promoObj = jsonObject.getJSONObject("promoCode");

                            promoCodeArrayList
                                    .add(new PromoCode(
                                            Integer.parseInt(promoObj.get("discount").toString()),
                                            promoObj.get("promoCode").toString(),
                                            Integer.parseInt(promoObj.get("id").toString()))
                                    );

                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {


                                if(promoCodeArrayList.size() != 0) {

                                    promoCodesS.setAdapter(new PromoCodesSpinnerAdapter(getActivity(), promoCodeArrayList));

                                } else {
                                    Toast.makeText(getActivity(), "No Promotions Found",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // Request not successful
                }
            }
        });

    }

    public void getCenterMobile() {

        CallAPI callAPI = new CallAPI();

        String url = Consts.COMPLETE_PROFILE
                + subCourse.centerID + "/";

        Log.d("url", url);


        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong


                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        Toast.makeText(getActivity(), "Fetching center mobile number failed, Try again later", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    Log.d("response", responseStr);

                    try {
                        JSONObject jsonObject = new JSONObject(responseStr);

                        if (jsonObject.has("mobile") && !jsonObject.get("mobile").toString().equals("null"))
                            centerMobile = jsonObject.get("mobile").toString();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // Request not successful
                }
            }
        });

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
