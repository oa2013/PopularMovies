package agafonova.com.popularmovies.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import java.util.List;

public class FavoritesRepository {

    private FavoriteDao mFavoriteDao;
    private LiveData<List<FavoriteItem>> mAllFavorites;

    public FavoritesRepository(Application application) {
        FavoritesRoomDB db = FavoritesRoomDB.getDatabase(application);
        mFavoriteDao = db.favoriteDao();
        mAllFavorites = mFavoriteDao.selectAllFavorites();
    }

    public LiveData<List<FavoriteItem>> getAllFavorites() {
        return mAllFavorites;
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

    public LiveData<List<FavoriteItem>> selectFavorite(String name) {
        return mFavoriteDao.selectFavorite(name);
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
