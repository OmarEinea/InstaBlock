package com.eineao.instablock.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eineao.instablock.Models.AppModel;
import com.eineao.instablock.DBHelpers.BlockedAppsDatabase;
import com.eineao.instablock.R;

import java.util.ArrayList;

/**
 *
 * Created by abdulahiosoble on 10/23/17.
 */

public abstract class AppsAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    protected ArrayList<AppModel> mApps;
    protected Context mContext;
    protected BlockedAppsDatabase mDB;

    public AppsAdapter(Context context) {
        mApps = new ArrayList<>();
        mContext = context;
        mDB = new BlockedAppsDatabase(context);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public void clearApps() {
        mApps.clear();
    }

    public void addApp(AppModel app) {
        mApps.add(app);
    }

}

class AppViewHolder extends RecyclerView.ViewHolder {
    protected TextView mTitle;
    protected ImageView mIcon;

    public AppViewHolder(View itemView) {
        super(itemView);
        mIcon = itemView.findViewById(R.id.app_icon);
        mTitle = itemView.findViewById(R.id.app_title);
    }
}
