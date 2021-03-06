package com.eineao.instablock.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.eineao.instablock.R;
import com.eineao.instablock.fragments.BlockedAppsFragment;
import com.eineao.instablock.fragments.FiltersFragment;
import com.eineao.instablock.managers.PasswordManager;
import com.eineao.instablock.managers.StorageManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String INSTALLED_APPS = "installed_apps";
    private ViewPager mViewPager;
    private TabLayout mTabs;
    private View mFabShade;
    private StorageManager mStorageManager;
    private PasswordManager mPasswordManager;
    private FloatingActionsMenu mFabMenu;
    private FloatingActionButton mPlayStoreButton, mInstalledAppsButton,
                                 mPredefinedFiltersButton, mCustomFiltersButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        mTabs = findViewById(R.id.tabs);
        mFabMenu = findViewById(R.id.fab_menu);
        mFabShade = findViewById(R.id.fab_shade);

        mPlayStoreButton = findViewById(R.id.play_store_btn);
        mInstalledAppsButton = findViewById(R.id.installed_apps_btn);
        mCustomFiltersButton = findViewById(R.id.custom_filters_btn);
        mPredefinedFiltersButton = findViewById(R.id.predefined_filters_btn);

        mStorageManager = new StorageManager(this);
        mPasswordManager = new PasswordManager(this, mStorageManager);

        // Set the adapter that will return a fragment for each of the two tabs
        mViewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));

        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                mViewPager.setCurrentItem(tabPosition);
                collapseAllExpendedViews();
                switch(tabPosition) {
                    case 0:
                        mCustomFiltersButton.setVisibility(View.GONE);
                        mPredefinedFiltersButton.setVisibility(View.GONE);
                        mPlayStoreButton.setVisibility(View.VISIBLE);
                        mInstalledAppsButton.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        mCustomFiltersButton.setVisibility(View.VISIBLE);
                        mPredefinedFiltersButton.setVisibility(View.VISIBLE);
                        mPlayStoreButton.setVisibility(View.GONE);
                        mInstalledAppsButton.setVisibility(View.GONE);
                        break;
                }
            }
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        mFabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                collapseAllExpendedViews();
            }
            public void onMenuCollapsed() {}
        });
        mFabMenu.getChildAt(4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.toggle();
                mFabShade.setVisibility(
                        mFabMenu.isExpanded() ? View.VISIBLE : View.INVISIBLE
                );
                findViewById(R.id.block_apps_hint).setVisibility(View.GONE);
            }
        });

        mFabShade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFabMenu.toggle();
                mFabShade.setVisibility(View.INVISIBLE);
            }
        });

        mPlayStoreButton.setOnClickListener(getOpenActivityOnClickListener(PlayStoreActivity.class));
        mInstalledAppsButton.setOnClickListener(getOpenActivityOnClickListener(InstalledAppsActivity.class));
        mPredefinedFiltersButton.setOnClickListener(getOpenActivityOnClickListener(PredefinedFiltersActivity.class));
        mCustomFiltersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FiltersFragment.mAdapter.modifyFilter(null);
                mFabMenu.collapse();
                mFabShade.setVisibility(View.INVISIBLE);
            }
        });

        if(mPasswordManager.isFirstTime()) {
            startActivity(new Intent(this, WelcomeActivity.class));
            mPasswordManager.registerNewPassword(false);
        } else
            mPasswordManager.signInWithPassword(false);
    }

    private View.OnClickListener getOpenActivityOnClickListener(final Class targetActivity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, targetActivity));
                mFabMenu.collapse();
                mFabShade.setVisibility(View.INVISIBLE);
            }
        };
    }

    private void collapseAllExpendedViews() {
        BlockedAppsFragment.collapseExpendedViews();
        FiltersFragment.collapseExpendedViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.block_new_installs).setChecked(mStorageManager.has(INSTALLED_APPS));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.block_new_installs:
                if (item.isChecked()) {
                    item.setChecked(false);
                    mStorageManager.pop(INSTALLED_APPS);
                } else {
                    item.setChecked(true);
                    Set<String> apps = new HashSet<>();
                    for(ApplicationInfo app : getPackageManager().getInstalledApplications(0))
                        apps.add(app.packageName);
                    mStorageManager.putStrings(INSTALLED_APPS, apps);
                } break;
            case R.id.change_password:
                mPasswordManager.signInWithPassword(true);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        collapseAllExpendedViews();
    }

    public class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new BlockedAppsFragment();
                case 1:
                    return new FiltersFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
