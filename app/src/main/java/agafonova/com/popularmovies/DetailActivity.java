package agafonova.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Property;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import agafonova.com.popularmovies.model.Result;

public class DetailActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    private TextView mTitleView;
    private TextView mReleaseDateView;
    private TextView mVoteAverageView;
    private TextView mPlotSynopsisView;
    private ImageView mPosterView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mTitleView = findViewById(R.id.title_text);
        mReleaseDateView = findViewById(R.id.release_date_text);
        mVoteAverageView = findViewById(R.id.vote_average_text);
        mPlotSynopsisView = findViewById(R.id.plot_synopsis_text);
        mPosterView = findViewById(R.id.poster_detail);

        Intent intent = getIntent();

        try {
            Result result = intent.getParcelableExtra("Movies");

            mTitleView.setText("Title: " + result.getTitle());
            mReleaseDateView.setText("Release date: " + result.getReleaseDate());
            mVoteAverageView.setText("Popularity: " + result.getVoteAverage());
            mPlotSynopsisView.setText(result.getOverview());

            String imagePath = IMAGE_URL + result.getPosterPath();

            Picasso.with(getApplicationContext()).load(imagePath).into(mPosterView);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
