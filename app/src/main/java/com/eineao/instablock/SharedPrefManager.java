package com.eineao.instablock;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by abdulahiosoble on 11/8/17.
 *
 * This class allows users to store blocked apps.
 */

public class SharedPrefManager {

    Context mContext;

    public SharedPrefManager(Context mContext) {
        this.mContext = mContext;
    }

    public void saveBlockedApp(AppDetails apps) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("BlockedApps", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(apps);
        editor.putString("App", json);
        editor.commit();

        Log.i(apps.getAppTitle(), "Has been Blocked!! ---- -- ---");
    }

    public Map<String, String> getBlockedApp() {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("BlockedApps", Context.MODE_PRIVATE);
        return (Map<String, String>) sharedPreferences.getAll();
    }

}
