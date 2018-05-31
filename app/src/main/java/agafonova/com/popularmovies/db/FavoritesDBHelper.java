package agafonova.com.popularmovies.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * @author Olga Agafonova
 * @date May 30, 2018
 * Android Nanodegree Movie Poster Project
 *
 * Based on Google's code here:
 * https://github.com/google-developer-training/android-fundamentals/
 * */
public class FavoritesDBHelper extends SQLiteOpenHelper {

    private static final String TAG = FavoritesDBHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String MASTER_TABLE = "master";
    private static final String DATABASE_NAME = "favorites";

    public static final String KEY_ID = "id";
    public static final String KEY_FAVORITE = "favorite";
    private static final String[] COLUMNS = { KEY_ID, KEY_FAVORITE };

    private static final String MASTER_TABLE_CREATE =
            "CREATE TABLE " + MASTER_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_FAVORITE + " TEXT );";

    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;

    public FavoritesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d(TAG, "Construct FavoritesOpenHelper");
    }

    @Override
    public String getDatabaseName() {
        return super.getDatabaseName();
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return super.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {
        return super.getReadableDatabase();
    }

    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate beginning.");
        db.execSQL(MASTER_TABLE_CREATE);
        fillDatabaseWithData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(FavoritesDBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data.");
        db.execSQL("DROP TABLE IF EXISTS " + MASTER_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public long count() {
        if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
        Log.d(TAG, "count() called");
        return DatabaseUtils.queryNumEntries(mReadableDB, MASTER_TABLE);
    }

    public void fillDatabaseWithData(SQLiteDatabase db) {

        String[] words = {"Test Movie"};
        ContentValues values = new ContentValues();

        for (int i=0; i < words.length;i++) {
            values.put(KEY_FAVORITE, words[i]);
            db.insert(MASTER_TABLE, null, values);
            Log.d(TAG, "Entering dummy data.");
        }
    }

    //Select one item
    public FavoriteItem query(String name) {

        String query = "SELECT  * FROM " + MASTER_TABLE + " WHERE favorite='" + name + "';";
        Cursor cursor = null;
        FavoriteItem entry = new FavoriteItem();

        try {
            if (mReadableDB == null) {mReadableDB = getReadableDatabase();}
            cursor = mReadableDB.rawQuery(query, null);
            Log.d(TAG, "Cursor count:  " + cursor.getCount());

            if(cursor.getCount() > 0) {
                cursor.moveToFirst();
                entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                entry.setFavorite(cursor.getString(cursor.getColumnIndex(KEY_FAVORITE)));
                Log.d(TAG, "query() called");
            }
            else {
                Log.d(TAG, "cursor returned no entries");
            }

        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return entry;
        }
    }
    //SQL
    //INSERT INTO MASTER_TABLE(KEY_ID, KEY_FAVORTIE)
    //VALUES(BLAH, BLAH)
    public long insert(String item) {
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_FAVORITE, item);

        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            newId = mWritableDB.insert(MASTER_TABLE, null, values);
            Log.d(TAG, "insert() called");
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }

        return newId;
    }

    //SQL
    //UPDATE MASTER_TABLE
    //SET KEY_FAVORITE = BLAH
    //WHERE KEY_ID = BLAH
    public int update(int id, String word) {
        int mNumberOfRowsUpdated = -1;

        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            ContentValues values = new ContentValues();
            values.put(KEY_FAVORITE, word);

            mNumberOfRowsUpdated = mWritableDB.update(MASTER_TABLE,
                    values,
                    KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});

            Log.d(TAG, "update() called");
        } catch (Exception e) {
            Log.d (TAG, "UPDATE EXCEPTION! " + e.getMessage());
        }

        return mNumberOfRowsUpdated;
    }

    public int delete(int id) {
        int deleted = 0;

        try {
            if (mWritableDB == null) {mWritableDB = getWritableDatabase();}
            deleted = mWritableDB.delete(MASTER_TABLE,
                    KEY_ID + " = ? ", new String[]{String.valueOf(id)});
            Log.d(TAG, "delete() called");
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());
        }

        return deleted;
    }
}
