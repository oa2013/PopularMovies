package agafonova.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class GetReview implements Parcelable {

    String id;
    String page;
    String totalResults;
    String totalPages;
    ArrayList<ReviewResult> results = null;

    public GetReview() {

    }

    public GetReview(String iId, String iPage, String iTotalResults, String iTotalPages, ArrayList<ReviewResult> iResults) {
        this.id = iId;
        this.page = iPage;
        this.totalResults = iTotalResults;
        this.totalPages = iTotalPages;
        this.results = iResults;
    }

    private GetReview(Parcel in) {
        id = in.readString();
        page = in.readString();
        totalResults =  in.readString();
        totalPages = in.readString();
        in.readTypedList(results, ReviewResult.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(page);
        dest.writeString(totalResults);
        dest.writeString(totalPages);
        dest.writeTypedList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ArrayList<ReviewResult> getResults() {
        return results;
    }

    public void setResults(ArrayList<ReviewResult> results) {
        this.results = results;
    }

    static final Parcelable.Creator<GetReview> CREATOR = new Parcelable.Creator<GetReview>() {
        @Override
        public GetReview createFromParcel(Parcel in) {
            return new GetReview(in);
        }

        @Override
        public GetReview[] newArray(int size) {
            return new GetReview[size];
        }
    };
}
