package com.improveresponse.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

/**
 * class starting the service only when there is network
 */
public class NetworkReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNetworkDown = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);

        if(isNetworkDown){
            context.stopService(new Intent(context, UpdaterService.class));
        }else{
            context.startService(new Intent(context, UpdaterService.class));
        }
    }
}
