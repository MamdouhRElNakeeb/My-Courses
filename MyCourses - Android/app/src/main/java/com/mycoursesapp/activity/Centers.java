package com.mycoursesapp.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.mycoursesapp.helper.CallAPI;
import com.mycoursesapp.R;
import com.mycoursesapp.helper.CachedIconGenerator;
import com.mycoursesapp.helper.Consts;
import com.mycoursesapp.helper.PermissionUtils;
import com.mycoursesapp.model.Center;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Centers extends BaseActivity implements OnMapReadyCallback,
        GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerClickListener,
        LocationListener,
        ActivityCompat.OnRequestPermissionsResultCallback,
        ClusterManager.OnClusterClickListener<Center>,
        ClusterManager.OnClusterInfoWindowClickListener<Center>,
        ClusterManager.OnClusterItemClickListener<Center>,
        ClusterManager.OnClusterItemInfoWindowClickListener<Center>{

    private GoogleMap mMap;

    private static final String TAG = Centers.class.getSimpleName();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    Location nearestStation;
    Location mNearestStation;
    LocationRequest mLocationRequest;

    private final int[] MAP_TYPES = { GoogleMap.MAP_TYPE_SATELLITE,
            GoogleMap.MAP_TYPE_NORMAL,
            GoogleMap.MAP_TYPE_HYBRID,
            GoogleMap.MAP_TYPE_TERRAIN,
            GoogleMap.MAP_TYPE_NONE };
    private int curMapTypeIndex = 1;

    Context context = this;
    private LocationManager locationManager;
    private ProgressDialog pDialog;

    private ClusterManager<Center> mClusterManager;

    private ArrayList<Center> centerArrayList;

    @Override
    protected int getLayoutResourceId() {
        return R.layout.centers_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        setupToolbar();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        initListeners();
        enableMyLocation();
        mClusterManager = new ClusterManager<Center>(this, mMap);
        mClusterManager.setRenderer(new MarkerRenderer());
        mClusterManager.setOnClusterClickListener(this);
        mClusterManager.setOnClusterInfoWindowClickListener(this);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        mMap.setOnCameraIdleListener(mClusterManager);

        displayCenters();
        mClusterManager.cluster();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(16f));

        if (mCurrentLocation != null){
            LatLng latLng1 = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng1));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16f));
        }

        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

//        Intent i = new Intent(this, CenterProfile.class);
//        i.putExtra("centerID", marker.getTitle());
//        startActivity(i);

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        marker.showInfoWindow();
        Intent i = new Intent(this, CenterProfile.class);
        i.putExtra("centerID", Integer.parseInt(marker.getTitle()));
        startActivity(i);

        return true;
    }

    @Override
    public boolean onClusterClick(Cluster<Center> cluster) {
        // Zoom in the cluster. Need to create LatLngBounds and including all the cluster items
        // inside of bounds, then animate to center of the bounds.

        // Create the builder to collect all essential cluster items for the bounds.
        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<Center> cluster) {

    }

    @Override
    public boolean onClusterItemClick(Center center) {
        return true;
    }

    @Override
    public void onClusterItemInfoWindowClick(Center center) {
        Intent i = new Intent(this, CenterProfile.class);
        i.putExtra("centerID", center.id);
        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeListeners();
    }


    @Override
    public void onPause() {
        super.onPause();
        //stop location updates when Activity is no longer active
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }
    }

    @Override
    public void onResume() {

        super.onResume();
    }


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    private void initListeners() {
        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void removeListeners() {
        if( mMap != null ) {
            mMap.setOnMarkerClickListener(null);
            mMap.setOnMapLongClickListener(null);
            mMap.setOnInfoWindowClickListener(null);
            mMap.setOnMapClickListener(null);
        }
    }

    private class MarkerRenderer extends DefaultClusterRenderer<Center> {

        private final IconGenerator mIconGenerator = new CachedIconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new CachedIconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final TextView mClusterTextView;
        private final int mDimension;


        public MarkerRenderer() {
            super(getApplicationContext(), mMap, mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_centers_marker, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.centersClusterIV);
            mClusterTextView = (TextView) multiProfile.findViewById(R.id.centersCountTV);

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            // mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            // mImageView.setPadding(padding, padding, padding, padding);
            mIconGenerator.setContentView(mImageView);

        }

        @Override
        protected void onBeforeClusterItemRendered(Center item, MarkerOptions markerOptions) {
            // mImageView.setImageResource(R.drawable.cluster_icon);
            Bitmap icon = mIconGenerator.makeIcon();
            markerOptions.icon(BitmapDescriptorFactory
                    .fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.marker_center_icn)))
                    .title(String.valueOf(item.id));
//            markerOptions.zIndex(item.id);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<Center> cluster, MarkerOptions markerOptions) {

            mClusterImageView.setImageResource(R.drawable.marker_cluster_icn);
            mClusterTextView.setText(String.valueOf(cluster.getSize()));
            mClusterIconGenerator.setBackground(getResources().getDrawable(R.drawable.marker_cluster_icn));
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            System.out.println("clusterssz " + String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster<Center> cluster) {
            // Always render clusters.
            return cluster.getSize() > 1;
        }
    }

    public void displayCenters() {

        CallAPI callAPI = new CallAPI();
        centerArrayList = new ArrayList<>();

        String url = Consts.CENTERS;

        Log.d("url", url);

        pDialog.setMessage("Loading Near Centers");
        showDialog();

        callAPI.get(url, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Something went wrong


                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {

                        hideDialog();
                        Toast.makeText(Centers.this, "Network Error, Try again", Toast.LENGTH_SHORT).show();
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

                        for (int i = 0; i <jsonArray.length(); i++){

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            double lat = 0, lon = 0;
                            if (jsonObject.has("lat") && !jsonObject.get("lat").toString().equals("null")){
                                lat = jsonObject.getDouble("lat");
                            }
                            if (jsonObject.has("lon") && !jsonObject.get("lon").toString().equals("null")){
                                lon = jsonObject.getDouble("lon");
                            }

                            int id = 0;
                            if (jsonObject.has("user") && !jsonObject.get("user").toString().equals("null")){
                                id = jsonObject.getInt("user");
                            }

                            centerArrayList
                                    .add(new Center(
                                            id,
                                            jsonObject.get("centreName").toString(),
                                            jsonObject.get("info").toString(),
                                            jsonObject.get("address").toString(),
                                            lat,
                                            lon,
                                            jsonObject.get("image").toString()));

                        }

                        new Handler(Looper.getMainLooper()).post(new Runnable() {

                            @Override
                            public void run() {

                                mCurrentLocation = new Location("");
                                mCurrentLocation = mMap.getMyLocation();
                                mNearestStation = new Location("");
                                nearestStation = new Location("");

                                if(centerArrayList.size() != 0) {
                                    mClusterManager.addItems(centerArrayList);

                                    mNearestStation.setLatitude(30.1071329);
                                    mNearestStation.setLongitude(-95.4322685);

                                    //hideDialog();
                                } else {
                                    Toast.makeText(context,"No Centers Found",Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

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

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void setupToolbar(){

        TextView toolbarTV = findViewById(R.id.toolbarTV);

        toolbarTV.setText(R.string.near2);

    }
}
