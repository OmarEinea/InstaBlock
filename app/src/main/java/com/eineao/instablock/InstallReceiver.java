package com.eineao.instablock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Omar on 9/27/2017.
 *
 * This class will contain the main logic for blocking apps installation
 */

public class InstallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // If the broadcast received is due to an app installation
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            // Show a toast to indicate that for now
            Toast.makeText(context, "An app has been installed", Toast.LENGTH_LONG).show();
        }
    }
}
