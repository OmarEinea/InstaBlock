package com.eineao.instablock.Activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;

import com.eineao.instablock.Adapters.FiltersAdapter;
import com.eineao.instablock.Models.FilterModel;
import com.eineao.instablock.R;

/**
 *
 * Created by Omar on 12/3/2017.
 */

public class PredefinedFiltersActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FiltersAdapter mAdapter;
    private Resources mResources;
    private final Integer[] filtersNames = new Integer[] {
        R.string.social_media, R.string.video,
        R.string.proxies, R.string.chatting,
        R.string.root, R.string.gambling,
        R.string.violence, R.string.dating,
        R.string.music, R.string.gaming,
        R.string.adults_content
    },
    filtersKeywords = new Integer[] {
        R.string.social_media_keywords, R.string.video_keywords,
        R.string.proxies_keywords, R.string.chatting_keywords,
        R.string.root_keywords, R.string.gambling_keywords,
        R.string.violence_keywords, R.string.dating_keywords,
        R.string.music_keywords, R.string.gaming_keywords,
        R.string.adults_content_keywords
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predefined_filters);

        mAdapter = new FiltersAdapter(this, false);
        mRecyclerView = findViewById(R.id.items_list);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mRecyclerView.getContext(), 1)
        );
        mResources = getApplicationContext().getResources();
        for(int i = 0; i < filtersNames.length; i++)
            mAdapter.addFilter(new FilterModel(
                    mResources.getString(filtersNames[i]),
                    mResources.getString(filtersKeywords[i])
            ));
        mAdapter.notifyDataSetChanged();
    }
}
