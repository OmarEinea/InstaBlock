package com.eineao.instablock;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by abdulahiosoble on 10/23/17.
 */

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppsViewHolder>{
    private ArrayList<AppDetails> mApps;
    private Context mContext;
    private BlockedAppsDatabase mDB;

    public AppsAdapter(Context context) {
        mApps = new ArrayList<>();
        mContext = context;
        mDB = new BlockedAppsDatabase(context);
    }

    @Override
    public AppsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_item, parent, false);
        return new AppsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppsViewHolder holder, final int position) {
        final AppDetails app = mApps.get(position);

        if(app.isInstalled())
            holder.mIcon.setImageBitmap(app.getIcon());
        else
            Glide.with(mContext).load(app.getIconURL()).into(holder.mIcon);

        holder.mAppName.setText(app.getTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mContext.getClass().getSimpleName().equals("MainActivity")) {
                    Toast.makeText(mContext, app.getTitle() + " is Blocked", Toast.LENGTH_SHORT).show();
                } else {
                    new AppBlocker().execute(app);
                    Toast.makeText(mContext, app.getTitle() + " Has been Blocked", Toast.LENGTH_SHORT).show();
                    ((Activity) mContext).finish();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public void clearApps() {
        mApps.clear();
    }

    public void addApp(AppDetails app) {
        mApps.add(app);
    }

    public void addApps(List<AppDetails> apps) {
        mApps.addAll(apps);
    }

    public class AppsViewHolder extends RecyclerView.ViewHolder{
        private TextView mAppName;
        private ImageView mIcon;

        public AppsViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.app_icon);
            mAppName = itemView.findViewById(R.id.app_name);
        }
    }

    private class AppBlocker extends AsyncTask<AppDetails, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(AppDetails... apps) {
            AppDetails app = apps[0];
            if(app.isInstalled()) {
                InstallReceiver.uninstallPackage(app.getPackageName());
                app.setInstalled(false);
            } else
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
            new MainActivity.BlockedAppsFetcher().execute();
            return true;
        }
    }
}
