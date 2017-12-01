package com.eineao.instablock.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.eineao.instablock.Fragments.BlockedAppsFragment;
import com.eineao.instablock.Models.AppModel;
import com.eineao.instablock.R;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 *
 * Created by Omar on 11/30/2017.
 */

public class BlockedAppsAdapter extends AppsAdapter<BlockedAppsAdapter.ExpandableAppViewHolder> {

    private ExpandableAppViewHolder mPreviousHolder;
    public BlockedAppsAdapter(Context context) {
        super(context);
    }

    @Override
    public ExpandableAppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpandableAppViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expandable_app_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final ExpandableAppViewHolder holder, int position) {
        final AppModel app = mApps.get(position);

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
        mDB.loadAllBlockedApps(mApps);
    }

    protected class ExpandableAppViewHolder extends AppViewHolder {
        protected ExpandableLayout mExpandable;
        protected LinearLayout mUnblock, mTimer, mOpenInfo;

        public ExpandableAppViewHolder(View itemView) {
            super(itemView);
            mExpandable = itemView.findViewById(R.id.expandable);
            mUnblock = itemView.findViewById(R.id.unblock_btn);
        }
    }
}
