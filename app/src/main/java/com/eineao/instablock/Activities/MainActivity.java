package com.eineao.instablock.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.eineao.instablock.Fragments.BlockedAppsFragment;
import com.eineao.instablock.Fragments.FiltersFragment;
import com.eineao.instablock.PrefUtils;
import com.eineao.instablock.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {
    private View mFabShade;
    private FloatingActionsMenu mFabMenu;
    private FloatingActionButton mPlayStoreButton, mInstalledAppsButton;
    private ViewPager mViewPager;
    private String pin;
    private PrefUtils pref;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mSharedPreferences = this.getPreferences(Context.MODE_PRIVATE);

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        // Set the adapter that will return a fragment for each of the two tabs
        mViewPager.setAdapter(new SectionsPagerAdapter(getSupportFragmentManager()));

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mFabMenu = findViewById(R.id.fab_menu);
        mFabShade = findViewById(R.id.fab_shade);
        mPlayStoreButton = findViewById(R.id.play_store_button);
        mInstalledAppsButton = findViewById(R.id.installed_apps_button);

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

        pref = new PrefUtils(this);
        pref.clearPin();
        pin = pref.getPin();
        if(pin.equals("")){
            newPin();
        }
        else{
            promptPin();
        }

    }

    private void newPin() {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.new_pin_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, R.style.myDialog)
        );

        alertDialogBuilder.setView(promptsView);

        final EditText newPin = promptsView.findViewById(R.id.new_pin);
        final EditText confirmPin = promptsView.findViewById(R.id.confirm_pin);

        final Button cancel = promptsView.findViewById(R.id.n_cancel);
        final Button enter = promptsView.findViewById(R.id.n_enter);

        alertDialogBuilder.setCancelable(false);
        AlertDialog alertDialog = alertDialogBuilder.create();
        final AlertDialog finalDialog = alertDialog;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
                finish();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String new_pin = newPin.getText().toString().trim();
                final String confirm_pin = confirmPin.getText().toString().trim();

                if(confirm_pin.equals(new_pin)){
                    pref.setPin(new_pin);
                    finalDialog.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(), "The PINs do not match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        // show it
        alertDialog.show();
    }

    private void promptPin() {
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.enter_pin_dialog, null);
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, R.style.myDialog)
        );
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.pin);

        Button enter = promptsView.findViewById(R.id.e_enter);
        Button cancel = promptsView.findViewById(R.id.e_cancel);

        alertDialogBuilder.setCancelable(false);

        final AlertDialog alertDialog = alertDialogBuilder.create();
        final AlertDialog finalDialog = alertDialog;

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalDialog.dismiss();
                finish();
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pin.equals(userInput.getText().toString())) {
                    Log.i("SHAREDPREF", pin);
                    Toast.makeText(getApplicationContext(), "Welcome",
                            Toast.LENGTH_SHORT).show();
                    finalDialog.dismiss();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong PIN",
                            Toast.LENGTH_SHORT).show();
                    finalDialog.dismiss();
                    finalDialog.show();
                    userInput.setText("");
                }
            }
        });

        // show it
        alertDialog.show();
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

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
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
