package agafonova.com.popularmovies;

import android.app.Activity;
import android.arch.lifecycle.LiveData;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import agafonova.com.popularmovies.adapters.ResultAdapter;
import agafonova.com.popularmovies.db.FavoriteItem;
import agafonova.com.popularmovies.db.FavoritesDBHelper;
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
* @date June 6, 2018
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

    @BindView(R.id.sort_button_favorites)
    Button mFavoritesButton;

    @BindView(R.id.error_message)
    TextView mErrorTextView;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private boolean sortByPopularity;
    private boolean sortByRating;
    private boolean sortByFavorites;

    private static final int loader1 = 1;
    private static final int loader2 = 2;

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    private ArrayList<FavoriteItem> favoriteItemArrayList = null;
    private ArrayList<Result> allMovies = null;
    private FavoriteViewModel mFavoriteItemViewModel;

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

        mFavoriteItemViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        MutableLiveData<List<FavoriteItem>> mutableFavoriteItemList = new MutableLiveData<List<FavoriteItem>>();
        favoriteItemArrayList = new ArrayList<FavoriteItem>();

        adapter = new ResultAdapter(this, mutableFavoriteItemList);

        mFavoriteItemViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteItem>>() {
            @Override
            public void onChanged(@Nullable final List<FavoriteItem> items) {

                adapter.mFavoritesList.setValue(items);

                for(FavoriteItem item : items)
                {
                    favoriteItemArrayList.add(item);
                }

                mRecyclerView.setAdapter(adapter);
            }
        });

        if(savedInstanceState == null) {

            sortByRating = false;
            sortByPopularity = false;
            sortByFavorites = false;

            checkNetworkAndGetData();
        }
        else if (savedInstanceState != null){

            sortByPopularity = savedInstanceState.getBoolean("sortByPopularity");
            sortByRating = savedInstanceState.getBoolean("sortByRating");
            sortByFavorites = savedInstanceState.getBoolean("sortByFavorites");

            popularityResults = savedInstanceState.getParcelableArrayList("moviesPopular");
            topRatedResults = savedInstanceState.getParcelableArrayList("moviesTopRated");
            favoriteItemArrayList = savedInstanceState.getParcelableArrayList("moviesFavorite");

            mRecyclerView.setAdapter(adapter);

            if (sortByPopularity) {
                adapter.setData(popularityResults);
                adapter.notifyDataSetChanged();
            }

            if (sortByRating) {
                adapter.setData(topRatedResults);
                adapter.notifyDataSetChanged();
            }

            if(sortByFavorites) {
                ArrayList<Result> sortedMovies = getFavoriteMovies();
                adapter.setData(sortedMovies);
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

            getMoviesByAllCategories();

            mPopularityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortByPopularity = true;
                    sortByRating = false;
                    sortByFavorites = false;

                    if (popularityResults != null) {
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
                    sortByFavorites = false;

                    if (topRatedResults != null) {
                        adapter.setData(topRatedResults);
                        adapter.notifyDataSetChanged();
                    }
                }
            });

            mFavoritesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sortByFavorites = true;
                    sortByRating = false;
                    sortByPopularity = false;

                    ArrayList<Result> sortedMovies = getFavoriteMovies();
                    adapter.setData(sortedMovies);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        else {
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    //Match movies in favoriteItemsList with movies in the adapter
    public ArrayList<Result> getFavoriteMovies() {

        allMovies = new ArrayList<Result>();
        allMovies.addAll(popularityResults);
        allMovies.addAll(topRatedResults);

        if(favoriteItemArrayList!= null) {

            for(int i=0; i<allMovies.size(); i++) {

                for(int k=0; k<favoriteItemArrayList.size(); k++) {

                    //if the titles match, we found the right movies in favoriteMovies
                    if(allMovies.get(i).getTitle().contains(favoriteItemArrayList.get(k).getFavorite())) {
                        allMovies.get(i).setIsFavorite(1);
                    }
                }
            }
        }

        Set<Result> noDuplicates = new LinkedHashSet<Result>(allMovies);
        ArrayList<Result> noDuplicatesList = new ArrayList<Result>();
        Iterator iterator = noDuplicates.iterator();

        while(iterator.hasNext()) {
            noDuplicatesList.add((Result)iterator.next());
        }

        Comparator<Result> myComparator = new Comparator<Result>() {
            @Override
            public int compare(Result a, Result b) {

                if(b.getIsFavorite() > a.getIsFavorite()) {
                    return 1;
                }
                else if (b.getIsFavorite() < a.getIsFavorite()) {
                    return -1;
                }
                else  {
                    return 0;
                }
            }
        };

        //Requires minSdkVersion 24
        Collections.sort(noDuplicatesList, myComparator);

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

            if(sortByRating) {
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
        outState.putParcelableArrayList("moviesFavorite", favoriteItemArrayList);
        outState.putBoolean("sortByPopularity",sortByPopularity);
        outState.putBoolean("sortByRating",sortByRating);
        outState.putBoolean("sortByFavorites",sortByFavorites);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        popularityResults = savedInstanceState.getParcelableArrayList("moviesPopular");
        topRatedResults = savedInstanceState.getParcelableArrayList("moviesTopRated");
        favoriteItemArrayList = savedInstanceState.getParcelableArrayList("moviesFavorite");
        sortByPopularity = savedInstanceState.getBoolean("sortByPopularity");
        sortByRating = savedInstanceState.getBoolean("sortByRating");
        sortByFavorites = savedInstanceState.getBoolean("sortByFavorites");
    }

}
