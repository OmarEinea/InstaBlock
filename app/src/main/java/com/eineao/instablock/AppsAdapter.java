package com.eineao.instablock;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
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

/**
 *
 * Created by abdulahiosoble on 10/23/17.
 */

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppsViewHolder>{
    private ArrayList<AppDetails> mApps;
    private Context mContext;

    public AppsAdapter(Context context) {
        mApps = new ArrayList<>();
        mContext = context;
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
            holder.mIcon.setImageDrawable(app.getAppIcon());
        else
            Glide.with(mContext).load(app.getIconURL(96)).into(holder.mIcon);

        holder.mAppName.setText(app.getAppTitle());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AppBlocker().execute(app);
                Toast.makeText(mContext, app.getAppTitle() + " Has been Blocked", Toast.LENGTH_SHORT).show();
                ((Activity) mContext).finish();
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
                    URL iconURL = new URL(app.getIconURL(96));
                    InputStream iconStream = (InputStream) iconURL.getContent();
                    Drawable icon = Drawable.createFromStream(iconStream, "src");
                    app.setAppIcon(icon);
                    app.setIconSubURL(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            // TODO: Add app to database
            return true;
        }
    }
}
