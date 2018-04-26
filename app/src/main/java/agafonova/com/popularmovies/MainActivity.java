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
import android.widget.ListView;
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
    private String mSortBy;
    private String mLanguage;
    private String mApiKey;
    private List<Result> results = null;
    private ListView posterView;
    private ResultAdapter adapter;
    private RecyclerView mRecyclerView;

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_posters);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        if(getSupportLoaderManager().getLoader(0)!=null){
            getSupportLoaderManager().initLoader(0,null,this);
        }

        mPageParam = "1";
        mVideoParam = "false";
        mAdultParam = "false";
        mSortBy = "popularity.desc";
        mLanguage = "en-US";

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

        getMovies(mPageParam, mVideoParam, mAdultParam, mSortBy, mLanguage, mApiKey);
    }

    public void getMovies(String page, String video, String adult, String sort, String language, String apiKey) {

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {

            Bundle queryBundle = new Bundle();
            queryBundle.putString("page", page);
            queryBundle.putString("video", video);
            queryBundle.putString("adult", adult);
            queryBundle.putString("sort", sort);
            queryBundle.putString("language", apiKey);
            queryBundle.putString("apiKey", apiKey);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
        else {
            //Display network error in UI
        }
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
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    @Override
    public void onPosterClick(String data) {

    }
}
