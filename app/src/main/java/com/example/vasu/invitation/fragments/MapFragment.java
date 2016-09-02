package com.example.vasu.invitation.fragments;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.vasu.invitation.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
* this fragment is using for chat tab option
* */
public class MapFragment extends Fragment implements OnMapReadyCallback, RoutingListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final int REQUEST_CODE = 101;
    private static final float ZOOM_LEVEL = 14;
    MapView mMapView;
    private GoogleMap googleMap;
    private List<Polyline> polylines;
    private boolean isCurrentLocationRequired = true;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private Location mLocation;

    private LatLng startLatLng, endLatlng;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // inflat and return the layout
        View v = inflater.inflate(R.layout.fragment_chat, container,
                false);
        mMapView = (MapView) v.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        // latitude and longitude


        // Perform any camera updates here
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        requestLocation();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(Bundle bundle) {

        try {
            mLocation = LocationServices.FusedLocationApi.getLastLocation(
                    mGoogleApiClient);
            if (mLocation != null) {
                startLatLng = new LatLng(mLocation.getLatitude(), mLocation.getLongitude());

                MarkerOptions marker = new MarkerOptions().position(startLatLng).title("You are here");
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_home));
                googleMap.addMarker(marker);

                Routing routing = new Routing.Builder()
                        .travelMode(Routing.TravelMode.WALKING)
                        .withListener(this)
                        .waypoints(startLatLng, endLatlng)
                        .build();
                routing.execute();
            }
        } catch (SecurityException e) {
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
    }


    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    @TargetApi(23)
    private boolean addPermission(List<String> permissionsList, String permission) {
        if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);
            if (!this.shouldShowRequestPermissionRationale(permission)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                Map<String, Integer> perms = new HashMap<>();
                // Initial
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    initLocationManager();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "GPS Permission is Denied", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void initLocationManager() {
        if (!isGPSEnabled(getActivity())) {
            alertDialog(getActivity(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    buildGoogleApiClient();
                    createLocationRequest();
                }
            }, null, "Please enable GPS to get your location.");
        } else {
            buildGoogleApiClient();
            createLocationRequest();
           /* locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, HomeActivity.this);*/
        }

    }

    public static boolean isGPSEnabled(Context mContext) {
        final LocationManager manager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void alertDialog(Context mContext, final DialogInterface.OnClickListener mPositiveListener, final DialogInterface.OnClickListener mNegativeListener, final String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.Material_Dialog);
        builder.setTitle("");
        builder.setMessage(message);
        builder.setPositiveButton("OK", mPositiveListener);
        if (mNegativeListener == null) {
            builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setNegativeButton(mContext.getResources().getString(R.string.cancel), mNegativeListener);
        }
        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    private void requestLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission_group.LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initLocationManager();
        } else {
            ArrayList<String> permissionsList = new ArrayList<>();
            addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION);
            addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION);
            if (permissionsList.size() > 0) {
                requestPermissions((String[]) permissionsList.toArray(new String[permissionsList.size()]), REQUEST_CODE);
            } else {
                initLocationManager();
            }
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {


        endLatlng = new LatLng(23.012172, 72.450682);

        this.googleMap = googleMap;

        googleMap.addMarker(new MarkerOptions()
                .position(endLatlng)
                .title("Venue: Parker Jalaram Farm"));




        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(endLatlng).zoom(ZOOM_LEVEL).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));


    }

    @Override
    public void onRoutingFailure(RouteException e) {
        // The Routing request failed
        if (e != null) {
            Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Something went wrong, Try again", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(endLatlng).zoom(ZOOM_LEVEL).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        if (polylines != null && polylines.size() > 0) {
            polylines.clear();
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = googleMap.addPolyline(polyOptions);
            polylines.add(polyline);

            Toast.makeText(getActivity(), "Route " + (i + 1) + ": distance - " + route.get(i).getDistanceValue() + ": duration - " + route.get(i).getDurationValue(), Toast.LENGTH_SHORT).show();
        }

        // Start marker
        MarkerOptions options = new MarkerOptions();
        options.position(startLatLng);
        googleMap.addMarker(options);

        // End marker
        options = new MarkerOptions();
        options.position(endLatlng);
        googleMap.addMarker(options);

    }

    @Override
    public void onRoutingCancelled() {
    }

}

