package agafonova.com.popularmovies.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class TrailerLoader extends AsyncTaskLoader<String>{

    private String mApiKey;
    private String mMovieID;

    public TrailerLoader(Context context,String apiKey, String movieID) {
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
        return NetworkUtils.getTrailers(mApiKey, mMovieID);
    }
}
