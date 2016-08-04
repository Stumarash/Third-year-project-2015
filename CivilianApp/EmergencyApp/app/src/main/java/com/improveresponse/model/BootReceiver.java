package com.improveresponse.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * class handling the starting the service when the device starts up
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context, UpdaterService.class));
    }
}
