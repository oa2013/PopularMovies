package agafonova.com.popularmovies.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/*
 * @author Olga Agafonova
 * @date June 6, 2018
 * Android Nanodegree Movie Poster Project
 */

@Database(entities = {FavoriteItem.class}, version = 1)
public abstract class FavoritesRoomDB extends RoomDatabase {

    private static FavoritesRoomDB INSTANCE;

    public abstract FavoriteDao favoriteDao();

    public static FavoritesRoomDB  getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FavoritesRoomDB.class) {
                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FavoritesRoomDB.class, "favorites_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
