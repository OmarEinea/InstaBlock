package com.eineao.instablock.fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eineao.instablock.adapters.FiltersAdapter;
import com.eineao.instablock.R;

public class FiltersFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    public static FiltersAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_items_list, container, false);
        mAdapter = new FiltersAdapter(view.getContext());
        mRecyclerView = view.findViewById(R.id.items_list);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mRecyclerView.getContext(), 1)
        );
        return view;
    }

    @Override
    public void onResume() {
        mAdapter.loadAllFiltersFromDatabase();
        super.onResume();
    }

    public static void collapseExpendedViews() {
        if(mAdapter.mPreviousHolder != null)
            mAdapter.mPreviousHolder.mExpandable.collapse();
    }
}