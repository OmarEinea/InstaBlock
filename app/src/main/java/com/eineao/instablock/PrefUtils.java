package com.eineao.instablock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by abdulahiosoble on 12/2/17.
 */

public class PrefUtils {

    private SharedPreferences mSharedPreferences;
    private Activity mActivity;

    public PrefUtils(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setPin(String pin) {
        mSharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString("Pin", pin);
        Log.i("SHAREDPREF", pin);
        editor.commit();
    }

    public String getPin(){
        mSharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        String pin = mSharedPreferences.getString("Pin", "");
        Log.i("SHAREDPREF", pin);
        return pin;
    }

    public void clearPin(){
        mSharedPreferences = mActivity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.clear();
    }
}
