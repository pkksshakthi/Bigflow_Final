package network;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import presenter.VolleyCallback;

public class ConnectivityReceiver
        extends BroadcastReceiver {

    public static ConnectivityReceiverListener listener;

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (listener != null) {
            listener.onNetworkConnectionChanged(isConnected);
        }

    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }

    public static void setConnectivityReceiver(ConnectivityReceiverListener listener1) {
        listener = listener1;
    }

}
