package com.eineao.instablock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite Helper to add and get blocked apps to database
 *
 * Created by Omar on 11/29/2017.
 */

public class BlockedAppsDatabase extends SQLiteOpenHelper {

    private final static int DATABASE_VERSION = 1;
    private final static String DATABASE_NAME = "blocked_apps",
        PACKAGE_NAME = "package_name", TITLE = "title", ICON = "icon",
        DATABASE_CREATE_SCHEMA = "create table " + DATABASE_NAME + " (" +
            PACKAGE_NAME + " text primary key," +
            TITLE + " text," +
            ICON + " text" +
        ");";

    public BlockedAppsDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCHEMA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + DATABASE_NAME);
        onCreate(db);
    }

    public void addBlockedApp(AppDetails app) {
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

    public List<AppDetails> getAllBlockedApp() {
        SQLiteDatabase db = getWritableDatabase();
        List<AppDetails> blockedApps = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + DATABASE_NAME, null);

        if(cursor.moveToFirst())
            do
                blockedApps.add(new AppDetails(
                        cursor.getString(1), getIcon(cursor.getBlob(2)), cursor.getString(0)
                ));
            while(cursor.moveToNext());

        cursor.close();
        db.close();
        return blockedApps;
    }

    private Bitmap getIcon(byte[] bytes) {
        return BitmapFactory.decodeByteArray(bytes , 0, bytes.length);
    }

    public AppDetails getBlockedApp(String packageName) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(
            "select * from " + DATABASE_NAME + " where " + PACKAGE_NAME + "='" + packageName + "'",
            null
        );
        AppDetails app = null;
        if(cursor.moveToFirst())
            app = new AppDetails(cursor.getString(1), cursor.getString(0));
        cursor.close();
        db.close();
        return app;
    }
}
