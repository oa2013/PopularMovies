package agafonova.com.popularmovies.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import java.util.List;

@Dao
public interface FavoriteDao {

    @Insert
    void insert(FavoriteItem item);

    @Query("DELETE FROM MASTER_TABLE WHERE id=:mId")
    void deleteById(int mId);

    @Query("SELECT * FROM MASTER_TABLE")
    LiveData<List<FavoriteItem>> getAllFavorites();

    @Query("DELETE FROM MASTER_TABLE")
    void deleteAllFavorites();

    @Query("SELECT EXISTS(SELECT * FROM MASTER_TABLE WHERE favorite=:mId)")
    LiveData<Integer> isFavoriteMovie(int mId);
}
