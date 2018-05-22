package agafonova.com.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import agafonova.com.popularmovies.adapters.TrailerAdapter;
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
 * @date May 22, 2018
 * Android Nanodegree Movie Poster Project
 * */
public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, TrailerAdapter.ResourceClickListener {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private static final String YOUTUBE_URL = "https://www.youtube.com/watch?v=";
    private static final int loader1 = 1;

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

    @BindView(R.id.error_message_detail)
    TextView mErrorTextView;

    @BindView(R.id.review_button)
    ImageView mReviewButton;

    @BindView(R.id.favoriteButton)
    ImageView mFavoriteButton;

    @BindView(R.id.rv_trailers)
    RecyclerView mRecyclerView;

    private TrailerAdapter adapter;
    private String mApiKey;
    private String mMovieID;
    private ArrayList<TrailerResult> trailerResults = null;
    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        try {
            result = intent.getParcelableExtra("Movies");

            mTitleView.setText(result.getTitle());

            SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD");
            Date releaseDate = format.parse(result.getReleaseDate());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(releaseDate);
            int year = calendar.get(Calendar.YEAR);

            mReleaseDateView.setText(Integer.toString(year));
            mVoteAverageView.setText(result.getVoteAverage() + "/10");
            mPlotSynopsisView.setText(result.getOverview());

            String imagePath = IMAGE_URL + result.getPosterPath();
            Picasso.with(getApplicationContext()).load(imagePath).into(mPosterView);

            /**
             * Phase 2 code starts here
             */
            GridLayoutManager layoutManager = new GridLayoutManager(DetailActivity.this, 1, GridLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setHasFixedSize(true);

            if (getSupportLoaderManager().getLoader(1) != null) {
                getSupportLoaderManager().initLoader(1, null, this);
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

            getMovieTrailers();

            adapter = new TrailerAdapter(this);
            mRecyclerView.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }

        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ReviewActivity.class);
                intent.putExtra("MovieID", result.getId());
                intent.putExtra("ApiKey", mApiKey);
                startActivity(intent);
            }
        });

        mFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //call to internal db here
            }
        });
    }

    public void getMovieTrailers() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("apiKey", mApiKey);
        queryBundle.putString("movieID", mMovieID);
        getSupportLoaderManager().restartLoader(1, queryBundle, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new TrailerLoader(this, args.getString("apiKey"), args.getString("movieID"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        progressBar.setVisibility(View.GONE);

        try {
            trailerResults = JsonUtils.parseTrailers(data);

            if(trailerResults != null) {
               adapter.setData(trailerResults);
               adapter.notifyDataSetChanged();
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

        if(trailerResults != null) {
            for(int i=0; i<trailerResults.size(); i++) {
                if (trailerResults.get(i).getId().equals(trailerID)) {
                    String address = YOUTUBE_URL + trailerResults.get(i).getKey();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(address)));
                }
            }
        }
    }
}