package com.eineao.instablock.models;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eineao.instablock.R;

import net.cachapa.expandablelayout.ExpandableLayout;

/**
 *
 * Created by Omar on 12/2/2017.
 */

public class VHModels {

    public static class AppViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public ImageView mIcon;

        public AppViewHolder(View itemView) {
            super(itemView);
            mIcon = itemView.findViewById(R.id.item_icon);
            mTitle = itemView.findViewById(R.id.item_title);
        }
    }

    public static class ExpandableViewHolder extends AppViewHolder {
        public ExpandableLayout mExpandable;
        public LinearLayout mUnblock, mInfo;
        public TextView mAttempts;

        public ExpandableViewHolder(View itemView) {
            super(itemView);
            mExpandable = itemView.findViewById(R.id.expandable);
            mUnblock = itemView.findViewById(R.id.unblock_btn);
            mAttempts = itemView.findViewById(R.id.attempts);
            mInfo = itemView.findViewById(R.id.info_btn);
        }
    }
}
