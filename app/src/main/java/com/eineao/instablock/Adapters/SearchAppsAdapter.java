package com.eineao.instablock.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eineao.instablock.Fragments.BlockedAppsFragment;
import com.eineao.instablock.Helpers.AppDetails;
import com.eineao.instablock.Helpers.InstallReceiver;
import com.eineao.instablock.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 *
 * Created by Omar on 11/30/2017.
 */

public class SearchAppsAdapter extends AppsAdapter<AppViewHolder> {
    private boolean mInstalledApps;

    public SearchAppsAdapter(Context context, boolean installedApps) {
        super(context);
        mInstalledApps = installedApps;
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_view, parent, false));
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, final int position) {
        final AppDetails app = mApps.get(position);

        if(mInstalledApps)
            holder.mIcon.setImageBitmap(app.getIcon());
        else
            Glide.with(mContext).load(app.getIconURL()).into(holder.mIcon);

        holder.mTitle.setText(app.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                    .setTitle("Block App")
                    .setIcon(R.drawable.ic_warning)
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            new AppBlocker().execute(app);
                            Toast.makeText(mContext,
                                    app.getTitle() + " has been blocked!",
                                    Toast.LENGTH_SHORT
                            ).show();
                            ((Activity) mContext).setResult(Activity.RESULT_OK);
                            ((Activity) mContext).finish();
                        }
                    }).setMessage(String.format(
                        "Do you really want to%s block\n%s ?",
                        mInstalledApps ? " uninstall and": "", app.getTitle()
                    )).show();
            }
        });
    }

    private class AppBlocker extends AsyncTask<AppDetails, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(AppDetails... apps) {
            AppDetails app = apps[0];
            if(mInstalledApps)
                InstallReceiver.uninstallPackage(app.getPackageName());
            else
                try {
                    URL iconURL = new URL(app.getIconURL());
                    InputStream iconStream = (InputStream) iconURL.getContent();
                    Bitmap icon = BitmapFactory.decodeStream(iconStream);
                    app.setIcon(icon);
                    app.setIconURL(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            mDB.addBlockedApp(app);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            BlockedAppsFragment.fetchBlockedApps();
            super.onPostExecute(aBoolean);
        }
    }
}
