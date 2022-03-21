package com.elmahousingfinanceug_test.main_Pages;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Adapters.Branch_List_Adapter;
import com.elmahousingfinanceug_test.recursiveClasses.AllMethods;
import com.elmahousingfinanceug_test.recursiveClasses.ResponseListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ZAtmBranch extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener ,
        ResponseListener {

    private SupportMapFragment fm;
    private TextView b_label;
    private FloatingActionButton list;
    private RecyclerView branch_list;
    private LatLng myLoc;
    private Dialog aDialog;
    private AllMethods am;
    private GoogleMap gMap;
    private LocationManager locationManager;
    private Branch_List_Adapter brAdapter;
    private final ArrayList<String> branch_Set = new ArrayList<>();
    private final ArrayList<Location> location_Set = new ArrayList<>();
    private Boolean mGranted = false, gps_enabled = false, network_enabled = false;
    private final LevelListDrawable lld = new LevelListDrawable();
    private final GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
    private FusedLocationProviderClient mFusedLocationClient;
    private BottomSheetBehavior goBottomSheetBehavior, mBottomSheetBehavior;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int REQUEST_CHECK_SETTINGS_GPS = 0x1, M_PM_LOC = 14;
    private String rush;
    private long now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        am = new AllMethods(this);
        am.disableScreenShot(this);
        setContentView(R.layout.activity_z_atm);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //toolbar.setTitle(getIntent().getStringExtra("title"));
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else {
                    finish();
                }
            }
        });

        fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        NestedScrollView bottomSheetGo = findViewById(R.id.bottom_sheet_go);
        b_label = findViewById(R.id.b_label);
        branch_list = findViewById(R.id.branch_list);
        list = findViewById(R.id.list);

        branch_list.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        branch_list.setLayoutManager(mLayoutManager);
        brAdapter = new Branch_List_Adapter(branch_Set);

        mBottomSheetBehavior = BottomSheetBehavior.from(branch_list);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        lld.addLevel(0, 0, getResources().getDrawable(R.drawable.placeholderdark));
        lld.addLevel(1, 1, getResources().getDrawable(R.drawable.placeholderwhite));
        lld.setLevel(0);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        goBottomSheetBehavior = BottomSheetBehavior.from(bottomSheetGo);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        int status = apiAvailability.isGooglePlayServicesAvailable(getBaseContext());
        if(status != ConnectionResult.SUCCESS) {
            am.ToastMessage(ZAtmBranch.this, getString(R.string.connection));
        } else {
            fm.getMapAsync(this);
        }
    }

    public void loc(View l){
        getLocation();
    }

    private void getLocation() {
        final String [] locationPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
                || (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))) {

            aDialog = new Dialog(this);
            //noinspection ConstantConditions
            aDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            aDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            aDialog.setContentView(R.layout.dialog_confirm);

            final TextView txtTitle = aDialog.findViewById(R.id.dialog_title),
                    txtMessage = aDialog.findViewById(R.id.dialog_message),
                    noBTN = aDialog.findViewById(R.id.noBTN),
                    yesBTN = aDialog.findViewById(R.id.yesBTN);

            txtTitle.setText(getString(R.string.location));
            txtMessage.setText(getString(R.string.permLocation));
            yesBTN.setText(getString(R.string.ok));

            noBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    aDialog.dismiss();
                }
            });

            yesBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(ZAtmBranch.this, locationPermissions, M_PM_LOC);
                    aDialog.dismiss();
                }
            });

            aDialog.show();
        } else {
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(@NonNull LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            if (ContextCompat.checkSelfPermission(ZAtmBranch.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(ZAtmBranch.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, M_PM_LOC);
                            } else {
                                b_label.setText(getString(R.string.loading_ellipsis));
                                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                                gMap.setMyLocationEnabled(true);
                            }
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            gMap.setMyLocationEnabled(false);
                            try {
                                status.startResolutionForResult(ZAtmBranch.this, REQUEST_CHECK_SETTINGS_GPS);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            gMap.setMyLocationEnabled(false);
                            // Location settings are not satisfied.
                            // However, we have no way to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            });
        }
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {

            goBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            for (Location location : locationResult.getLocations()) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                myLoc = new LatLng(latitude, longitude);
            }

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(myLoc)
                    .zoom(12.6f)
                    .bearing(0)
                    .build();
            gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            am.saveLatitude(String.valueOf(myLoc.latitude));
            am.saveLongitude(String.valueOf(myLoc.longitude));

            rush = (
                    "FORMID:O-GetNearestAgent:" +
                            "TYPE:" + "AGENT" + ":" +
                            "BANKID:" + am.getBankID() + ":"
            );
            am.get(ZAtmBranch.this, rush, getString(R.string.gettingNearest),"");

            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    };

    private void locationPermission() {
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            am.ToastMessage(ZAtmBranch.this, getString(R.string.errGpsLoc));
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            am.ToastMessage(ZAtmBranch.this, getString(R.string.errNetworkLoc));
        }
        mGranted = !(!gps_enabled || !network_enabled);
    }

    private Bitmap drawableToBitmap(Drawable d ) {
        Bitmap bitmap;
        if (d instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) d;
            if(bitmapDrawable.getBitmap() != null) return bitmapDrawable.getBitmap();
        }
        if(d.getIntrinsicWidth() <= 0 || d.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);
        return bitmap;
    }

    private void distribute(String values, String step) {
        try {
            String [] branch_array = values.split(";");
            branch_Set.clear();
            for(String aBranch : branch_array) {
                //,0.429681,33.2109,Plot 58 Clive Road,8.00 AM-5.00 PM,0756073000
                String [] in_aBranch = aBranch.split(",");
                if(in_aBranch[1]!=null && !in_aBranch[1].equalsIgnoreCase("0") && in_aBranch[2]!=null && !in_aBranch[2].equalsIgnoreCase("0")){

                    String last;
                    if (in_aBranch.length==7) {
                        last = in_aBranch[6];
                    } else {
                        last = in_aBranch[3];
                    }

                    Marker m1 = gMap.addMarker(new MarkerOptions()
                            .position(new LatLng(Double.parseDouble(in_aBranch[1]), Double.parseDouble(in_aBranch[2])))
                            .title(in_aBranch[3])
                            .icon(BitmapDescriptorFactory.fromBitmap(drawableToBitmap(lld)))
                            .snippet(last));
                    m1.setVisible(true);

                    branch_Set.add(in_aBranch[3]+"|"+last+"|"+in_aBranch[1]+"|"+in_aBranch[2]);
                    brAdapter.notifyDataSetChanged();
                    branch_list.setAdapter(brAdapter);

                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(Double.parseDouble(in_aBranch[1]));
                    location.setLongitude(Double.parseDouble(in_aBranch[2]));
                    location_Set.add(location);
                }
            }

            Location closestLocation = new Location(LocationManager.KEY_LOCATION_CHANGED);
            float smallestDistance = -1;

            if(myLoc!=null && !step.equals("MIR")) {
                for(Location location:location_Set){
                    Location targetLocation = new Location(LocationManager.GPS_PROVIDER);//provider name is unnecessary
                    targetLocation.setLatitude(myLoc.latitude);//your coords of course
                    targetLocation.setLongitude(myLoc.longitude);
                    //float distanceInMeters =  targetLocation.distanceTo(myLocation);
                    float distance  = targetLocation.distanceTo(location);
                    if(smallestDistance == -1 || distance < smallestDistance) {
                        closestLocation = location;
                        smallestDistance = distance;
                    }
                }
                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(new LatLng(myLoc.latitude,myLoc.longitude))
                        .include((new LatLng(closestLocation.getLatitude(), closestLocation.getLongitude())))
                        .build();
                gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
            } else {
                //String [] closest = branch_Set.get(0).split("\\|");

                for(Location location:location_Set){
                    Location targetLocation = new Location(LocationManager.GPS_PROVIDER);
                    targetLocation.setLatitude(Double.parseDouble(am.getLatitude()));
                    targetLocation.setLongitude(Double.parseDouble(am.getLongitude()));
                    //float distanceInMeters =  targetLocation.distanceTo(myLocation);
                    float distance  = targetLocation.distanceTo(location);
                    if(smallestDistance == -1 || distance < smallestDistance) {
                        closestLocation = location;
                        smallestDistance = distance;
                    }
                }

                LatLngBounds bounds = new LatLngBounds.Builder()
                        .include(new LatLng(Double.parseDouble(am.getLatitude()), Double.parseDouble(am.getLongitude())))
                        .include((new LatLng(closestLocation.getLatitude(), closestLocation.getLongitude())))
                        .build();
                gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showHide (View vf){
        am.ToastMessage(this,getString(R.string.zoomtoselect));
        if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public void onGoToNew(double lat,double lon) {
        final LatLng ZOOMTONEW = new LatLng(lat,lon);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ZOOMTONEW)
                .zoom(20)
                .bearing(0)
                .tilt(20)
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onResume() {
        mGoogleApiClient.connect();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationPermission();
        if(mGranted) getLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        am.ToastMessage(this, connectionResult.getErrorMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.map_type, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.mapType) {
            final CharSequence [] items = {getString(R.string.roadMap),getString(R.string.satellite),getString(R.string.hybrid),getString(R.string.terrain)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true)
                    .setTitle(R.string.mapType)
                    .setItems(items, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int item) {
                            switch(item) {
                                case 0:
                                    lld.setLevel(0);
                                    gMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    break;
                                case 1:
                                    lld.setLevel(1);
                                    gMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                                    break;
                                case 2:
                                    lld.setLevel(1);
                                    gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                    break;
                                case 3:
                                    lld.setLevel(0);
                                    gMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                                    break;
                            }
                            dialog.dismiss();
                            gMap.clear();
                            fm.getMapAsync(ZAtmBranch.this);
                            if(mGranted) getLocation();
                        }
                    });
            aDialog = builder.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == M_PM_LOC) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                if (!(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION) && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION))) {
                    am.ToastMessage(this, getString(R.string.locationDenied));
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CHECK_SETTINGS_GPS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    getLocation();
                    break;
                case Activity.RESULT_CANCELED:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        gMap = googleMap;
        if (googleMap != null) {
            if (!mGranted ) {
                LatLng myMark = new LatLng(0.347596,32.582520);
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(myMark));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo( 11.9f));
                goBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                am.saveLatitude(String.valueOf(myMark.latitude));
                am.saveLongitude(String.valueOf(myMark.longitude));

                rush = (
                        "FORMID:O-GetNearestAgent:" +
                                "TYPE:" + "AGENT" + ":" +
                                "BANKID:" + am.getBankID() + ":"
                );
                am.get(ZAtmBranch.this, rush,getString(R.string.findingBankBranch),"MIR");

                /*now = new Date().getTime();
                long diff = (now - am.getA()) / 60 / 60 / 60 / 18;
                if(diff >= 360 || am.getATMB().equals("")) {
                } else {
                    b_label.setText(getString(R.string.getMLoc));
                    distribute(am.getATMB(),"MIR");
                }*/
            }
            //noinspection ConstantConditions
            View locationButton = ((View) fm.getView().findViewById(Integer.parseInt("1")).
                    getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
            rlp.setMargins(0, 150, 10, 0);

            View mapToolbar = ((View) fm.getView().findViewById(Integer.parseInt("1")).
                    getParent()).findViewById(Integer.parseInt("4"));
            RelativeLayout.LayoutParams rlMt = (RelativeLayout.LayoutParams) mapToolbar.getLayoutParams();
            rlMt.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            rlMt.setMargins(0, 0, 10, 150);

            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
            });

            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull  View brRecyclerView, int newState) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        list.setVisibility(View.GONE);
                        rlp.setMargins(0, 0, 10, 420);
                    }
                    else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        list.setVisibility(View.GONE);
                        rlp.setMargins(0, 0, 10, 20);
                    }
                    else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        list.setVisibility(View.VISIBLE);
                        rlp.setMargins(0, 0, 10, 20);
                    }
                }
                @Override
                public void onSlide(@NonNull View brRecyclerView, float slideOffset) {}
            });

        } else {
            finish();
            am.ToastMessageLong(getApplicationContext(),getString(R.string.error));
        }
    }

    @Override
    public void onPause(){
        if(aDialog !=null) aDialog.dismiss();
        if(mFusedLocationClient != null) mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        if(mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    public void onResponse(String response, String step) {
        b_label.setText(getString(R.string.getMLoc));
        branch_Set.clear();
        location_Set.clear();
        distribute(response, step);
        /*if(step.equalsIgnoreCase("MIR")) {
            b_label.setText(getString(R.string.getMLoc));
           *//* am.saveATMB(response);
            am.saveA(String.valueOf(now));*//*
            distribute(response, step);
        } else {
        }*/
    }

    @Override
    protected void onDestroy() {
        mFusedLocationClient.flushLocations();
        if(gMap!=null) gMap.clear();
        super.onDestroy();
    }
}
