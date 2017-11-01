package com.eineao.instablock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class MainActivity extends AppCompatActivity {
    private final int SEARCH = 1;
    private View mFabShade;
    private FloatingActionsMenu mFabMenu;
    private FloatingActionButton mPlayStoreButton, mInstalledAppsButton, mPackageNameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        mPlayStoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, PlayStoreActivity.class), SEARCH);
                mFabMenu.collapse();
            }
        });

        mInstalledAppsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this, InstalledAppsActivity.class), SEARCH);
                mFabMenu.collapse();
            }
        });
    }
}
