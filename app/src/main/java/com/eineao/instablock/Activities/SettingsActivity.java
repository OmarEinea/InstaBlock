package com.eineao.instablock.Activities;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eineao.instablock.PrefUtils;
import com.eineao.instablock.R;

public class SettingsActivity extends AppCompatActivity {
    private TextView changePin;
    private PrefUtils pref;
    private String pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.s_toolbar);
        setSupportActionBar(toolbar);

        pref = new PrefUtils(this);
        pin = pref.getPin();
        Log.i("-----------", "-----------------");
        Log.i("-----------", pin);

        changePin = findViewById(R.id.change_pin);
        changePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                promptPin();
            }
        });
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
            }
        });

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String input = userInput.getText().toString();
                if(input.equals(pin)) {
                    Log.i("SHAREDPREF", pin);
                    Toast.makeText(getApplicationContext(), "Choose New Pin",
                            Toast.LENGTH_SHORT).show();
                    finalDialog.dismiss();

                    pref.clearPin();
                    newPin();

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
}
