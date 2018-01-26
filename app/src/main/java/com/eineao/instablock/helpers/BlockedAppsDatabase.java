package com.eineao.instablock.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import com.eineao.instablock.models.AppModel;

import java.io.ByteArrayOutputStream;
import java.util.List;

/**
 * SQLite Helper to add and get blocked apps to database
 *
 * Created by Omar on 11/29/2017.
 */

public class BlockedAppsDatabase extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "blocked_apps",
        PACKAGE_NAME = "package_name", TITLE = "title", ICON = "icon", ATTEMPTS = "attempts",
        DROP_TABLE_IF_EXISTED = "drop table if exists " + DATABASE_NAME,
        QUERY_ALL_APPS = "select * from " + DATABASE_NAME,
        QUERY_AN_APP = String.format(
            "select %s,%s from %s where %s=?",
            PACKAGE_NAME, TITLE, DATABASE_NAME, PACKAGE_NAME
        ),
        QUERY_APP_ATTEMPTS = String.format(
                "select %s from %s where %s=?",
                ATTEMPTS, DATABASE_NAME, PACKAGE_NAME
        ),
        INCREMENT_APP_ATTEMPTS = String.format(
            "UPDATE %s SET %s=%s+1 WHERE %s='%%s'",
            DATABASE_NAME, ATTEMPTS, ATTEMPTS, PACKAGE_NAME
        ),
        DATABASE_CREATE_SCHEMA = String.format(
            "create table %s (%s text primary key,%s text,%s text,%s integer default 0);",
            DATABASE_NAME, PACKAGE_NAME, TITLE, ICON, ATTEMPTS
        );

    public BlockedAppsDatabase(Context context) {
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

    public void addBlockedApp(AppModel app) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        app.getIcon().compress(Bitmap.CompressFormat.PNG, 1, stream);

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(PACKAGE_NAME, app.getPackageName());
        values.put(TITLE, app.getTitle());
        values.put(ICON, stream.toByteArray());

        db.insert(DATABASE_NAME, null, values);
        db.close();
    }

    public void deleteBlockedApp(AppModel app) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DATABASE_NAME, PACKAGE_NAME + "=?", new String[]{app.getPackageName()});
        db.close();
    }

    public void loadAllBlockedApps(List<AppModel> blockedApps) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_ALL_APPS, null);

        blockedApps.clear();
        if(cursor.moveToFirst())
            do blockedApps.add(new AppModel(cursor));
            while(cursor.moveToNext());

        cursor.close();
        db.close();
    }

    public AppModel getBlockedApp(String packageName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(QUERY_AN_APP, new String[] {packageName});
        AppModel app = null;
        if(cursor.moveToFirst())
            app = new AppModel(cursor.getString(1), null, cursor.getString(0));
        cursor.close();
        db.close();
        return app;
    }

    public void incrementAppAttempts(String packageName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(String.format(INCREMENT_APP_ATTEMPTS, packageName));
        db.close();
    }

    public int getAppAttempts(AppModel app) {
        SQLiteDatabase db = getWritableDatabase();
        int attempts = db.rawQuery(QUERY_APP_ATTEMPTS, new String[]{app.getPackageName()}).getInt(0);
        db.close();
        return attempts;
    }
}
