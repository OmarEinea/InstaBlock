package com.eineao.instablock;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StorageManager {
    private SharedPreferences mStorage;

    StorageManager(Activity activity) {
        mStorage = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void set(String key, String value) {
        mStorage.edit().putString(key, value).apply();
    }

    public String get(String key) {
        return mStorage.getString(key, "");
    }

    public void pop(String key) {
        mStorage.edit().remove(key).apply();
    }
}
