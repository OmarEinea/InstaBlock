package com.eineao.instablock;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.os.AsyncTask;
import android.os.Bundle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                new SearchApps().execute(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
}

class SearchApps extends AsyncTask<String, Integer, ArrayList> {

    private final String URL = "https://play.google.com/store/search?hl=en&c=apps&q=";

    @Override
    protected ArrayList doInBackground(String... strings) {
        Elements tags;
        try {
            tags = Jsoup.connect(URL + strings[0]).get().select(".cover");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        ArrayList<AppDetails> apps = new ArrayList<>();
        for(Element tag : tags)
            apps.add(new AppDetails(
                tag.getElementsByTag("img").first(),
                tag.getElementsByTag("a").first()
            ));
        return apps;
    }

    @Override
    protected void onPostExecute(ArrayList apps) {
        super.onPostExecute(apps);
        // TODO: Display apps details in a RecycleView
    }
}