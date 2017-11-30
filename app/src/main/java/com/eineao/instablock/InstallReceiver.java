package com.eineao.instablock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

/**
 * Created by Omar on 9/27/2017.
 *
 * This class contains the main logic for blocking apps installation
 */

public class InstallReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // If the broadcast received is due to an app installation
        if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
            AppDetails app = MainActivity.mDB.getBlockedApp(intent.getDataString().substring(8));
            if(app != null) {
                // Uninstall any app after installation (for now)
                uninstallPackage(app.getPackageName());
                // Show a toast to indicate that app was uninstalled
                Toast.makeText(context, "InstaBlock uninstalled " + app.getTitle(), Toast.LENGTH_LONG).show();
            }
        }
    }

    // A function that silently uninstalls a package using root access
    public static void uninstallPackage(String packageName) {
        // Make sure root access is granted
        if(RootTools.isAccessGiven())
            try { // Try uninstalling the package using PackageManager's "uninstall" command
                RootTools.getShell(true).add(new Command(0, "pm uninstall " + packageName));
            } catch (Exception e) { // If command execution fails, record an error of failure
                e.printStackTrace();
                Log.e("uninstallPackage()", "Failed to execute uninstall package command");
            }
    }
}
