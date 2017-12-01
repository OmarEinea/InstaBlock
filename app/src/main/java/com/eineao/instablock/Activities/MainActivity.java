package com.eineao.instablock.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.eineao.instablock.Adapters.BlockedAppsAdapter;
import com.eineao.instablock.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {
    private View mFabShade;
    private FloatingActionsMenu mFabMenu;
    private FloatingActionButton mPlayStoreButton, mInstalledAppsButton, mPackageNameButton;
    private RecyclerView mRecyclerView;
    private BlockedAppsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFabMenu = findViewById(R.id.fab_menu);
        mFabShade = findViewById(R.id.fab_shade);
        mPlayStoreButton = findViewById(R.id.play_store_button);
        mInstalledAppsButton = findViewById(R.id.installed_apps_button);
        mRecyclerView = findViewById(R.id.blocked_apps);
        mAdapter = new BlockedAppsAdapter(this);

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

        mPlayStoreButton.setOnClickListener(getSearchAppsOnClickListener(PlayStoreActivity.class));
        mInstalledAppsButton.setOnClickListener(getSearchAppsOnClickListener(InstalledAppsActivity.class));

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mRecyclerView.getContext(), 1)
        );

        new BlockedAppsFetcher().execute();
    }

    private View.OnClickListener getSearchAppsOnClickListener(final Class targetActivity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, targetActivity), 0);
                mFabMenu.collapse();
                mFabShade.setVisibility(View.INVISIBLE);
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK)
            new BlockedAppsFetcher().execute();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @SuppressLint("StaticFieldLeak")
    private class BlockedAppsFetcher extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            mAdapter.loadAllBlockedAppsFromDatabase();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean appsUpdated) {
            mAdapter.notifyDataSetChanged();
            super.onPostExecute(appsUpdated);
        }
    }
}
