package agafonova.com.popularmovies.model;

import java.util.ArrayList;
import android.os.Parcel;
import android.os.Parcelable;

public class DiscoverMovie implements Parcelable {

    private String mPage;
    private String mTotalResults;
    private String mTotalPages;
    private ArrayList<Result> mResults = null;

    public DiscoverMovie() {

    }

    public DiscoverMovie(String iPage, String iTotalResults, String iTotalPages, ArrayList<Result> iResults) {
        this.mPage = iPage;
        this.mTotalResults = iTotalResults;
        this.mTotalPages = iTotalPages;
        this.mResults = iResults;
    }

    private DiscoverMovie(Parcel in) {
        mPage = in.readString();
        mTotalResults =  in.readString();
        mTotalPages = in.readString();
        in.readTypedList(mResults, Result.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPage);
        dest.writeString(mTotalResults);
        dest.writeString(mTotalPages);
        dest.writeTypedList(mResults);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPage() {
        return mPage;
    }

    public void setPage(String page) {
        this.mPage = page;
    }

    public String getTotalResults() {
        return mTotalResults;
    }

    public void setTotalResults(String totalResults) {
        this.mTotalResults = totalResults;
    }

    public String getTotalPages() {
        return mTotalPages;
    }

    public void setTotalPages(String totalPages) {
        this.mTotalPages = totalPages;
    }

    public ArrayList<Result> getResults() {
        return mResults;
    }

    public void setResults(ArrayList<Result> results) {
        this.mResults = results;
    }

    static final Parcelable.Creator<DiscoverMovie> CREATOR = new Parcelable.Creator<DiscoverMovie>() {
        @Override
        public DiscoverMovie createFromParcel(Parcel in) {
            return new DiscoverMovie(in);
        }

        @Override
        public DiscoverMovie[] newArray(int size) {
            return new DiscoverMovie[size];
        }
    };
}
