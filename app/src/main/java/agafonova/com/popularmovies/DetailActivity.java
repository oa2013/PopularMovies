package agafonova.com.popularmovies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import agafonova.com.popularmovies.adapters.MovieReviewAdapter;
import agafonova.com.popularmovies.adapters.TrailerAdapter;
import agafonova.com.popularmovies.db.FavoriteItem;
import agafonova.com.popularmovies.model.Result;
import agafonova.com.popularmovies.model.ReviewResult;
import agafonova.com.popularmovies.model.TrailerResult;
import agafonova.com.popularmovies.util.JsonUtils;
import agafonova.com.popularmovies.util.NetworkUtils;
import agafonova.com.popularmovies.util.ReviewLoader;
import agafonova.com.popularmovies.util.TrailerLoader;
import agafonova.com.popularmovies.viewmodel.FavoriteViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * @author Olga Agafonova
 * @date June 1, 2018
 * Android Nanodegree Movie Poster Project
 * */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, TrailerAdapter.ResourceClickListener, MovieReviewAdapter.ResourceClickListener {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    @BindView(R.id.title_text)
    TextView mTitleView;

    @BindView(R.id.release_date_text)
    TextView mReleaseDateView;

    @BindView(R.id.vote_average_text)
    TextView mVoteAverageView;

    @BindView(R.id.plot_synopsis_text)
    TextView mPlotSynopsisView;

    @BindView(R.id.poster_detail)
    ImageView mPosterView;

    @BindView(R.id.progressBarDetail)
    ProgressBar mProgressBar;

    @BindView(R.id.error_message_detail)
    TextView mErrorTextView;

    @BindView(R.id.favoriteButton)
    ImageView mFavoriteButton;

    @BindView(R.id.rv_trailers)
    RecyclerView mRecyclerView;

    @BindView(R.id.rv_moviereviews)
    RecyclerView mReviewRecyclerView;

    private TrailerAdapter mAdapter;
    private String mApiKey;
    private String mMovieID;
    private ArrayList<TrailerResult> mTrailerResults = null;
    private Result mResult;
    private FavoriteViewModel mFavoriteItemViewModel;
    private ArrayList<FavoriteItem> mFavoriteItemList = null;

    private MovieReviewAdapter mReviewAdapter;
    private ArrayList<ReviewResult> mReviewResults = null;

    private static final int mLoader1 = 1;
    private static final int mLoader2 = 2;

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        //Enable the "Up" button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        try {
            Intent intent = getIntent();
            mResult = intent.getParcelableExtra("Movies");
            mTitleView.setText(mResult.getTitle());

            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
            Date releaseDate = format.parse(mResult.getReleaseDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(releaseDate);
            int year = calendar.get(Calendar.YEAR);

            mReleaseDateView.setText(Integer.toString(year));
            mVoteAverageView.setText(mResult.getVoteAverage() + "/10");
            mPlotSynopsisView.setText(mResult.getOverview());

            String imagePath = IMAGE_URL + mResult.getPosterPath();
            Picasso.with(getApplicationContext()).load(imagePath).into(mPosterView);

            /**
             * Phase 2 code starts here
             */
            GridLayoutManager layoutManager = new GridLayoutManager(DetailActivity.this, 1, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            GridLayoutManager layoutManager2 = new GridLayoutManager(DetailActivity.this, 1, GridLayoutManager.VERTICAL, false);
            mReviewRecyclerView.setLayoutManager(layoutManager2);
            mReviewRecyclerView.setHasFixedSize(true);

            if (getSupportLoaderManager().getLoader(mLoader1) != null) {
                getSupportLoaderManager().initLoader(mLoader1, null, this);
            }

            if (getSupportLoaderManager().getLoader(mLoader2) != null) {
                getSupportLoaderManager().initLoader(mLoader2, null, this);
            }

            mErrorTextView.setVisibility(View.INVISIBLE);
            mMovieID = mResult.getId();

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

            getMovieTrailers();
            getReviews();

            mAdapter = new TrailerAdapter(this);
            mRecyclerView.setAdapter(mAdapter);

            mReviewAdapter = new MovieReviewAdapter(this);
            mReviewRecyclerView.setAdapter(mReviewAdapter);

            mFavoriteItemViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

            mFavoriteItemViewModel.getAllFavorites().observe(this, new Observer<List<FavoriteItem>>() {
                @Override
                public void onChanged(@Nullable final List<FavoriteItem> items) {

                    mFavoriteItemList = new ArrayList<FavoriteItem>(items);
                }
            });

            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    Result currentResult = intent.getParcelableExtra("Movies");
                    String movieName = currentResult.getTitle();

                    mFavoriteItemViewModel.getAllFavorites().observe(DetailActivity.this, new Observer<List<FavoriteItem>>() {
                        @Override
                        public void onChanged(@Nullable final List<FavoriteItem> favoritesList) {

                            if(favoritesList != null) {
                                //populate the static list with items from LiveData list
                                for(FavoriteItem item : favoritesList) {
                                    mFavoriteItemList.add(item);
                                }
                            }
                        }
                    });

                    FavoriteItem existingItem = null;

                    for(FavoriteItem item : mFavoriteItemList) {
                        if(item.getFavorite().contains(movieName)) {
                            existingItem = item;
                        }
                    }

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("wasFavoriteButtonPressed","Yes");
                    editor.apply();

                    if(existingItem != null) {
                        //If this movie is already in the favorites table when we click on the button,
                        //then we delete it
                        mFavoriteItemViewModel.deleteById(existingItem.getId());
                        ImageView favoriteButtonView = (ImageView)v.findViewById(R.id.favoriteButton);
                        favoriteButtonView.setImageResource(R.drawable.ic_brokenheart);

                        //Toast.makeText(getApplicationContext(), "Deleted from favorites", Toast.LENGTH_SHORT).show();
                        //Log.d(LOG_TAG, "favorite id deleted: " + existingItem.getId());

                    }
                    //else, we add it to the table
                    else {
                        FavoriteItem newItem = new FavoriteItem();
                        newItem.setFavorite(movieName);
                        mFavoriteItemViewModel.insert(newItem);

                        //Toast.makeText(getApplicationContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                        //Log.d(LOG_TAG, "favorite added: " + newItem.getFavorite());
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getMovieTrailers() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("apiKey", mApiKey);
        queryBundle.putString("movieID", mMovieID);
        getSupportLoaderManager().restartLoader(1, queryBundle, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        mProgressBar.setVisibility(View.VISIBLE);

        switch(id) {
            case mLoader1:
                return new TrailerLoader(this, args.getString("apiKey"), args.getString("movieID"));
            case mLoader2:
                return new ReviewLoader(this, args.getString("apiKey"), args.getString("movieID"));

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        mProgressBar.setVisibility(View.GONE);

        try {

            if(loader.getId() == 1) {
                mTrailerResults = JsonUtils.parseTrailers(data);

                if(mTrailerResults.size()>0) {
                    mAdapter.setData(mTrailerResults);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getApplicationContext(), "No trailers available", Toast.LENGTH_SHORT).show();
                }
            }

            if(loader.getId() == 2) {
                mReviewResults = JsonUtils.parseReviews(data);

                if(mReviewResults.size()>0) {
                    mReviewAdapter.setData(mReviewResults);
                    mReviewAdapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(getApplicationContext(), "No reviews available", Toast.LENGTH_SHORT).show();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            mErrorTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    @Override
    public void onTrailerClick(String trailerID) {

        if(mTrailerResults != null) {
            for(int i=0; i<mTrailerResults.size(); i++) {
                if (mTrailerResults.get(i).getId().equals(trailerID)) {
                    String address = YOUTUBE_URL + mTrailerResults.get(i).getKey();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(address)));
                }
            }
        }
    }

    @Override
    public void onReviewClick(String reviewID) {
    }

    public void getReviews() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("apiKey", mApiKey);
        queryBundle.putString("movieID", mMovieID);
        getSupportLoaderManager().restartLoader(mLoader2, queryBundle, this);
    }
}