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

public class PasswordManager {
    private final String PASSWORD = "password";
    private SharedPreferences mStorage;
    private Activity mActivity;

    public PasswordManager(Activity activity) {
        mActivity = activity;
        mStorage = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    public void setPassword(String password) {
        mStorage.edit().putString(PASSWORD, password).apply();
    }

    public String getPassword() {
        return mStorage.getString(PASSWORD, "");
    }

    public void clearPassword() {
        mStorage.edit().clear().apply();
    }

    public void registerNewPassword(final boolean change) {
        View dialog = LayoutInflater.from(mActivity).inflate(R.layout.dialog_manage_password, null);
        final EditText newPassword = dialog.findViewById(R.id.password);
        final EditText confirmPassword = dialog.findViewById(R.id.confirm_password);

        final AlertDialog alertDialog = buildDialog(dialog, R.string.create_password);

        dialog.findViewById(R.id.enter_password_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = newPassword.getText().toString();

                if(password.isEmpty())
                    Toast.makeText(mActivity, "The Password cannot be empty", Toast.LENGTH_SHORT).show();
                else if(!confirmPassword.getText().toString().equals(password))
                    Toast.makeText(mActivity, "The two Passwords didn't match", Toast.LENGTH_SHORT).show();
                else {
                    if(change)
                        Toast.makeText(mActivity, "Your Password is changed", Toast.LENGTH_SHORT).show();
                    else {
                        Toast.makeText(mActivity, "Welcome to InstaBlock!", Toast.LENGTH_SHORT).show();
                        mActivity.findViewById(R.id.block_apps_hint).setVisibility(View.VISIBLE);
                    }
                    setPassword(password);
                    alertDialog.dismiss();
                }
            }
        });
        dialog.findViewById(R.id.cancel_password_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        alertDialog.show();
        showSoftKeyboard(newPassword);
    }

    public void signInWithPassword(final boolean change) {
        View dialog = LayoutInflater.from(mActivity).inflate(R.layout.dialog_manage_password, null);
        dialog.findViewById(R.id.hide_on_sign_in).setVisibility(View.GONE);
        final EditText password = dialog.findViewById(R.id.password);

        final AlertDialog alertDialog = buildDialog(dialog, R.string.enter_password);

        dialog.findViewById(R.id.enter_password_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getPassword().equals(password.getText().toString())) {
                    alertDialog.dismiss();

                    if(change) {
                        clearPassword();
                        registerNewPassword(true);
                    } else
                        Toast.makeText(mActivity, "Welcome back!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mActivity, "Wrong Password", Toast.LENGTH_SHORT).show();
                    password.setText("");
                }
            }
        });
        dialog.findViewById(R.id.cancel_password_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.finish();
            }
        });

        alertDialog.show();
        showSoftKeyboard(password);
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
