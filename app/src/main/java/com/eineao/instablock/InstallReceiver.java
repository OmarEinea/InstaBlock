package com.eineao.instablock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import com.eineao.instablock.DBHelpers.BlockedAppsDatabase;
import com.eineao.instablock.DBHelpers.FiltersDatabase;
import com.eineao.instablock.Models.AppModel;
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
            String blockedKeyword = null, packageName = intent.getData().getSchemeSpecificPart();
            BlockedAppsDatabase appsDB = new BlockedAppsDatabase(context);
            FiltersDatabase filtersDB = new FiltersDatabase(context);
            // Check if installed app is among the blocked apps
            AppModel app = appsDB.getBlockedApp(packageName);
            if(app == null) {
                try {
                    PackageManager pm = context.getPackageManager();
                    String appName = pm.getApplicationInfo(packageName, 0).loadLabel(pm).toString().toLowerCase();
                    for(String keyword : filtersDB.getAllBlockedKeywords())
                        if(appName.contains(keyword.toLowerCase())) {
                            app = new AppModel(appName, null, packageName);
                            blockedKeyword = keyword;
                            break;
                        }
                } catch (Exception ignored) {}
            }
            // If it is blocked
            if(app != null) {
                // Uninstall the blocked app
                uninstallPackage(packageName);
                // Show a toast to indicate that the blocked app was uninstalled
                Toast.makeText(context, "InstaBlock uninstalled " + app.getTitle(), Toast.LENGTH_LONG).show();
                if(blockedKeyword == null)
                    appsDB.incrementAppAttempts(packageName);
                else
                    filtersDB.incrementFilterAttempts(blockedKeyword);
                Log.i("InstaBlock", app.getTitle() + "was uninstalled");
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
