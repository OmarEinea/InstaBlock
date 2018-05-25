package com.eineao.instablock.managers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;
import java.util.Set;

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

    public void putStrings(String key, Set<String> strings) {
        mStorage.edit().putStringSet(key, strings).apply();
    }

    public boolean inStrings(String key, String query) {
        for(String string : mStorage.getStringSet(key, new HashSet<String>()))
            if (string.equals(query))
                return true;
        return false;
    }

    public boolean has(String key) {
        return mStorage.contains(key);
    }

    public void pop(String key) {
        mStorage.edit().remove(key).apply();
    }
}
