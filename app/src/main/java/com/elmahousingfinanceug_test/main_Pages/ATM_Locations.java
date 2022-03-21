package com.elmahousingfinanceug_test.main_Pages;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elmahousingfinanceug_test.R;
import com.elmahousingfinanceug_test.main_Pages.Adapters.Branch_List_Adapter;
import com.elmahousingfinanceug_test.recursiveClasses.BaseAct;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ATM_Locations extends BaseAct implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    GoogleMap mMap;
    RelativeLayout.LayoutParams rLp;
    SupportMapFragment fm;
    FloatingActionButton list,rMap;
    RecyclerView atm_list;
    AlertDialog l;
    LocationManager locationManager;
    Branch_List_Adapter brAdapter;
    ArrayList<String> atm_Set = new ArrayList<>();
    float zoomIn = 12.7f;
    Boolean mGranted = false, gps_enabled = false, network_enabled = false;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FusedLocationProviderClient mFusedLocationClient;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static final int M_LOC = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setContentView(R.layout.activity_atm);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(getString(R.string.atmLocations));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                } else {
                    finish();
                }
            }
        });

        atm_list = findViewById(R.id.atm_list);
        rMap = findViewById(R.id.rMap);
        list = findViewById(R.id.list);

        atm_list.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(ATM_Locations.this);
        atm_list.setLayoutManager(mLayoutManager);
        brAdapter = new Branch_List_Adapter(atm_Set);

        mBottomSheetBehavior = BottomSheetBehavior.from(atm_list);
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(30000)
                .setFastestInterval(15000);

        rMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationPermission();
                getLocation();
            }
        });

        int status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        if (status != ConnectionResult.SUCCESS) {
            am.ToastMessage(this, getString(R.string.connection));
        } else {
            fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            assert fm != null;
            fm.getMapAsync(this);
        }
    }

    private void locationPermission() {
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            am.ToastMessage(this, getString(R.string.errGpsLoc));
        }

        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            am.ToastMessage(this, getString(R.string.errNetworkLoc));
        }

        mGranted = !(!gps_enabled || !network_enabled);
    }

    private void getLocation() {
        final String[] locationPermissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setCancelable(true)
                        .setTitle(R.string.locationAccess)
                        .setMessage(R.string.permLocation)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions(ATM_Locations.this, locationPermissions, M_LOC);
                                dialog.dismiss();
                            }
                        })
                        .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                l = builder.show();
            } else {
                ActivityCompat.requestPermissions(this, locationPermissions, M_LOC);
            }
        } else {
            if (mGranted) {
                try {
                    WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    assert wifi != null;
                    if (!wifi.isWifiEnabled()) wifi.setWifiEnabled(true);
                } catch (Exception e) {
                    am.LogThis("WiFi Error : " + e.getMessage());
                }
                mMap.setMyLocationEnabled(true);
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                rMap.setVisibility(View.GONE);
            } else {
                mMap.setMyLocationEnabled(false);
                rMap.setVisibility(View.VISIBLE);
                promptAccuracy();
            }
        }
    }

    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                onGoToNew(latitude,longitude);
                am.saveLatitude(String.valueOf(latitude));
                am.saveLongitude(String.valueOf(longitude));
            }
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        }
    };

    private void promptAccuracy() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true)
                .setTitle(R.string.locationAccuracy)
                .setMessage(R.string.enableGps)
                .setPositiveButton(getString(R.string.settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        dialog.dismiss();
                    }
                })
                .setNeutralButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        l = builder.show();
    }

    public void showHide(View vf) {
        am.ToastMessage(ATM_Locations.this, getString(R.string.zoomtoselect));
        if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        } else {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
    }

    public void onGoToNew(double lat, double lon) {
        final LatLng ZOOMTONEW = new LatLng(lat, lon);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(ZOOMTONEW)
                .zoom(20)
                .bearing(0)// Sets the orientation of the camera to east
                .tilt(20) // Sets the tilt of the camera to 20 degrees
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            LatLng mymarker = new LatLng(0.313179, 32.582606);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(mymarker));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoomIn));

            //noinspection ConstantConditions
            View mapToolbar = ((View) fm.getView().findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("4"));
            rLp = (RelativeLayout.LayoutParams) mapToolbar.getLayoutParams();
            rLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);

            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull  View brRecyclerView, int newState) {
                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                        list.setVisibility(View.GONE);
                        rLp.setMargins(0, 0, 10, 420);
                    }
                    else if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        list.setVisibility(View.GONE);
                        rLp.setMargins(0, 0, 10, 20);
                    }
                    else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        list.setVisibility(View.VISIBLE);
                        rLp.setMargins(0, 0, 10, 20);
                    }
                }
                @Override
                public void onSlide(@NonNull View brRecyclerView, float slideOffset) {}
            });

            String names = "Kampala Road~" +
                    "Namuwongo~" +
                    "Nakasero~" +
                    "Ntinda~" +
                    "Kololo~" +
                    "Garden City~" +
                    "Kikuubo~" +
                    "Ovino~" +
                    "Ndeeba~" +
                    "Najjanankumbi~" +
                    "Mukono~" +
                    "Nakawa~" +
                    "Bugolobi~" +
                    "Lubowa~" +
                    "Entebbe~" +
                    "Kabalagala~" +
                    "Kireka~" +
                    "Quality supermarket, Naalya~" +
                    "Nansana~" +
                    "Mbale~" +
                    "Mbale~" +
                    "Mbarara~" +
                    "Arua~";

            String descriptions =
                    "Plot 25, Investment House opposite Entebbe road traffic lights~" +
                            "Plot 38, Kisugu Road~" +
                            "Plot 34A, Nakasero Road opposite the Nigerian High Commission~" +
                            "Ground floor, Plot 1, Kimera Road~" +
                            "Plot 4, Wampewo Avenue, Kololo opposite Air strip~" +
                            "Plot 64/86 2nd Upper Level, Yusuf Lule Road~" +
                            "plot 54-56 William street, muzinge hotel building near Arua Park~" +
                            "Plot 22, Kibuga Block 12 Ovino Market Mall-Kisenyi~" +
                            "Plot 94/95/96 Masaka Road~" +
                            "Plot 1032, Pelican House, Najjanakumbi~" +
                            "Plot 51, Kampala Rd, Mukono~" +
                            "Capital Shoppers Supermarket Building~" +
                            "Spring road, Bugolobi Middle eas~" +
                            "Plot 1620-1622 Lubowa Estate; Quality Shopping Village Lubowa~" +
                            "Plot 23-25 Kampala road Entebbe next to NWSC offices~" +
                            "Kabalagala Trading Centre near Equity Bank~" +
                            "Near Total Kireka~" +
                            "Plot 2447 Kyaliwajjala Road; Quality Shopping Mall Namugongo~" +
                            "Plot 1158 Hoima Road Njovu Estate Head Office Building ~" +
                            "Plot 2,court road Bugishu Cooperative Union House~" +
                            "Housing Finance Mbale~" +
                            "Classic Hotel Building, Plot 57, High Street~" +
                            "Plot 9/10 OB Plaza, Adumi Road~";

            String latitudes =
                    "0.313179~" +
                            "0.307415~" +
                            "0.325423~" +
                            "0.354047~" +
                            "0.324256~" +
                            "0.319986~" +
                            "0.316893~" +
                            "0.309579~" +
                            "0.292917~" +
                            "0.278391~" +
                            "0.359687~" +
                            "0.326203~" +
                            "0.318955~" +
                            "0.240770~" +
                            "0.060705~" +
                            "0.297587~" +
                            "0.346193~" +
                            "0.375344~" +
                            "0.332976~" +
                            "1.078013~" +
                            "1.071236~" +
                            "-0.605235~" +
                            "3.021373~";

            String longitudes =
                    "32.582606~" +
                            "32.610214~" +
                            "32.576713~" +
                            "32.611593~" +
                            "32.596344~" +
                            "32.591929~" +
                            "32.573704~" +
                            "32.570963~" +
                            "32.564702~" +
                            "32.565987~" +
                            "32.747937~" +
                            "32.615114~" +
                            "32.622932~" +
                            "32.563624~" +
                            "32.47165~" +
                            "32.600930~" +
                            "32.647439~" +
                            "32.643459~" +
                            "32.552356~" +
                            "34.149499~" +
                            "34.181763~" +
                            "30.662032~" +
                            "30.910571~";

            String[] splitNames = names.split("~");
            String[] splitDescriptions = descriptions.split("~");
            String[] splitLatitude = latitudes.split("~");
            String[] splitLongitude = longitudes.split("~");

            int size = splitNames.length;

            atm_Set.clear();

            for (int i = 0; i < size; i++) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Double.parseDouble(splitLatitude[i]), Double.parseDouble(splitLongitude[i])))
                        .title(splitNames[i])
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholderdark))
                        .snippet(splitDescriptions[i]));
                marker.setVisible(true);

                String atm, loc, lat, lon, array_atm;
                atm = splitNames[i];
                loc = splitDescriptions[i];
                lat = splitLatitude[i];
                lon = splitLongitude[i];
                array_atm = atm + "|" + loc + "|" + lat + "|" + lon;
                atm_Set.add(array_atm);
                brAdapter.notifyDataSetChanged();
                atm_list.setAdapter(brAdapter);
            }

            getLocation();

        } else {
            finish();
            am.ToastMessage(getApplicationContext(), getString(R.string.error));
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN)
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });
    }

    @Override
    protected void onResume() {
        locationPermission();
        mGoogleApiClient.connect();
        super.onResume();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        am.ToastMessage(this,connectionResult.getErrorMessage());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == M_LOC) {
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
    public void onBackPressed() {
        if(mBottomSheetBehavior.getState() != BottomSheetBehavior.STATE_HIDDEN) {
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onPause(){
        if(l!=null) l.dismiss();
        if(mFusedLocationClient != null) mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        if(mGoogleApiClient.isConnected()) mGoogleApiClient.disconnect();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMap.clear();
        mFusedLocationClient.flushLocations();
        super.onDestroy();
    }
}
