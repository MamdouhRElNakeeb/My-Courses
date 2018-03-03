package com.mycoursesapp.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mycoursesapp.R;
import com.mycoursesapp.model.Center;

/**
 * Created by mamdouhelnakeeb on 2/10/18.
 */

public class CenterInfo extends Fragment {

    public Center center;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.center_info, container, false);

        TextView infoTV = view.findViewById(R.id.infoTV);
        TextView addressTV = view.findViewById(R.id.addressTV);

        infoTV.setText(center.info);
        addressTV.setText(center.address);

        return view;
    }
}
