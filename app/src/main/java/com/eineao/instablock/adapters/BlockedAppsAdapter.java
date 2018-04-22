package com.eineao.instablock.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eineao.instablock.models.AppModel;
import com.eineao.instablock.models.VHModels.ExpandableViewHolder;
import com.eineao.instablock.R;

/**
 *
 * Created by Omar on 11/30/2017.
 */

public class BlockedAppsAdapter extends ItemsAdapter<ExpandableViewHolder> {
    public ExpandableViewHolder mPreviousHolder;
    private final String URL = "https://play.google.com/store/apps/details?id=";

    public BlockedAppsAdapter(Context context) {
        super(context);
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpandableViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_expandable_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ExpandableViewHolder holder, int position) {
        final AppModel app = (AppModel) mItems.get(position);

        holder.mIcon.setImageBitmap(app.getIcon());
        holder.mTitle.setText(app.getTitle());
        if(app.getAttempts() > 0) {
            holder.mAttempts.setVisibility(View.VISIBLE);
            holder.mAttempts.setText(app.getAttemptsString());
        } else
            holder.mAttempts.setVisibility(View.GONE);
        holder.mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openURL = new Intent(Intent.ACTION_VIEW);
                openURL.setData(Uri.parse(URL + app.getPackageName()));
                mContext.startActivity(openURL);
            }
        });
        holder.mUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDB.deleteBlockedApp(app);
                loadAllBlockedAppsFromDatabase();
                holder.mExpandable.toggle();
                mPreviousHolder = null;
                Toast.makeText(mContext, app.getTitle() + " is unblocked", Toast.LENGTH_SHORT).show();
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mPreviousHolder != null && mPreviousHolder != holder)
                    mPreviousHolder.mExpandable.collapse();
                holder.mExpandable.toggle();
                mPreviousHolder = holder;
            }
        });
    }

    public void loadAllBlockedAppsFromDatabase() {
        mDB.loadAllBlockedApps(mItems);
        notifyDataSetChanged();
    }
}
