package com.eineao.instablock;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {
    private View mFabShade;
    private FloatingActionsMenu mFabMenu;
    private FloatingActionButton mPlayStoreButton, mInstalledAppsButton, mPackageNameButton;
    private RecyclerView mRecyclerView;
    private AppsAdapter mAdapter;
    public static BlockedAppsDatabase mDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFabMenu = findViewById(R.id.fab_menu);
        mFabShade = findViewById(R.id.fab_shade);
        mPlayStoreButton = findViewById(R.id.play_store_button);
        mInstalledAppsButton = findViewById(R.id.installed_apps_button);
        mRecyclerView = findViewById(R.id.blocked_apps);
        mAdapter = new AppsAdapter(this);
        mDB = new BlockedAppsDatabase(this);

        mFabMenu.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.toggle();
                mFabShade.setVisibility(
                        mFabMenu.isExpanded() ? View.VISIBLE : View.INVISIBLE
                );
            }
        });

        mFabShade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.toggle();
                mFabShade.setVisibility(View.INVISIBLE);
            }
        });

        mPlayStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PlayStoreActivity.class));
                mFabMenu.collapse();
                mFabShade.setVisibility(View.INVISIBLE);
            }
        });

        mInstalledAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, InstalledAppsActivity.class));
                mFabMenu.collapse();
                mFabShade.setVisibility(View.INVISIBLE);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mRecyclerView.getContext(), 1)
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        new BlockedAppsFetcher().execute();
    }

    private class BlockedAppsFetcher extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            mAdapter.clearApps();
            mAdapter.addApps(mDB.getAllBlockedApp());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean appsUpdated) {
            mAdapter.notifyDataSetChanged();
            Log.d("OnResume", "Refreshed blocked apps list");
            super.onPostExecute(appsUpdated);
        }
    }
}
