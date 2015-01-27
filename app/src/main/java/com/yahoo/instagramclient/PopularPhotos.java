package com.yahoo.instagramclient;

import android.app.ActionBar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;

import android.os.Bundle;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;

import java.util.ArrayList;

public class PopularPhotos extends Activity {

    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_photos);
        getActionBar().setTitle(R.string.popular_photos_caption);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPhotosAsync();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        fetchPhotosAsync();
    }

    private void fetchPhotosAsync() {
        final PopularPhotos instance;
        instance = this;
        PhotoFetcher.fetchPopularPhotos(new PhotoFetcherCallback() {
            @Override
            public void onSuccess(ArrayList<InstagramPhoto> photos) {
                swipeContainer.setRefreshing(false);
                InstagramPhotosAdapter photosAdapter;
                photosAdapter = new InstagramPhotosAdapter(instance, photos);
                ListView lvPhotos = (ListView) findViewById(R.id.lvPhotos);
                lvPhotos.setAdapter(photosAdapter);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_popular_photos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
