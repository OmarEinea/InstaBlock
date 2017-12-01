package com.eineao.instablock.Fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eineao.instablock.Adapters.BlockedAppsAdapter;
import com.eineao.instablock.R;

public class BlockedAppsFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    private static BlockedAppsAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public BlockedAppsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.blocked_items_list, container, false);
        mRecyclerView = view.findViewById(R.id.blocked_apps);
        mAdapter = new BlockedAppsAdapter(view.getContext());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mRecyclerView.getContext(), 1)
        );
        fetchBlockedApps();
        return view;
    }

    public static void fetchBlockedApps() {
        new BlockedAppsFetcher().execute();
    }

    private static class BlockedAppsFetcher extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            mAdapter.loadAllBlockedAppsFromDatabase();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean appsUpdated) {
            mAdapter.notifyDataSetChanged();
            super.onPostExecute(appsUpdated);
        }
    }
}