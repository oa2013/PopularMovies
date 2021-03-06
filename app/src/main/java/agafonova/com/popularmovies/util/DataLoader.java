package agafonova.com.popularmovies.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class DataLoader extends AsyncTaskLoader<String> {

    private String mApiKey;

    public DataLoader(Context context,String apiKey) {
        super(context);
        mApiKey = apiKey;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getPopularMovies(mApiKey);
    }
}
