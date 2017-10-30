package com.eineao.instablock;

import org.jsoup.nodes.Element;

import java.util.Locale;

/**
 * Created by Omar on 10/13/2017.
 *
 * This class contains apps' basic details like
 * its title, icon url and package name
 */

public class AppDetails {
    private final String ICON_URL = "https://lh3.googleusercontent.com/%s=w%d";
    private String appTitle, iconSubURL, packageName;

    public AppDetails(String appTitle, String iconSubURL, String packageName) {
        this.appTitle = appTitle;
        this.iconSubURL = iconSubURL;
        this.packageName = packageName;
    }

    public AppDetails(Element image, Element link) {
        String icon = image.attr("src"), url = link.attr("href");
        appTitle = image.attr("alt");
        iconSubURL = icon.substring(icon.lastIndexOf("/") + 1, icon.length() - 8);
        packageName = url.substring(url.indexOf("=") + 1);
    }

    public String getAppTitle() {
        return appTitle;
    }

    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }

    public String getIconSubURL() {
        return iconSubURL;
    }

    public String getIconURL(int size) {
        return String.format(Locale.US, ICON_URL, iconSubURL, size);
    }

    public void setIconSubURL(String iconSubURL) {
        this.iconSubURL = iconSubURL;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppDetails {" +
            "appTitle='" + appTitle + "', " +
            "iconSubURL='" + iconSubURL + "', " +
            "packageName='" + packageName + "'" +
        "}";
    }
}
