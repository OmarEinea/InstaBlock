package com.eineao.instablock.Models;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jsoup.nodes.Element;

import java.util.Locale;

/**
 * Created by Omar on 10/13/2017.
 *
 * This class contains apps' basic details like
 * its title, icon and package name
 */

public class AppModel {
    private final String ICON_URL = "https://lh3.googleusercontent.com/%s=w%d";
    private String mTitle, mPackageName, mIconURL = null;
    private Bitmap mIcon = null;
    private int mAttempts = 0;

    public AppModel(String title, Bitmap icon, String packageName) {
        mTitle = title;
        mIcon = icon;
        mPackageName = packageName;
    }

    public AppModel(Cursor cursor) {
        mTitle = cursor.getString(1);
        mIcon = getIcon(cursor.getBlob(2));
        mPackageName = cursor.getString(0);
        mAttempts = cursor.getInt(3);
    }

    public AppModel(Element image, Element link) {
        String iconURL = image.attr("src"), appURL = link.attr("href");
        String iconSubURL = iconURL.substring(iconURL.lastIndexOf("/") + 1, iconURL.length() - 8);
        mIconURL = String.format(Locale.US, ICON_URL, iconSubURL, 96);
        mTitle = image.attr("alt");
        mPackageName = appURL.substring(appURL.indexOf("=") + 1);
    }

    private Bitmap getIcon(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getIconURL() {
        return mIconURL;
    }

    public void setIconURL(String iconURL) {
        mIconURL = iconURL;
    }

    public Bitmap getIcon() {
        return mIcon;
    }

    public void setIcon(Bitmap icon) {
        mIcon = icon;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

    public String getAttemptsString() {
        return mAttempts + " attempt" + (mAttempts > 1 ? "s" : "");
    }

    public int getAttempts() {
        return mAttempts;
    }

    @Override
    public String toString() {
        return "AppModel {" +
            "appTitle='" + mTitle + "', " +
            "packageName='" + mPackageName + "'" +
        "}";
    }
}
