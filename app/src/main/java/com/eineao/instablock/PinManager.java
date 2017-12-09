package com.eineao.instablock;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 *
 * Created by abdulahiosoble on 12/2/17.
 */

public class PinManager {
    private final String PIN = "pin";
    private SharedPreferences mStorage;
    private Activity mActivity;

    public PinManager(Activity activity) {
        mActivity = activity;
        mStorage = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setPin(String pin) {
        mStorage.edit().putString(PIN, pin).apply();
    }

    public String getPin() {
        return mStorage.getString(PIN, "");
    }

    public void clearPin() {
        mStorage.edit().clear().apply();
    }

    public void registerNewPin(final boolean change) {
        View dialog = LayoutInflater.from(mActivity).inflate(R.layout.dialog_manage_pin, null);
        final EditText newPin = dialog.findViewById(R.id.pin);
        final EditText confirmPin = dialog.findViewById(R.id.confirm_pin);

        final AlertDialog alertDialog = buildDialog(dialog, R.string.create_pin);

        dialog.findViewById(R.id.enter_pin_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pin = newPin.getText().toString();

                if(pin.isEmpty())
                    Toast.makeText(mActivity, "The PIN cannot be empty", Toast.LENGTH_SHORT).show();
                else if(!confirmPin.getText().toString().equals(pin))
                    Toast.makeText(mActivity, "The PINs do not match", Toast.LENGTH_SHORT).show();
                else {
                    if(change)
                        Toast.makeText(mActivity, "Your PIN is changed", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(mActivity, "Welcome to InstaBlock!", Toast.LENGTH_SHORT).show();
                    setPin(pin);
                    alertDialog.dismiss();
                }
            }
        });
        dialog.findViewById(R.id.cancel_pin_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        alertDialog.show();
        showSoftKeyboard(newPin);
    }

    public void signInWithPin(final boolean change) {
        View dialog = LayoutInflater.from(mActivity).inflate(R.layout.dialog_manage_pin, null);
        dialog.findViewById(R.id.hide_on_sign_in).setVisibility(View.GONE);
        final EditText pin = dialog.findViewById(R.id.pin);

        final AlertDialog alertDialog = buildDialog(dialog, R.string.enter_pin);

        dialog.findViewById(R.id.enter_pin_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getPin().equals(pin.getText().toString())) {
                    alertDialog.dismiss();

                    if(change) {
                        clearPin();
                        registerNewPin(true);
                    } else
                        Toast.makeText(mActivity, "Welcome back!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "Wrong PIN", Toast.LENGTH_SHORT).show();
                    pin.setText("");
                }
            }
        });
        dialog.findViewById(R.id.cancel_pin_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        alertDialog.show();
        showSoftKeyboard(pin);
    }

    private AlertDialog buildDialog(View dialog, int title) {
        return new AlertDialog
                .Builder(mActivity)
                .setView(dialog)
                .setIcon(R.drawable.ic_security)
                .setTitle(title)
                .setCancelable(false)
                .create();
    }

    private void showSoftKeyboard(final EditText editText) {
        editText.requestFocus();
        editText.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((InputMethodManager) mActivity
                        .getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(editText, 0);
            }
        }, 300);
    }
}
