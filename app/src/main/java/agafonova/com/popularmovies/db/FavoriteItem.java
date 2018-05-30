package agafonova.com.popularmovies.db;

public class FavoriteItem {

    private int mId;
    private String mFavorite;

    public FavoriteItem() {}

    public int getId() {
        return this.mId;
    }

    public String getFavorite() {
        return this.mFavorite;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setFavorite(String word) {
        this.mFavorite = word;
    }
}
