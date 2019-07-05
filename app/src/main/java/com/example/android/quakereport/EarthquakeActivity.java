/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.LoaderManager.LoaderCallbacks;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderCallbacks<List<EarthQuake>> {
    private TextView mEmptyState;
    private ProgressBar mProgressBar;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private CustomAdapter mAdapter;
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "OnCreateLoader");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
        Log.v(LOG_TAG, "OnLoadFinished");
        mEmptyState.setText("No earthquakes found!");
        mProgressBar.setVisibility(View.INVISIBLE);
        mAdapter.clear();
        if(data!=null&&!data.isEmpty()){
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<EarthQuake>> loader) {
        Log.v(LOG_TAG, "OnLoadReset");
        mAdapter.clear();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        final ArrayList<EarthQuake> earthQuakes = new ArrayList<EarthQuake>();
        mAdapter = new CustomAdapter(this, earthQuakes);


        // Find a reference to the {@link ListView} in the layout
        ListView earthquakeListView = (ListView) findViewById(R.id.list);


        earthquakeListView.setAdapter(mAdapter);

        mEmptyState = (TextView) findViewById(R.id.emptyTextView);
        earthquakeListView.setEmptyView(mEmptyState);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_circular);
        if(isConnected) {
            Log.v(LOG_TAG, "initLoader");
            getLoaderManager().initLoader(0, null, this);
        }
        else{
            mProgressBar.setVisibility(View.INVISIBLE);
            mEmptyState.setText("No internet Connection!");
        }
        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EarthQuake currentQuake = earthQuakes.get(i);
                String url=currentQuake.getUrl();
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });
    }
}
