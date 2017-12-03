package com.eineao.instablock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eineao.instablock.Models.AppModel;
import com.eineao.instablock.Models.VHModels.ExpandableViewHolder;
import com.eineao.instablock.R;

/**
 *
 * Created by Omar on 11/30/2017.
 */

public class BlockedAppsAdapter extends ItemsAdapter<ExpandableViewHolder> {
    public ExpandableViewHolder mPreviousHolder;

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
        }
        holder.mUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDB.deleteBlockedApp(app);
                loadAllBlockedAppsFromDatabase();
                holder.mExpandable.toggle();
                mPreviousHolder = null;
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
