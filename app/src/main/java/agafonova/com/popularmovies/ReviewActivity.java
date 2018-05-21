package agafonova.com.popularmovies;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import agafonova.com.popularmovies.adapters.MovieReviewAdapter;
import agafonova.com.popularmovies.model.ReviewResult;
import agafonova.com.popularmovies.util.JsonUtils;
import agafonova.com.popularmovies.util.ReviewLoader;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>, MovieReviewAdapter.ResourceClickListener {

    private static final int loader1 = 1;

    @BindView(R.id.rv_reviews)
    RecyclerView mRecyclerView;

    @BindView(R.id.error_message_reviews)
    TextView mErrorTextView;

    @BindView(R.id.progressBarReview)
    ProgressBar progressBar;

    private MovieReviewAdapter adapter;
    private String mApiKey;
    private String mMovieID;
    private ArrayList<ReviewResult> reviewResults = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        mApiKey = intent.getStringExtra("ApiKey");
        mMovieID = intent.getStringExtra("MovieID");
        mErrorTextView.setVisibility(View.INVISIBLE);

        GridLayoutManager layoutManager = new GridLayoutManager(ReviewActivity.this, 1, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(true);

        if (getSupportLoaderManager().getLoader(1) != null) {
            getSupportLoaderManager().initLoader(1, null, this);
        }

        getReviews();

        adapter = new MovieReviewAdapter(this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onReviewClick(String reviewID) {
    }

    public void getReviews() {
        Bundle queryBundle = new Bundle();
        queryBundle.putString("apiKey", mApiKey);
        queryBundle.putString("movieID", mMovieID);
        getSupportLoaderManager().restartLoader(1, queryBundle, this);
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        progressBar.setVisibility(View.VISIBLE);
        return new ReviewLoader(this, args.getString("apiKey"), args.getString("movieID"));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        progressBar.setVisibility(View.GONE);

        try {
            reviewResults = JsonUtils.parseReviews(data);
            adapter.setData(reviewResults);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            e.printStackTrace();
            mErrorTextView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<String> loader) {}

    @Override
    public void onResume() {
        super.onResume();
        getReviews();
    }

}
