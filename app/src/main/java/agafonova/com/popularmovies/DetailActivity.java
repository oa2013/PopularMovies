package agafonova.com.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import agafonova.com.popularmovies.model.Result;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

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
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}
