package agafonova.com.popularmovies.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class ReviewLoader extends AsyncTaskLoader<String>  {

    private String mApiKey;
    private String mMovieID;

    public ReviewLoader(Context context,String apiKey, String movieID) {
        super(context);
        mApiKey = apiKey;
        mMovieID = movieID;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getMovieReviews(mApiKey, mMovieID);
    }
}
