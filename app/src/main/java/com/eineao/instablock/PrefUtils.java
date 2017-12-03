package com.eineao.instablock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by abdulahiosoble on 12/2/17.
 */

public class PrefUtils {

    private SharedPreferences mSharedPreferences;
    private Activity mActivity;
    private Context mContext;

    public PrefUtils(Context mContext) {
        this.mContext = mContext;
    }

    public void setPin(String pin) {
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("Pin", pin);
        Log.i("SHAREDPREF", pin);
        editor.apply();
    }

    public String getPin(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String pin = mSharedPreferences.getString("Pin", "0000");
        Log.i("SHAREDPREF", pin);
        return pin;
    }

    public void clearPin(){
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove("Pin");
        editor.apply();
    }
}
