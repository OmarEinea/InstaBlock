package com.eineao.instablock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.widget.Toast;

import com.eineao.instablock.helpers.BlockedAppsDatabase;
import com.eineao.instablock.helpers.FiltersDatabase;
import com.eineao.instablock.models.AppModel;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import org.jsoup.Jsoup;

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
            // If not
            if(app == null)
                try {
                    // Get app name from play store
                    String appName = new PlayStoreFetcher().execute(packageName).get();
                    // If app can't be found on play store
                    if(appName == null) {
                        // Get its name from the installed package
                        PackageManager pm = context.getPackageManager();
                        appName = pm.getApplicationInfo(packageName, 0).loadLabel(pm).toString();
                    }
                    String appNameLowerCase = appName.toLowerCase();
                    // Check if installed app name contains any of the filtered keywords
                    for(String keyword : filtersDB.getAllBlockedKeywords())
                        if(appNameLowerCase.contains(keyword.toLowerCase())) {
                            app = new AppModel(appName, null, packageName);
                            blockedKeyword = keyword;
                            break;
                        }
                } catch (Exception ignored) {}
            // If it is blocked
            if(app != null) {
                // Uninstall the blocked app
                uninstallPackage(packageName);
                // Show a toast to indicate that the blocked app was uninstalled
                Toast.makeText(context, "InstaBlock uninstalled " + app.getTitle(), Toast.LENGTH_LONG).show();
                Toast.makeText(context, "Recorded a blocked installation" + app.getTitle(), Toast.LENGTH_SHORT).show();
                if(blockedKeyword == null)
                    appsDB.incrementAppAttempts(packageName);
                else
                    filtersDB.incrementFilterAttempts(blockedKeyword);
            }
        }
    }

    private static class PlayStoreFetcher extends AsyncTask<String, Integer, String> {
        private final String URL = "https://play.google.com/store/apps/details?hl=en&id=";

        @Override
        protected String doInBackground(String... strings) {
            try {
                return Jsoup.connect(URL + strings[0]).get().select(".id-app-title").first().text();
            } catch (Exception e) {
                return null;
            }
        }
    }

    // A function that silently uninstalls a package using root access
    public static void uninstallPackage(String packageName) {
        // Make sure root access is granted
        if(RootTools.isAccessGiven())
            try { // Try uninstalling the package using PackageManager's "uninstall" command
                RootTools.getShell(true).add(new Command(0, "pm uninstall " + packageName));
            } catch (Exception ignored) {}
    }
}
