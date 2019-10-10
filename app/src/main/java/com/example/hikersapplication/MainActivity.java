package com.example.hikersapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    public void startlistining(){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==
                          PackageManager.PERMISSION_GRANTED){
                       startlistining();
                        }
            }
        }

    public void updateloca(Location location){

        TextView lattextview = findViewById(R.id.Latitude);
        TextView longTextview = findViewById(R.id.longitude);
        TextView AccuracyTextView = findViewById(R.id.Accuracy);
        TextView AttitudeTextView = findViewById(R.id.Attitude);
        TextView AddressTextView = findViewById(R.id.Address);

        lattextview.setText("Latitude:"+location.getLatitude());
        longTextview.setText("Longitude:"+location.getLongitude());
        AccuracyTextView.setText("Accuracy:"+location.getAccuracy());
        AttitudeTextView.setText("Latitude:"+location.getAltitude());
       //
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            Log.i("address",addresses.toString());
            String postalcode = addresses.get(0).getPostalCode();
            String Road = addresses.get(0).getThoroughfare();
            String Localty = addresses.get(0).getLocality();
            String city  = addresses.get(0).getSubAdminArea();

           String info = Road +"\n"+Localty+"\n"+city+"\n"+postalcode  ;
            Log.i("Info =",info);
            AddressTextView.setText("Address -"+info);
                } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    locationManager =(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
    locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
          updateloca(location);

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
    }else{
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location !=null){
            updateloca(location);
        }
    }
    }
}
