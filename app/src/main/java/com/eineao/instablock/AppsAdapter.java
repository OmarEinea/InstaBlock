package com.eineao.instablock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 *
 * Created by abdulahiosoble on 10/23/17.
 */

public class AppsAdapter extends RecyclerView.Adapter<AppsAdapter.AppsViewHolder>{
    private ArrayList<AppDetails> apps;
    private Context mContext;

    public AppsAdapter(ArrayList<AppDetails> apps, Context context) {
        this.apps = apps;
        this.mContext = context;
    }

    @Override
    public AppsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.app_item, parent, false);
        return new AppsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppsViewHolder holder, int position) {
        AppDetails app = apps.get(position);

        Glide.with(mContext).load(app.getIconURL(96)).into(holder.mIcon);

        holder.mAppName.setText(app.getAppTitle());
        Log.i("Adapter", "Was Called " + position);
    }

    @Override
    public int getItemCount() {
        return apps.size();
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
}