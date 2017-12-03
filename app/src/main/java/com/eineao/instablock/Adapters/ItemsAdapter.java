package com.eineao.instablock.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.eineao.instablock.DBHelpers.BlockedAppsDatabase;
import com.eineao.instablock.Models.AppModel;

import java.util.ArrayList;

/**
 *
 * Created by abdulahiosoble on 10/23/17.
 */

public abstract class ItemsAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH>{
    protected ArrayList mItems;
    protected Context mContext;
    protected BlockedAppsDatabase mDB;

    public ItemsAdapter(Context context) {
        mItems = new ArrayList<>();
        mContext = context;
        mDB = new BlockedAppsDatabase(context);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void clearItems() {
        mItems.clear();
    }

    public void addApp(AppModel app) {
        mItems.add(app);
    }
}
