package com.eineao.instablock.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.eineao.instablock.PinManager;
import com.eineao.instablock.R;

public class SettingsActivity extends AppCompatActivity {
    private TextView changePin;
    private PinManager mPinManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.s_toolbar);
        setSupportActionBar(toolbar);

        mPinManager = new PinManager(this);

        changePin = findViewById(R.id.change_pin);
        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPinManager.signInWithPin(true);
            }
        });
    }
}
