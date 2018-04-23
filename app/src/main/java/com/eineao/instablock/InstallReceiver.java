package com.eineao.instablock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.Toast;

import com.eineao.instablock.activities.BlockedDialog;
import com.eineao.instablock.helpers.BlockedAppsDatabase;
import com.eineao.instablock.helpers.FiltersDatabase;
import com.eineao.instablock.models.AppModel;
import com.stericson.RootShell.execution.Command;
import com.stericson.RootTools.RootTools;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

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
            AppModel blockedApp = appsDB.getBlockedApp(packageName);
            // If it isn't blocked
            if(blockedApp == null) {
                /* Check if app name contains any of the blocked keywords */
                ArrayList<String> keywords = filtersDB.getAllBlockedKeywords();
                String appName = null;
                PackageManager pm = context.getPackageManager();
                try { // Get app name from the installed package
                    appName = pm.getApplicationInfo(packageName, 0).loadLabel(pm).toString();
                    // Check if app name contains any blocked keyword
                    blockedKeyword = checkKeywords(keywords, appName);
                } catch (NameNotFoundException ignore) {}
//                // If installed app name is clean
//                if(blockedKeyword == null)
//                    // Check the app's Play Store name
//                    try { // Get app name from Play Store
//                        appName = new PlayStoreFetcher().execute(packageName).get();
//                        // Check if app name contains any blocked keyword
//                        blockedKeyword = checkKeywords(keywords, appName);
//                    } catch (InterruptedException | ExecutionException ignore) {}
                // If app name contains a blocked keyword
                if(blockedKeyword != null)
                    try { // Consider it as a blocked app
                        blockedApp = new AppModel(
                            appName,
                            ((BitmapDrawable) pm.getApplicationIcon(packageName)).getBitmap(),
                            packageName
                        );
                    } catch (NameNotFoundException ignored) {}
            }
            // If it's blocked
            if(blockedApp != null) {
                // Uninstall the blocked app
                if(uninstallPackage(packageName))
                    // Show a toast to indicate that the blocked app was uninstalled
                    Toast.makeText(context, "InstaBlock uninstalled " + blockedApp.getTitle(), Toast.LENGTH_LONG).show();
                // Send app name and icon to blocked dialog through intent
                ByteArrayOutputStream icon = new ByteArrayOutputStream();
                blockedApp.getIcon().compress(Bitmap.CompressFormat.PNG, 100, icon);
                Intent dialogIntent = new Intent(context, BlockedDialog.class);
                dialogIntent.putExtra("appIcon", icon.toByteArray());
                dialogIntent.putExtra("appName", blockedApp.getTitle());
                context.startActivity(dialogIntent);
                if(blockedKeyword == null)
                    appsDB.incrementAppAttempts(packageName);
                else
                    filtersDB.incrementFilterAttempts(blockedKeyword);
            }
        }
    }

    private static String checkKeywords(ArrayList<String> keywords, String appName) {
        // Normalize app name by making it lower case
        appName = appName.toLowerCase();
        // Check if app name contains any of the keywords
        for (String keyword : keywords)
            if (appName.contains(keyword.toLowerCase()))
                return keyword;
        return null;
    }

//    private static class PlayStoreFetcher extends AsyncTask<String, Integer, String> {
//        private final String URL = "https://play.google.com/store/apps/details?hl=en&id=";
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try {
//                return Jsoup.connect(URL + strings[0]).get().select(".id-app-title").first().text();
//            } catch (Exception e) {
//                return null;
//            }
//        }
//    }

    // A function that silently uninstalls a package using root access
    public static boolean uninstallPackage(String packageName) {
        // Make sure root access is granted
        if(RootTools.isAccessGiven())
            try { // Try uninstalling the package using PackageManager's "uninstall" command
                RootTools.getShell(true).add(new Command(0, "pm uninstall " + packageName));
                return true;
            } catch (Exception ignored) {}
        return false;
    }
}
