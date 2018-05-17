package agafonova.com.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import agafonova.com.popularmovies.model.Result;
import agafonova.com.popularmovies.model.ReviewResult;
import agafonova.com.popularmovies.model.TrailerResult;
import agafonova.com.popularmovies.util.JsonUtils;
import agafonova.com.popularmovies.util.ReviewLoader;
import agafonova.com.popularmovies.util.TrailerLoader;
import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * @author Olga Agafonova
 * @date May 17, 2018
 * Android Nanodegree Movie Poster Project
 * */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {

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
    ProgressBar progressBar;

    @BindView(R.id.movieVideoView)
    VideoView mVideoView;

    @BindView(R.id.error_message_detail)
    TextView mErrorTextView;

    private static final int loader1 = 1;
    private static final int loader2 = 2;
    private String mApiKey;
    private String mMovieID;

    private ArrayList<TrailerResult> trailerResults = null;
    private ArrayList<ReviewResult> reviewResults = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            Result result = intent.getParcelableExtra("Movies");

            mTitleView.setText(result.getTitle());
            mReleaseDateView.setText(result.getReleaseDate());
            mVoteAverageView.setText(result.getVoteAverage());
            mPlotSynopsisView.setText(result.getOverview());

            String imagePath = IMAGE_URL + result.getPosterPath();
            Picasso.with(getApplicationContext()).load(imagePath).into(mPosterView);

            /**
             * Phase 2 code starts here
             */
            if (getSupportLoaderManager().getLoader(1) != null) {
                getSupportLoaderManager().initLoader(1, null, this);
            }

            if (getSupportLoaderManager().getLoader(2) != null) {
                getSupportLoaderManager().initLoader(2, null, this);
            }

            mErrorTextView.setVisibility(View.INVISIBLE);
            mMovieID = result.getId();

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

            getReviewsAndTrailer();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getReviewsAndTrailer() {

        Bundle queryBundle = new Bundle();
        queryBundle.putString("apiKey", mApiKey);
        queryBundle.putString("movieID", mMovieID);

        getSupportLoaderManager().restartLoader(1, queryBundle, this);
        getSupportLoaderManager().restartLoader(2, queryBundle, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);

        switch (id) {
            case loader1:
                return new ReviewLoader(this, args.getString("apiKey"), args.getString("movieID"));
            case loader2:
                return new TrailerLoader(this, args.getString("apiKey"), args.getString("movieID"));
        }

        return null;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        progressBar.setVisibility(View.GONE);

        try {

            if(loader.getId() == 1) {
                reviewResults = JsonUtils.parseReviews(data);
            }

            if(loader.getId() == 2 ) {

                trailerResults = JsonUtils.parseTrailers(data);

                if(trailerResults != null) {

                    String path = YOUTUBE_URL + trailerResults.get(0).getKey();

//                    mVideoView.setVideoPath(path);
//                    MediaController mc = new MediaController(this);
//                    mVideoView.setMediaController(mc);
//                    mVideoView.requestFocus();
//                    mVideoView.start();
//                    mc.show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            mErrorTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}


}