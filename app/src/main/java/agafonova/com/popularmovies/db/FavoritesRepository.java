package agafonova.com.popularmovies.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import java.util.List;

public class FavoritesRepository  {

    private FavoriteDao mFavoriteDao;
    private LiveData<List<FavoriteItem>> mAllFavorites;

    public interface AsyncResponse {
        void getFavorites(List<FavoriteItem> items);
    }

    public FavoritesRepository(Application application) {
        FavoritesRoomDB db = FavoritesRoomDB.getDatabase(application);
        mFavoriteDao = db.favoriteDao();
        mAllFavorites = mFavoriteDao.getAllFavorites();
    }

    public LiveData<List<FavoriteItem>> getAllFavorites() {
        return mAllFavorites;
    }

    public LiveData<Integer> isFavoriteMovie(int id) {
        return mFavoriteDao.isFavoriteMovie(id);
    }

    public void insert(FavoriteItem item) {
        new insertAsyncTask(mFavoriteDao).execute(item);
    }

    public void deleteById(int id) {
        new deleteAsyncTask(mFavoriteDao).execute(id);
    }

    public void deleteAllFavorites() {
        new deleteAllAsyncTask(mFavoriteDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<FavoriteItem, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        insertAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavoriteItem... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Integer, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        deleteAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteById(params[0]);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        deleteAllAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... params) {
            mAsyncTaskDao.deleteAllFavorites();
            return null;
        }
    }
}
