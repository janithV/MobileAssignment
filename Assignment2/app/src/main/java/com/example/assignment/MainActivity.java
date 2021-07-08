package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity{

    private GoogleMap gMap;
    private CameraPosition cameraPosition;

    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME=1000; //minimum time interval between location updates, in milliseconds
    private final long MIN_DISTANCE=5; //minimum distance between location updates, in meters
    private LatLng latLng;
    double lat;
    double lng;
    String number="0715491816";
    String message="null";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);


    }

    public void onMapReady(View view) {

        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                try {
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    lat=latLng.latitude;
                    lng=latLng.longitude;
                    message="http://maps.google.com/maps?saddr="+lat+","+lng;
                    Log.d("message",message);
                    sendSms(message);
//                    gMap.addMarker(new MarkerOptions().position(latLng).title("Latitude :"+latLng.latitude+" & Longitude :"+latLng.longitude));
//                    gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }catch (SecurityException e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
            @Override
            public void onProviderEnabled(String provider) { }
            @Override
            public void onProviderDisabled(String provider) { }
        };
        try {
            locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME,MIN_DISTANCE,locationListener);
        }catch (SecurityException e){e.printStackTrace();
        }
    }

    public void sendSms(String message) {
        try{
                Log.d("inside try", "athule pako");
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(number,null,message,null,null);
                Toast.makeText(getApplicationContext(),"SMS Sent to "+number, Toast.LENGTH_LONG).show();

        }catch (Exception e){
            Log.d("exception", e.toString());
        }
    }



    }
