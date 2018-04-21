package com.eineao.instablock.activities;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eineao.instablock.R;
import com.eineao.instablock.models.AppModel;

public class BlockedDialog extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent data = getIntent();
        final Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_blocked, null);
        view.findViewById(R.id.dismiss_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
            }
        });
        ((TextView) view.findViewById(R.id.app_name)).setText(data.getStringExtra("appName"));
        ((ImageView) view.findViewById(R.id.app_icon)).setImageBitmap(
                AppModel.getIcon(data.getByteArrayExtra("appIcon"))
        );
        dialog.setContentView(view);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(
            new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.dismiss();
                    finish();
                }
            }
        );
        dialog.show();
    }
}
