package com.mycoursesapp.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by mamdouhelnakeeb on 2/9/18.
 */

public class Center implements ClusterItem {

    public int id;
    public String name;
//    public String email;
    public String info;
    public String address;
    public double latitude;
    public double longitude;
    public String img;

    public Center(int id, String name, String info, String address, double latitude, double longitude, String img){

        this.id = id;
        this.name = name;
//        this.email = email;
        this.info = info;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.img = img;
    }

    public Center(){

    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }
}
