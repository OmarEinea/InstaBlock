package com.eineao.instablock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by abdulahiosoble on 11/8/17.
 *
 * Temporary
 */

public class TempBlockedAdapter extends RecyclerView.Adapter<TempBlockedAdapter.TempViewHolder> {
    ArrayList<AppDetails> mApps;

    public TempBlockedAdapter( Context mContext) {
        mApps = new ArrayList<>();
        this.mContext = mContext;
    }

    Context mContext;
    @Override
    public TempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.blocked_list, parent, false);
        return new TempViewHolder(v);
    }

    @Override
    public void onBindViewHolder(TempViewHolder holder, int position) {
        final AppDetails app = mApps.get(position);

        if(app.getAppIcon() == null)
            Glide.with(mContext).load(app.getIconURL(96)).into(holder.mIcon);
        else
            holder.mIcon.setImageDrawable(app.getAppIcon());

        holder.mAppName.setText(app.getAppTitle());


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void clearApps() {
        mApps.clear();
    }

    public void addApp(AppDetails app) {
        mApps.add(app);
    }

    public class TempViewHolder extends RecyclerView.ViewHolder {
        private TextView mAppName;
        private ImageView mIcon;

        public TempViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.app_icon_blocked);
            mAppName = itemView.findViewById(R.id.app_name_blocked);

        }
    }
}
