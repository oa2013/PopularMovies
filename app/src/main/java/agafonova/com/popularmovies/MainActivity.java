package agafonova.com.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import agafonova.com.popularmovies.adapters.ResultAdapter;
import agafonova.com.popularmovies.model.Result;
import agafonova.com.popularmovies.util.DataLoader;
import agafonova.com.popularmovies.util.JsonUtils;
import agafonova.com.popularmovies.util.NetworkUtils;

/*
* @author Olga Agafonova
* @date April 26, 2018
* Android Nanodegree Movie Poster Project (stage 1)
* */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, ResultAdapter.ResourceClickListener{

    private String mPageParam;
    private String mVideoParam;
    private String mAdultParam;
    private String mSortByParam;
    private String mLanguageParam;
    private String mApiKey;

    private List<Result> results = null;
    private ListView posterView;
    private ResultAdapter adapter;
    private RecyclerView mRecyclerView;

    private Button mPopularityButton;
    private Button mRatingButton;
    private TextView mErrorTextView;
    private boolean sortByPopularity = false;
    private boolean sortByRating = false;

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);
        mPopularityButton = (Button) findViewById(R.id.sort_button_popularity);
        mRatingButton = (Button) findViewById(R.id.sort_button_rating);
        mErrorTextView = (TextView) findViewById(R.id.error_message);

        mErrorTextView.setVisibility(View.INVISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        /*
        * To Reviewers: you need to create an xml file in res/values
        * and use your own MovieDB API key
        * <resources>
        * <string name="api_key">123456789</string>
        * </resources>
        * */
        try{
            mApiKey = getResources().getString(R.string.api_key);
        }
        catch(Exception e) {
            e.printStackTrace();
            mApiKey = "";
        }

        checkNetworkAndGetData();

    }

    private void checkNetworkAndGetData() {
        /*
        * Check network connection
        * */
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            getMoviesByPopularity();

            mPopularityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortByPopularity = true;

                    if (results != null) {
                        Collections.sort(results, new Result.PopularityComparator());
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            mRatingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortByRating = true;

                    if (results != null) {
                        Collections.sort(results, new Result.RatingComparator());
                        adapter.notifyDataSetChanged();
                    }
                }
            });
        }
        else {
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    public void getMoviesByPopularity() {

        mPageParam = "1";
        mVideoParam = "false";
        mAdultParam = "false";
        mSortByParam = "popularity.desc";
        mLanguageParam = "en-US";

        Bundle queryBundle = new Bundle();
        queryBundle.putString("page", mPageParam);
        queryBundle.putString("video", mVideoParam);
        queryBundle.putString("adult", mAdultParam);
        queryBundle.putString("sort", mSortByParam);
        queryBundle.putString("language", mLanguageParam);
        queryBundle.putString("apiKey", mApiKey);

        getSupportLoaderManager().restartLoader(0, queryBundle, this);

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new DataLoader(this, args.getString("page"), args.getString("video"), args.getString("adult"),
                args.getString("sort"), args.getString("language"), args.getString("apiKey"));
    }

    /*
    * Get parsed movie list results from JsonUtils
    * Load images if the list is not null
    * */
    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        try {
            results = JsonUtils.parseResults(data);

            adapter = new ResultAdapter(this);
            adapter.setData(results);
            mRecyclerView.setAdapter(adapter);

        }
        catch(Exception e){
            e.printStackTrace();
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    @Override
    public void onPosterClick(String data) {

    }

    @Override
    public void onResume() {
        super.onResume();

        checkNetworkAndGetData();
    }


}
