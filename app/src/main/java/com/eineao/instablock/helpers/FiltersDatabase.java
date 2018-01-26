package com.eineao.instablock.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.eineao.instablock.models.FilterModel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * Created by Omar on 12/1/2017.
 */

public class FiltersDatabase extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "blocked_keywords",
        FILTER_ID = "filter_id", FILTER_NAME = "filter_name", KEYWORD = "keyword", ATTEMPTS = "attempts",
        DROP_TABLE_IF_EXISTED = "drop table if exists " + DATABASE_NAME,
        QUERY_ALL_FILTERS = "select distinct " + FILTER_NAME + " from " + DATABASE_NAME,
        QUERY_ALL_KEYWORDS = "select distinct " + KEYWORD + " from " + DATABASE_NAME,
        QUERY_FILTER = String.format(
                "select %s,%s from %s where %s=?",
                KEYWORD, ATTEMPTS, DATABASE_NAME, FILTER_NAME
        ),
        INCREMENT_APP_ATTEMPTS = String.format(
                "UPDATE %s SET %s=%s+1 WHERE %s='%%s'",
                DATABASE_NAME, ATTEMPTS, ATTEMPTS, KEYWORD
        ),
        DATABASE_CREATE_SCHEMA = String.format(
                "create table %s (%s integer primary key autoincrement,%s text,%s text,%s integer default 0);",
                DATABASE_NAME, FILTER_ID, FILTER_NAME, KEYWORD, ATTEMPTS
        );

    public FiltersDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCHEMA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_IF_EXISTED);
        onCreate(db);
    }

    public void addBlockedKeyword(String filterName, String keyword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FILTER_NAME, filterName);
        values.put(KEYWORD, keyword);

        db.insert(DATABASE_NAME, null, values);
        db.close();
    }

    public void addFilter(FilterModel filter) {
        SQLiteDatabase db = getWritableDatabase();

        Iterator<String> keywords = filter.getKeywords().iterator();
        String filterName = filter.getName();

        while(keywords.hasNext()) {
            ContentValues values = new ContentValues();
            values.put(FILTER_NAME, filterName);
            values.put(KEYWORD, keywords.next());
            db.insert(DATABASE_NAME, null, values);
        }
        db.close();
    }

    public void deleteFilter(FilterModel filter) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATABASE_NAME, FILTER_NAME + "=?", new String[]{filter.getName()});
        db.close();
    }

    public void deleteBlockedKeyword(String keyword) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATABASE_NAME, KEYWORD + "=?", new String[]{keyword});
        db.close();
    }

    public void loadAllFilters(List<FilterModel> filters) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_ALL_FILTERS, null);

        filters.clear();
        if(cursor.moveToFirst())
            do
                filters.add(new FilterModel(cursor.getString(0), db.rawQuery(
                    QUERY_FILTER, new String[]{cursor.getString(0)}
                )));
            while(cursor.moveToNext());

        cursor.close();
        db.close();
    }

    public ArrayList<String> getAllBlockedKeywords() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<String> blockedKeywords = new ArrayList<>();
        Cursor cursor = db.rawQuery(QUERY_ALL_KEYWORDS, null);

        if(cursor.moveToFirst())
            do blockedKeywords.add(cursor.getString(0));
            while(cursor.moveToNext());

        cursor.close();
        db.close();
        return blockedKeywords;
    }

    public void incrementFilterAttempts(String keyword) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format(INCREMENT_APP_ATTEMPTS, keyword));
        db.close();
    }
}
