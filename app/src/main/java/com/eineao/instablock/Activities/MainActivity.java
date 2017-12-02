package com.eineao.instablock.Activities;

import android.content.Intent;
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

import com.eineao.instablock.Fragments.BlockedAppsFragment;
import com.eineao.instablock.Fragments.FiltersFragment;
import com.eineao.instablock.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {
    private View mFabShade;
    private FloatingActionsMenu mFabMenu;
    private FloatingActionButton mPlayStoreButton, mInstalledAppsButton, mFiltersButton;
    private ViewPager mViewPager;
    private TabLayout mTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mViewPager = findViewById(R.id.container);
        mTabs = findViewById(R.id.tabs);
        mFabMenu = findViewById(R.id.fab_menu);
        mFabShade = findViewById(R.id.fab_shade);
        mPlayStoreButton = findViewById(R.id.play_store_button);
        mInstalledAppsButton = findViewById(R.id.installed_apps_button);
        mFiltersButton = findViewById(R.id.filters_button);

        // Set the adapter that will return a fragment for each of the two tabs
        mViewPager.setAdapter(new TabsPagerAdapter(getSupportFragmentManager()));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabs));

        mTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabPosition = tab.getPosition();
                mViewPager.setCurrentItem(tabPosition);
                switch(tabPosition) {
                    case 0:
                        FiltersFragment.collapseExpendedViews();
                        mFiltersButton.setVisibility(View.GONE);
                        mPlayStoreButton.setVisibility(View.VISIBLE);
                        mInstalledAppsButton.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        BlockedAppsFragment.collapseExpendedViews();
                        mFiltersButton.setVisibility(View.VISIBLE);
                        mPlayStoreButton.setVisibility(View.GONE);
                        mInstalledAppsButton.setVisibility(View.GONE);
                        break;
                }
            }
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
        });

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
    }

    private View.OnClickListener getSearchAppsOnClickListener(final Class targetActivity) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, targetActivity));
                mFabMenu.collapse();
                mFabShade.setVisibility(View.INVISIBLE);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BlockedAppsFragment.collapseExpendedViews();
        FiltersFragment.collapseExpendedViews();
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
