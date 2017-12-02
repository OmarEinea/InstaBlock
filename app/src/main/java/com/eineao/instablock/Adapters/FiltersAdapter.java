package com.eineao.instablock.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eineao.instablock.DBHelpers.FiltersDatabase;
import com.eineao.instablock.Fragments.FiltersFragment;
import com.eineao.instablock.Models.FilterModel;
import com.eineao.instablock.Models.VHModels.ExpandableViewHolder;
import com.eineao.instablock.R;

/**
 *
 * Created by Omar on 11/30/2017.
 */

public class FiltersAdapter extends AppsAdapter<ExpandableViewHolder> {
    private FiltersDatabase mDB;
    private ExpandableViewHolder mPreviousHolder;

    public FiltersAdapter(Context context) {
        super(context);
        mDB = new FiltersDatabase(context);
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpandableViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.expandable_app_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final ExpandableViewHolder holder, int position) {
        final FilterModel filter = (FilterModel) mItems.get(position);

        holder.mIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_filter));
        holder.mTitle.setText(filter.getName());
        holder.mUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDB.deleteFilter(filter);
                FiltersFragment.fetchFilters();
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

    public void loadAllFiltersFromDatabase() {
        mDB.loadAllFilters(mItems);
    }
}
