package com.eineao.instablock.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class StorageManager {
    private SharedPreferences mStorage;

    public StorageManager(Context context) {
        mStorage = PreferenceManager.getDefaultSharedPreferences(context);
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
