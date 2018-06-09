package agafonova.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;

public class GetReview implements Parcelable {

    private String mId;
    private String mPage;
    private String mTotalResults;
    private String mTotalPages;
    private ArrayList<ReviewResult> mResults = null;

    public GetReview() {

    }

    public GetReview(String iId, String iPage, String iTotalResults, String iTotalPages, ArrayList<ReviewResult> iResults) {
        this.mId = iId;
        this.mPage = iPage;
        this.mTotalResults = iTotalResults;
        this.mTotalPages = iTotalPages;
        this.mResults = iResults;
    }

    private GetReview(Parcel in) {
        mId = in.readString();
        mPage = in.readString();
        mTotalResults =  in.readString();
        mTotalPages = in.readString();
        in.readTypedList(mResults, ReviewResult.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mPage);
        dest.writeString(mTotalResults);
        dest.writeString(mTotalPages);
        dest.writeTypedList(mResults);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
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

    public ArrayList<ReviewResult> getResults() {
        return mResults;
    }

    public void setResults(ArrayList<ReviewResult> results) {
        this.mResults = results;
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
