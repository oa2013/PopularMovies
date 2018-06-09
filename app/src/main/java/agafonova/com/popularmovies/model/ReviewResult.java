package agafonova.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewResult implements Parcelable {

    private String mAuthor;
    private String mContent;
    private String mId;
    private String mUrl;

    public ReviewResult() {

    }

    public ReviewResult(String iPage, String iTotalResults, String iID, String iUrl) {
        this.mAuthor = iPage;
        this.mContent = iTotalResults;
        this.mId = iID;
        this.mUrl = iUrl;
    }

    private ReviewResult(Parcel in) {
        mAuthor = in.readString();
        mContent =  in.readString();
        mId = in.readString();
        mUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mAuthor);
        dest.writeString(mContent);
        dest.writeString(mId);
        dest.writeString(mUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        this.mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getUrl() {return mUrl; }

    public void setUrl(String url) { this.mUrl = url; }

    static final Parcelable.Creator<ReviewResult> CREATOR = new Parcelable.Creator<ReviewResult>() {
        @Override
        public ReviewResult createFromParcel(Parcel in) {
            return new ReviewResult(in);
        }

        @Override
        public ReviewResult[] newArray(int size) {
            return new ReviewResult[size];
        }
    };
}
