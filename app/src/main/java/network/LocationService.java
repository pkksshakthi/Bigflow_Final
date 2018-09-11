package network;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service implements LocationListener {
    private Timer mTimer;
    private long notify_interval = 5000;
    private Handler mHandler;
    private LocationManager locationManager;
    private Location location;

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        Log.e("test","onCreate");
        super.onCreate();
        mHandler = new Handler();
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            });

        }
    }

    @SuppressLint("MissingPermission")
    private void fn_getlocation() {
        Boolean isGPSEnable, isNetworkEnable;
        double latitude, longitude;
        Log.v("Services", "fn_getlocation");
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnable && !isNetworkEnable) {

        } else {
            location = null;
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
            if (locationManager != null) {
                if (isNetworkEnable) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                } else {
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }
            }
            Log.e("latitude",location.getLatitude()+"");
            Log.e("longitude",location.getLongitude()+"");
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            setLatLong(location);
        }

    }

    private void setLatLong(Location location) {
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("test","onTaskRemoved");
        Intent broadcastIntent = new Intent("restarting.services");
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("test","onDestroy");
        Intent broadcastIntent = new Intent("restarting.services");
        sendBroadcast(broadcastIntent);
    }
}
