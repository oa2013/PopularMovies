package agafonova.com.popularmovies.util;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

public class DataLoader extends AsyncTaskLoader<String> {

    private String mPageParam;
    private String mVideoParam;
    private String mAdultParam;
    private String mSortBy;
    private String mLanguage;
    private String mApiKey;

    public DataLoader(Context context, String page, String video, String adult, String sort, String language, String apiKey) {
        super(context);
        mPageParam = page;
        mVideoParam = video;
        mAdultParam = adult;
        mSortBy = sort;
        mLanguage = language;
        mApiKey = apiKey;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        return NetworkUtils.getMovies(mPageParam, mVideoParam, mAdultParam, mSortBy, mLanguage, mApiKey);
    }
}
