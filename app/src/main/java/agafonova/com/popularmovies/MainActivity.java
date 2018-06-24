package agafonova.com.popularmovies;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import agafonova.com.popularmovies.adapters.ResultAdapter;
import agafonova.com.popularmovies.db.FavoriteItem;
import agafonova.com.popularmovies.model.Result;
import agafonova.com.popularmovies.util.DataLoader;
import agafonova.com.popularmovies.util.DataLoader2;
import agafonova.com.popularmovies.util.JsonUtils;
import agafonova.com.popularmovies.util.NetworkUtils;
import agafonova.com.popularmovies.viewmodel.FavoriteViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
* @author Olga Agafonova
* @date June 24, 2018
* Android Nanodegree Movie Poster Project
* */

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>,
        ResultAdapter.ResourceClickListener, AdapterView.OnItemSelectedListener {

    private String mApiKey;
    private ArrayList<Result> mPopularityResults = null;
    private ArrayList<Result> mTopRatedResults = null;
    private ResultAdapter mAdapter;

    @BindView(R.id.rv_posters)
    RecyclerView mRecyclerView;

    @BindView(R.id.spinner)
    Spinner mSpinner;

    @BindView(R.id.error_message)
    TextView mErrorTextView;

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    private boolean mSortByPopularity;
    private boolean mSortByRating;
    private boolean mSortByFavorites;

    private static final int mLoader1 = 1;
    private static final int mLoader2 = 2;

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private ArrayList<FavoriteItem> mFavoriteItemArrayList = null;
    private ArrayList<Result> mAllMovies = null;
    private FavoriteViewModel mFavoriteItemViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mErrorTextView.setVisibility(View.INVISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, numberOfColumns(), GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        if (getSupportLoaderManager().getLoader(mLoader1) != null) {
            getSupportLoaderManager().initLoader(mLoader1, null, this);
        }

        if (getSupportLoaderManager().getLoader(mLoader2) != null) {
            getSupportLoaderManager().initLoader(mLoader2, null, this);
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

        List<String> list = new ArrayList<String>();
        list.add("Sort by popularity");
        list.add("Sort by rating");
        list.add("Sort by favorites");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(dataAdapter);
        mSpinner.setOnItemSelectedListener(this);

        mFavoriteItemViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        MutableLiveData<List<FavoriteItem>> mutableFavoriteItemList = new MutableLiveData<List<FavoriteItem>>();
        mFavoriteItemArrayList = new ArrayList<FavoriteItem>();

        mAdapter = new ResultAdapter(this, mutableFavoriteItemList);

        mFavoriteItemViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteItem>>() {
            @Override
            public void onChanged(@Nullable final List<FavoriteItem> items) {

                mAdapter.mFavoritesList.setValue(items);

                for(FavoriteItem item : items)
                {
                    mFavoriteItemArrayList.add(item);
                }

                mRecyclerView.setAdapter(mAdapter);
            }
        });

        if(savedInstanceState == null) {

            mSortByRating = false;
            mSortByPopularity = false;
            mSortByFavorites = false;

            checkNetworkAndGetData();
        }
        else if (savedInstanceState != null){

            mSortByPopularity = savedInstanceState.getBoolean("sortByPopularity");
            mSortByRating = savedInstanceState.getBoolean("sortByRating");
            mSortByFavorites = savedInstanceState.getBoolean("sortByFavorites");

            mPopularityResults = savedInstanceState.getParcelableArrayList("moviesPopular");
            mTopRatedResults = savedInstanceState.getParcelableArrayList("moviesTopRated");
            mFavoriteItemArrayList = savedInstanceState.getParcelableArrayList("moviesFavorite");

            mRecyclerView.setAdapter(mAdapter);

            if (mSortByPopularity) {
                mAdapter.setData(mPopularityResults);
                mAdapter.notifyDataSetChanged();
            }

            if (mSortByRating) {
                mAdapter.setData(mTopRatedResults);
                mAdapter.notifyDataSetChanged();
            }

            if(mSortByFavorites) {
                ArrayList<Result> sortedMovies = getFavoriteMovies();
                mAdapter.setData(sortedMovies);
                mAdapter.notifyDataSetChanged();
            }

        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int position, long id) {

        switch (position) {
            case 0:
                mSortByPopularity = true;
                mSortByRating = false;
                mSortByFavorites = false;

                if (mPopularityResults != null) {
                    mAdapter.setData(mPopularityResults);
                    mAdapter.notifyDataSetChanged();
                }
                break;

            case 1:
                mSortByRating = true;
                mSortByPopularity = false;
                mSortByFavorites = false;

                if (mTopRatedResults != null) {
                    mAdapter.setData(mTopRatedResults);
                    mAdapter.notifyDataSetChanged();
                }
                break;

            case 2:
                mSortByFavorites = true;
                mSortByRating = false;
                mSortByPopularity = false;

                ArrayList<Result> sortedMovies = getFavoriteMovies();
                mAdapter.setData(sortedMovies);
                mAdapter.notifyDataSetChanged();
                break;

            default:
                mSortByPopularity = true;
                mSortByRating = false;
                mSortByFavorites = false;

                if (mPopularityResults != null) {
                    mAdapter.setData(mPopularityResults);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
            getMoviesByAllCategories();
        }
        else {
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    //Match movies in favoriteItemsList with movies in the adapter
    public ArrayList<Result> getFavoriteMovies() {

        mAllMovies = new ArrayList<Result>();
        ArrayList<Result> onlyFavoriteMovies = new ArrayList<Result>();
        mAllMovies.addAll(mPopularityResults);
        mAllMovies.addAll(mTopRatedResults);

        if(mFavoriteItemArrayList!= null) {

            for(int i=0; i<mAllMovies.size(); i++) {

                for(int k=0; k<mFavoriteItemArrayList.size(); k++) {

                    //if the titles match, we found the right movies in favoriteMovies
                    if(mAllMovies.get(i).getTitle().contains(mFavoriteItemArrayList.get(k).getFavorite())) {
                        mAllMovies.get(i).setIsFavorite(1);
                    }
                }
            }
        }

        for(int i=0; i<mAllMovies.size(); i++) {

            if(mAllMovies.get(i).getIsFavorite() == 1) {
                onlyFavoriteMovies.add(mAllMovies.get(i));
            }

         }

        Set<Result> noDuplicates = new LinkedHashSet<Result>(onlyFavoriteMovies);
        ArrayList<Result> noDuplicatesList = new ArrayList<Result>();
        Iterator iterator = noDuplicates.iterator();

        while(iterator.hasNext()) {
            noDuplicatesList.add((Result)iterator.next());
        }

        return noDuplicatesList;
    }

    public void getMoviesByAllCategories() {

        Bundle queryBundle = new Bundle();
        queryBundle.putString("apiKey", mApiKey);

        getSupportLoaderManager().restartLoader(1, queryBundle, this);
        getSupportLoaderManager().restartLoader(2, queryBundle, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {

        mProgressBar.setVisibility(View.VISIBLE);

        switch (id) {
            case mLoader1:
                return new DataLoader(this, args.getString("apiKey"));
            case mLoader2:
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

        mProgressBar.setVisibility(View.GONE);

        try {

            if(loader.getId() == 1) {
                mPopularityResults = JsonUtils.parseResults(data);
            }

            if(loader.getId() == 2 ) {
                mTopRatedResults = JsonUtils.parseResults(data);
            }

            if(mSortByRating) {
                mAdapter.setData(mTopRatedResults);
                mAdapter.notifyDataSetChanged();
            }
            else {
                mAdapter.setData(mPopularityResults);
                mAdapter.notifyDataSetChanged();
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

        for(Result movieResult : mPopularityResults)
        {
            if(movieResult.getId().equals(movieID)) {

                intent.putExtra("Movies", movieResult);
                startActivity(intent);
            }
        }

        for(Result movieResult : mTopRatedResults)
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
        outState.putParcelableArrayList("moviesPopular", mPopularityResults);
        outState.putParcelableArrayList("moviesTopRated", mTopRatedResults);
        outState.putParcelableArrayList("moviesFavorite", mFavoriteItemArrayList);
        outState.putBoolean("sortByPopularity",mSortByPopularity);
        outState.putBoolean("sortByRating",mSortByRating);
        outState.putBoolean("sortByFavorites",mSortByFavorites);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPopularityResults = savedInstanceState.getParcelableArrayList("moviesPopular");
        mTopRatedResults = savedInstanceState.getParcelableArrayList("moviesTopRated");
        mFavoriteItemArrayList = savedInstanceState.getParcelableArrayList("moviesFavorite");
        mSortByPopularity = savedInstanceState.getBoolean("sortByPopularity");
        mSortByRating = savedInstanceState.getBoolean("sortByRating");
        mSortByFavorites = savedInstanceState.getBoolean("sortByFavorites");
    }

}
