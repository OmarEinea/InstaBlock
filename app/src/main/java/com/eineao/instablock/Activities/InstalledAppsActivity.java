package com.eineao.instablock.Activities;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.eineao.instablock.Adapters.SearchAppsAdapter;
import com.eineao.instablock.Models.AppModel;
import com.eineao.instablock.R;

import java.util.List;

public class InstalledAppsActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private SearchAppsAdapter mAdapter;
    private PackageManager mPackageManager;
    private List<ApplicationInfo> mInstalledApps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_apps);

        mSearchView = findViewById(R.id.search_view);
        mAdapter = new SearchAppsAdapter(this, true);
        mRecyclerView = findViewById(R.id.items_list);
        mPackageManager = getApplicationContext().getPackageManager();
        mInstalledApps = mPackageManager.getInstalledApplications(0);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new InstalledAppsFetcher().execute(newText);
                return false;
            }
        });

        mRecyclerView.requestFocus();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mRecyclerView.getContext(), 1)
        );

        new InstalledAppsFetcher().execute("");
    }

    private class InstalledAppsFetcher extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            mAdapter.clearItems();
            String appName;
            for(ApplicationInfo app : mInstalledApps)
                if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                    appName = app.loadLabel(mPackageManager).toString();
                    if (appName.toLowerCase().contains(strings[0]))
                        mAdapter.addApp(new AppModel(
                                appName, getIcon(app), app.packageName
                        ));
                }
            return true;
        }

        private Bitmap getIcon(ApplicationInfo app) {
            return ((BitmapDrawable) app.loadIcon(mPackageManager)).getBitmap();
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            mAdapter.notifyDataSetChanged();
            super.onPostExecute(connected);
        }
    }
}
