package com.eineao.instablock.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.eineao.instablock.DBHelpers.FiltersDatabase;
import com.eineao.instablock.Models.FilterModel;
import com.eineao.instablock.Models.VHModels.ExpandableViewHolder;
import com.eineao.instablock.R;

/**
 *
 * Created by Omar on 11/30/2017.
 */

public class FiltersAdapter extends ItemsAdapter<ExpandableViewHolder> {
    private FiltersDatabase mDB;
    public ExpandableViewHolder mPreviousHolder = null;

    public FiltersAdapter(Context context) {
        super(context);
        mDB = new FiltersDatabase(context);
    }

    @Override
    public ExpandableViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExpandableViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_expandable_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ExpandableViewHolder holder, int position) {
        final FilterModel filter = (FilterModel) mItems.get(position);

        holder.mIcon.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_filter_single));
        holder.mTitle.setText(filter.getName());
        if(filter.getAttempts() > 0) {
            holder.mAttempts.setVisibility(View.VISIBLE);
            holder.mAttempts.setText(filter.getAttemptsString());
        }
        holder.mUnblock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDB.deleteFilter(filter);
                loadAllFiltersFromDatabase();
                holder.mExpandable.toggle();
                mPreviousHolder = null;
            }
        });
        ((ImageView) holder.mInfo.getChildAt(0)).setImageResource(R.drawable.ic_edit);
        ((TextView) holder.mInfo.getChildAt(1)).setText(R.string.edit);
        holder.mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modifyFilter(filter);
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
        ((TextView) holder.itemView.findViewById(R.id.item_description)).setText(filter.getKeywordsString());
    }

    public void loadAllFiltersFromDatabase() {
        mDB.loadAllFilters(mItems);
        notifyDataSetChanged();
    }

    public void modifyFilter(final FilterModel filter) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialog = inflater.inflate(R.layout.dialog_modify_filter, null);
        final EditText filterName = dialog.findViewById(R.id.filter_name);
        final EditText keywords = dialog.findViewById(R.id.keywords);
        String action;
        if(filter != null) {
            filterName.setText(filter.getName());
            keywords.setText(filter.getKeywordsString());
            action = mContext.getResources().getString(R.string.edit);
        } else action = mContext.getResources().getString(R.string.add);
        new AlertDialog.Builder(mContext)
            .setTitle(action + " a Filter")
            .setIcon(R.drawable.ic_filter_multi)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(action, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(filter != null)
                        mDB.deleteFilter(filter);
                    mDB.addFilter(
                        new FilterModel(
                            filterName.getText().toString(),
                            keywords.getText().toString()
                        )
                    );
                    loadAllFiltersFromDatabase();
                }
            }).setView(dialog).create().show();
    }
}
