package network;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import DataBase.DataBaseHandler;
import constant.Constant;
import models.Common;
import models.UserDetails;
import view.activity.LoginActivity;

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
//            Log.e("latitude",location.getLatitude()+"");
//            Log.e("longitude",location.getLongitude()+"");
//            latitude = location.getLatitude();
//            longitude = location.getLongitude();
            setLatLong(location);
        }

    }

    private void setLatLong(Location location) {
        DataBaseHandler dataBaseHandler = new DataBaseHandler(LocationService.this);
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constant.latitude,location.getLatitude());
        contentValues.put(Constant.longitude,location.getLongitude());
        contentValues.put(Constant.latlong_date, Common.convertDateString(new Date(),"yyyy-MM-dd HH:mm:ss"));
        contentValues.put(Constant.latlong_emp_gid, UserDetails.getUser_id());

        String Out_Message = dataBaseHandler.Insert("fet_trn_tlatlong",contentValues);
//        String Out_Message = "ss";

        if (!"SUCCESS".equals(Out_Message) ){
            Toast.makeText(getApplicationContext(),"Error.:"+"Error On Lat Long Save. ",Toast.LENGTH_LONG).show();
        }
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
