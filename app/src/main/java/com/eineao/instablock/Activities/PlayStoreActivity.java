package com.eineao.instablock.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;

import com.eineao.instablock.Adapters.SearchAppsAdapter;
import com.eineao.instablock.Models.AppModel;
import com.eineao.instablock.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PlayStoreActivity extends AppCompatActivity {

    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private SearchAppsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apps_search);

        mSearchView = findViewById(R.id.search_view);
        mAdapter = new SearchAppsAdapter(this, false);
        mRecyclerView = findViewById(R.id.search_results);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new PlayStoreFetcher().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(mRecyclerView.getContext(), 1)
        );
    }

    private class PlayStoreFetcher extends AsyncTask<String, Integer, Boolean> {
        private final String URL = "https://play.google.com/store/search?hl=en&c=apps&q=";

        @Override
        protected Boolean doInBackground(String... strings) {
            mAdapter.clearApps();
            Elements tags;

            try {
                tags = Jsoup.connect(URL + strings[0]).get().select(".cover");
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            for(Element tag : tags)
                mAdapter.addApp(new AppModel(
                        tag.getElementsByTag("img").first(),
                        tag.getElementsByTag("a").first()
                ));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean connected) {
            mAdapter.notifyDataSetChanged();
            super.onPostExecute(connected);
        }
    }
}
