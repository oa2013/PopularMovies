package agafonova.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import agafonova.com.popularmovies.adapters.ResultAdapter;
import agafonova.com.popularmovies.model.Result;
import agafonova.com.popularmovies.util.DataLoader;
import agafonova.com.popularmovies.util.DataLoader2;
import agafonova.com.popularmovies.util.JsonUtils;
import agafonova.com.popularmovies.util.NetworkUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
* @author Olga Agafonova
* @date May 2, 2018
* Android Nanodegree Movie Poster Project
* */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, ResultAdapter.ResourceClickListener{

    private String mApiKey;
    private ArrayList<Result> popularityResults = null;
    private ArrayList<Result> topRatedResults = null;
    private ListView posterView;
    private ResultAdapter adapter;

    @BindView(R.id.rv_posters)
    RecyclerView mRecyclerView;

    @BindView(R.id.sort_button_popularity)
    Button mPopularityButton;

    @BindView(R.id.sort_button_rating)
    Button mRatingButton;

    @BindView(R.id.error_message)
    TextView mErrorTextView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private boolean sortByPopularity;
    private boolean sortByRating;

    private static final int loader1 = 1;
    private static final int loader2 = 2;

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mErrorTextView.setVisibility(View.INVISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns(), GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        if (getSupportLoaderManager().getLoader(1) != null) {
            getSupportLoaderManager().initLoader(1, null, this);
        }

        if (getSupportLoaderManager().getLoader(2) != null) {
            getSupportLoaderManager().initLoader(2, null, this);
        }

        /*
         * To Reviewers: you need to create an xml file in res/values
         * and use your own MovieDB API key
         * <resources>
         * <string name="api_key">123456789</string>
         * </resources>
         * */
        try {
            mApiKey = getResources().getString(R.string.api_key);
        } catch (Exception e) {
            e.printStackTrace();
            mApiKey = "";
        }

        if(savedInstanceState == null) {

            sortByRating = false;
            sortByPopularity = false;
            adapter = new ResultAdapter(this);
            mRecyclerView.setAdapter(adapter);

            checkNetworkAndGetData();
        }
        else if (savedInstanceState != null){

            sortByPopularity = savedInstanceState.getBoolean("sortByPopularity");
            sortByRating = savedInstanceState.getBoolean("sortByRating");

            popularityResults = savedInstanceState.getParcelableArrayList("moviesPopular");
            topRatedResults = savedInstanceState.getParcelableArrayList("moviesTopRated");

            adapter = new ResultAdapter(this);
            mRecyclerView.setAdapter(adapter);

            if (sortByPopularity == true) {
                adapter.setData(popularityResults);
                adapter.notifyDataSetChanged();
            }

            if (sortByRating == true) {
                adapter.setData(topRatedResults);
                adapter.notifyDataSetChanged();
            }

        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void checkNetworkAndGetData() {
        /*
        * Check network connection
        * */
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getMoviesByPopularityAndTopRating();

            mPopularityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortByPopularity = true;
                    sortByRating = false;

                    if (popularityResults != null) {
                        //Collections.sort(popularityResults, new Result.PopularityComparator());
                        adapter.setData(popularityResults);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            mRatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortByRating = true;
                    sortByPopularity = false;

                    if (topRatedResults != null) {
                        //Collections.sort(topRatedResults, new Result.RatingComparator());
                        adapter.setData(topRatedResults);
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        else {
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    public void getMoviesByPopularityAndTopRating() {

        Bundle queryBundle = new Bundle();
        queryBundle.putString("apiKey", mApiKey);

        getSupportLoaderManager().restartLoader(1, queryBundle, this);
        getSupportLoaderManager().restartLoader(2, queryBundle, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        progressBar.setVisibility(View.VISIBLE);

        switch (id) {
            case loader1:
                return new DataLoader(this, args.getString("apiKey"));
            case loader2:
                return new DataLoader2(this, args.getString("apiKey"));
        }

        return null;
    }

    /*
    * Get parsed movie list results from JsonUtils
    * Load images if the list is not null
    * */
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {

        progressBar.setVisibility(View.GONE);

        try {

            if(loader.getId() == 1) {
                popularityResults = JsonUtils.parseResults(data);
            }

            if(loader.getId() == 2 ) {
                topRatedResults = JsonUtils.parseResults(data);
            }

            if(sortByRating == true) {
                adapter.setData(topRatedResults);
                adapter.notifyDataSetChanged();
            }
            else {
                adapter.setData(popularityResults);
                adapter.notifyDataSetChanged();
            }

        } catch (Exception e) {
            e.printStackTrace();
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    /*
    * Send movie info to the Detail Activity
    * */
    @Override
    public void onPosterClick(String movieID) {
        Intent intent = new Intent(getBaseContext(), DetailActivity.class);

        for(Result movieResult : popularityResults)
        {
            if(movieResult.getId().equals(movieID)) {

                intent.putExtra("Movies", movieResult);
                startActivity(intent);
            }
        }

        for(Result movieResult : topRatedResults)
        {
            if(movieResult.getId().equals(movieID)) {

                intent.putExtra("Movies", movieResult);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkNetworkAndGetData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("moviesPopular", popularityResults);
        outState.putParcelableArrayList("moviesTopRated", topRatedResults);
        outState.putBoolean("sortByPopularity",sortByPopularity);
        outState.putBoolean("sortByRating",sortByRating);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        popularityResults = savedInstanceState.getParcelableArrayList("moviesPopular");
        topRatedResults = savedInstanceState.getParcelableArrayList("moviesTopRated");
        sortByPopularity = savedInstanceState.getBoolean("sortByPopularity");
        sortByRating = savedInstanceState.getBoolean("sortByRating");
    }
}
