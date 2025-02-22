package com.example.hund_hunter.log_in_activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.hund_hunter.main_activities.OrderCreationActivity;
import com.example.hund_hunter.R;
import com.example.hund_hunter.main_activities.SeekerActivity;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class ChoiceActivity extends AppCompatActivity {

    public final int LOCATION_PERMISSION = 1001;
    public static LatLng myPlace;

    LocationManager locationManager;
    Location location;

    private boolean granted = false;

    LocationListener listener = new LocationListener() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onLocationChanged(Location location) {
            if (location == null) {
                return;
            }else {
                showLocation(location);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if(provider.equals(LocationManager.NETWORK_PROVIDER))
                Toast.makeText(getApplicationContext(),
                        "Status: " + status,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            //что делать, если провайдер включен
            if(granted || checkPermission())
                showLocation(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
        }

        @Override
        public void onProviderDisabled(String provider) {
            //что делать, если провайдер выключен
            Toast.makeText(ChoiceActivity.this, "Проверьте подключение к сети!", Toast.LENGTH_SHORT).show();
        }
    };

    private void showLocation(Location location){
        if(location == null){
            return;
        }
        if(location.getProvider().equals(LocationManager.GPS_PROVIDER)){
            this.location = location;
            // getLatitude - широта
            String koord = String.format("%.4f", location.getLatitude());
            // getLongitude - долгота
            koord = String.format("%.4f", location.getLongitude());
            koord = new Date(location.getTime()).toString();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choice_activity);

        //подключить менеджер местоположения
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (granted || checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000 * 10, 20, listener);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null){
                    // Последние координаты
                    Toast.makeText(this, String.format("%.4f", location.getLatitude()) + " " +
                            String.format("%.4f", location.getLongitude()), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean checkPermission(){
        //относится к опасным разрешениям, требует запроса.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //что делать, если разрешение не дано: попробовать запросить повторно
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION) {
            granted = true;
            if (grantResults.length > 0) {
                for (int res : grantResults) {
                    if (res != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Access denied", Toast.LENGTH_SHORT).show();
                        granted = false;
                    }
                }
            } else {
                Toast.makeText(this, "Access denied", Toast.LENGTH_SHORT).show();
                granted = false;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(listener);
    }

    public void findButtonOnClick(View view){
        Intent intent = new Intent(ChoiceActivity.this, SeekerActivity.class);
        if(location != null) {
            //intent.putExtra("latitude", location.getLatitude());
            //intent.putExtra("longitude", location.getLongitude());
            myPlace = new LatLng(location.getLatitude(), location.getLongitude());

        }else{
            //intent.putExtra("latitude", 52.27537);
            //intent.putExtra("longitude", 104.2774);
            myPlace = new LatLng(52.27537, 104.2774);
        }
        startActivity(intent);
    }

    public void lostButtonClick(View view){
        Intent intent = new Intent(ChoiceActivity.this, OrderCreationActivity.class);
        if(location != null) {
            //intent.putExtra("latitude", location.getLatitude());
            //intent.putExtra("longitude", location.getLongitude());
            myPlace = new LatLng(location.getLatitude(), location.getLongitude());


        }else{
            //intent.putExtra("latitude", 52.27537);
            //intent.putExtra("longitude", 104.2774);
            myPlace = new LatLng(52.27537, 104.2774);
        }
        startActivity(intent);
    }
}