package com.eineao.instablock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eineao.instablock.Fragments.BlockedAppsFragment;
import com.eineao.instablock.Models.AppModel;
import com.eineao.instablock.Models.VHModels.ExpandableViewHolder;
import com.eineao.instablock.R;

/**
 *
 * Created by Omar on 11/30/2017.
 */

public class BlockedAppsAdapter extends ItemsAdapter<ExpandableViewHolder> {
    private ExpandableViewHolder mPreviousHolder;

    public BlockedAppsAdapter(Context context) {
        super(context);
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpandableViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expandable_app_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final ExpandableViewHolder holder, int position) {
        final AppModel app = (AppModel) mItems.get(position);

        holder.mIcon.setImageBitmap(app.getIcon());
        holder.mTitle.setText(app.getTitle());
        holder.mUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDB.deleteBlockedApp(app);
                BlockedAppsFragment.fetchBlockedApps();
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
    }
}
