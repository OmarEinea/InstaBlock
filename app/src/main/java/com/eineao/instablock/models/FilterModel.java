package com.eineao.instablock.models;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * Created by Omar on 12/1/2017.
 */

public class FilterModel {
    private String mName;
    private ArrayList<String> mKeywords;
    private int mAttempts = 0;

    public FilterModel(String filterName, String keywords) {
        if(filterName.length() > 0 && keywords.length() > 0) {
            mName = filterName;
            mKeywords = new ArrayList<>(Arrays.asList(keywords.split(" *, *")));
        }
    }

    public FilterModel(String filterName, Cursor keywords) {
        mName = filterName;
        mKeywords = new ArrayList<>();
        if(keywords.moveToFirst())
            do {
                mKeywords.add(keywords.getString(0));
                mAttempts += keywords.getInt(1);
            } while(keywords.moveToNext());
        keywords.close();
    }

    public String getName() {
        return mName;
    }

    public void setName(String filterName) {
        mName = filterName;
    }

    public ArrayList<String> getKeywords() {
        return mKeywords;
    }

    public String getKeywordsString() {
        String keywordsString = mKeywords.toString();
        return keywordsString.substring(1, keywordsString.length() - 1);
    }

    public void setKeywords(ArrayList<String> keywords) {
        mKeywords = keywords;
    }

    public int getAttempts() {
        return mAttempts;
    }

    public void setAttempts(int attempts) {
        mAttempts = attempts;
    }

    public String getAttemptsString() {
        return mAttempts + " attempt" + (mAttempts > 1 ? "s" : "");
    }

    @Override
    public String toString() {
        return "FilterModel {" +
                "mName='" + mName + "'" +
                "mKeywords=" + mKeywords +
                "}";
    }
}
