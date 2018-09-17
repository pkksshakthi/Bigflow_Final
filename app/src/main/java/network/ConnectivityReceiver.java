package network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import presenter.VolleyCallback;

import static android.content.Context.LOCATION_SERVICE;

public class ConnectivityReceiver
        extends BroadcastReceiver {

    private ConnectivityReceiverListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String s = intent.getAction();
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e("test", "started");
        } else if (intent.getAction().equals("restarting.services")) {
            Log.e("test", "started");
            context.getApplicationContext().startService(new Intent(context, LocationService.class));
        } else if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
            if (listener != null) {
                if (isConnected) {
                    Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Disconnected", Toast.LENGTH_LONG).show();
                }
                listener.onNetworkConnectionChanged(isConnected);
            }
        }  

    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public void setConnectivityReceiver(ConnectivityReceiverListener listener1) {
        listener = listener1;
    }

}
