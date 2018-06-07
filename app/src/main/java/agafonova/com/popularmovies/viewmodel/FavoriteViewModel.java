package agafonova.com.popularmovies.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import java.util.List;
import agafonova.com.popularmovies.db.FavoriteItem;
import agafonova.com.popularmovies.db.FavoritesRepository;

public class FavoriteViewModel extends AndroidViewModel {

    private FavoritesRepository mRepository;
    private LiveData<List<FavoriteItem>> mAllFavorites;

    public FavoriteViewModel(Application application) {
        super(application);
        mRepository = new FavoritesRepository(application);
        mAllFavorites = mRepository.getAllFavorites();
    }

    public LiveData<List<FavoriteItem>> getAllFavorites() {
        return mAllFavorites;
    }

    public void insert(FavoriteItem item) {
        mRepository.insert(item);
    }

    public void deleteById(int id) {
        mRepository.deleteById(id);
    }

    public void deleteAllFavorites() {
        mRepository.deleteAllFavorites();
    }
}
