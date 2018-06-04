package agafonova.com.popularmovies.db;

import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteItem  implements Parcelable {

    private int mId;
    private String mFavorite;

    public FavoriteItem() {

    }

    private FavoriteItem(Parcel in) {
        mId = in.readInt();
        mFavorite = in.readString();
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mFavorite);
    }

    static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>() {
        @Override
        public FavoriteItem createFromParcel(Parcel in) {
            return new FavoriteItem(in);
        }

        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };
}
