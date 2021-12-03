package edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import edu.floridapoly.mobiledeviceapps.fall21.team5.crimedirectory.databinding.ActivityMapsBinding;


/**
 *  MapsActivity.java
 *  Purpose: Activity to display map
 *  Interaction: Users must click on map within a set lat and lon distance to the location where
 *  the target has an arrest warrant
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {
    public static String EXTRA_INDEX = "extra_index";
    public static String EXTRA_USER = "extra_user";
    public static String EXTRA_DIFFICULTY = "extra_difficulty";

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient client;
    LocationManager locationManager;
    LocationListener locationListener;
    Polyline currentPolyLine;
    MarkerOptions currentLocationMarker;

    private double crimeLat = 0.0;
    private double crimeLon = 0.0;
    private double range;
    private double currentLat = 0.0;
    private double currentLon = 0.0;

    private int index;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        client = LocationServices.getFusedLocationProviderClient(this);

        index = getIntent().getIntExtra(EXTRA_INDEX, 5);
        user = getIntent().getParcelableExtra(EXTRA_USER);

        setDifficulty(getIntent().getIntExtra(EXTRA_DIFFICULTY, 0));
        setCrimeLocation();
    }

    // check if user has granted permission to use their current location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);

                Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getCurrentLocation();
        locationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                        }
        };

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        client = LocationServices.getFusedLocationProviderClient(this);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location
                        if (location != null) {

                            double la = location.getLatitude();
                            double lo = location.getLongitude();

                            LatLng current = new LatLng(la, lo);
                            currentLocationMarker = new MarkerOptions().position(current).title("Your Location");
                            mMap.addMarker(currentLocationMarker).showInfoWindow();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
                        }
                    }
                });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                closeEnough(point);
            }
        });
    }

    /**
     * check whether the selected point is within the correct range
     * @param  point  where the user clicked on the map
     */
    public void closeEnough(LatLng point) {
        MarkerOptions marker;

        if (Math.abs(crimeLat - point.latitude) < range && Math.abs(crimeLon - point.longitude) < range) {
            Intent intentData = new Intent();
            marker = new MarkerOptions().position(point).title("Correct");

            user.setCompletedMap(index, true);

            Toast.makeText(this, "Location within range", Toast.LENGTH_SHORT).show();

            String url = getUrl(marker.getPosition(), currentLocationMarker.getPosition(), "driving");
            new FetchURL(MapsActivity.this).execute(url, "driving");

            intentData.putExtra(MapActivity.EXTRA_MAP_CHECK, true);
            intentData.putExtra(MapActivity.EXTRA_TOAST_STR, "correct value");
            setResult(RESULT_OK, intentData);
        } else {
            marker= new MarkerOptions().position(point).title("Incorrect");
            Toast.makeText(this, "Location out of range", Toast.LENGTH_SHORT).show();
        }

        mMap.addMarker(marker).showInfoWindow();
    }

    // set the correct location for the arrest warrant depending on index (investigation number)
    public void setCrimeLocation() {
        double polyaninLat = 32.7668;
        double polyaninLon = -96.7836;
        double smikaLat = 40.0833;
        double smikaLon = -105.3505;
        double ariasLat = 33.4484;
        double ariasLon = -112.074;
        double loriLat = 39.404;
        double loriLon = -84.4068;

        switch(index) {
            case 0:
                crimeLat = polyaninLat;
                crimeLon = polyaninLon;
                break;
            case 1:
                crimeLat = smikaLat;
                crimeLon = smikaLon;
                break;
            case 2:
                crimeLat = ariasLat;
                crimeLon = ariasLon;
                break;
            case 3:
                crimeLat = loriLat;
                crimeLon = loriLon;
                break;
        }
    }

    public void getCurrentLocation() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
            }
        }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
    }

    // for route drawing - from https://www.youtube.com/watch?v=wRDLjUK8nyU
    private String getUrl(LatLng dest, LatLng origin, String transportationMode) {
        String originStr = "origin=" + origin.latitude + "," + origin.longitude;
        String destStr = "destination=" + dest.latitude + "," + dest.longitude;
        String mode = "mode=" + transportationMode;
        String params = originStr + "&" + destStr + "&" + mode;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    // for route drawing - from https://www.youtube.com/watch?v=wRDLjUK8nyU
    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyLine != null)
            currentPolyLine.remove();
        currentPolyLine = mMap.addPolyline((PolylineOptions) values[0]);
    }

    /**
     * Set the acceptable range for a correct value based on difficulty setting
     *
     * @param  difficulty  refers to the easy, medium, or hard setting radio button clicked in MapActivity
     *                     default difficulty = easy
     */
    private void setDifficulty(int difficulty) {
        switch(difficulty) {
            case 0:
                range = 1;

                break;
            case 1:
                range = 0.5;
                break;
            case 2:
                range = 0.2;
                break;
        }
    }
}