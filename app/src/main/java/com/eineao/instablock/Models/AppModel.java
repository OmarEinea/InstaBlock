package com.eineao.instablock.Models;

import android.graphics.Bitmap;

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

    public AppModel(String title, Bitmap icon, String packageName) {
        mTitle = title;
        mIcon = icon;
        mPackageName = packageName;
    }

    public AppModel(Element image, Element link) {
        String iconURL = image.attr("src"), appURL = link.attr("href");
        String iconSubURL = iconURL.substring(iconURL.lastIndexOf("/") + 1, iconURL.length() - 8);
        mIconURL = String.format(Locale.US, ICON_URL, iconSubURL, 96);
        mTitle = image.attr("alt");
        mPackageName = appURL.substring(appURL.indexOf("=") + 1);
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

    @Override
    public String toString() {
        return "AppModel {" +
            "appTitle='" + mTitle + "', " +
            "packageName='" + mPackageName + "'" +
        "}";
    }
}
