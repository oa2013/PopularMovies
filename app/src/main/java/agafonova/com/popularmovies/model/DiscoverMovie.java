package agafonova.com.popularmovies.model;

import java.util.ArrayList;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class DiscoverMovie implements Parcelable {

    String page;
    String totalResults;
    String totalPages;
    ArrayList<Result> results = null;

    public DiscoverMovie() {

    }

    public DiscoverMovie(String iPage, String iTotalResults, String iTotalPages, ArrayList<Result> iResults) {
        this.page = iPage;
        this.totalResults = iTotalResults;
        this.totalPages = iTotalPages;
        this.results = iResults;
    }

    private DiscoverMovie(Parcel in) {
        page = in.readString();
        totalResults =  in.readString();
        totalPages = in.readString();
        in.readTypedList(results, Result.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(page);
        dest.writeString(totalResults);
        dest.writeString(totalPages);
        dest.writeTypedList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
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
