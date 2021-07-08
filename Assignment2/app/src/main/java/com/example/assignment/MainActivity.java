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

import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity{

    private LocationListener locationListener;
    private LocationManager locationManager;
    private final long MIN_TIME=1000;
    private final long MIN_DISTANCE=5;
    private LatLng latLng;
    double lat;
    double lng;
    String number="0716332197";
    String message="null";
    boolean stopClicked=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

    }

    public void onMapReady() {
        stopClicked=false;
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
                }catch (SecurityException | InterruptedException e){
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

    public void sendSms(String message) throws InterruptedException {
        while(!stopClicked){
            try{
                SmsManager smgr = SmsManager.getDefault();
                smgr.sendTextMessage(number,null,message,null,null);
                Toast.makeText(getApplicationContext(),"SMS Sent to "+number, Toast.LENGTH_LONG).show();
                Log.d("confirmation", "Message sent");

            }catch (Exception e){
                Log.d("exception", e.toString());
            }

            Thread.sleep(60000);
        }

    }

    public void onSendSmsClicked(View view){
            onMapReady();

    }

    public void onStopClicked(View view){
        stopClicked=true;
        Toast.makeText(getApplicationContext(),"STOP CLICKED", Toast.LENGTH_LONG).show();
    }

}
