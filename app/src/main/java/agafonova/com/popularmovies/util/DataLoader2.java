package agafonova.com.popularmovies.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class DataLoader2 extends AsyncTaskLoader<String> {

    private String mApiKey;

    public DataLoader2(Context context,String apiKey) {
        super(context);
        mApiKey = apiKey;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getTopRatedMovies(mApiKey);
    }
}
