package agafonova.com.popularmovies.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ReviewResult implements Parcelable {

    String author;
    String content;
    String id;
    String url;

    public ReviewResult() {

    }

    public ReviewResult(String iPage, String iTotalResults, String iID, String iUrl) {
        this.author = iPage;
        this.content = iTotalResults;
        this.id = iID;
        this.url = iUrl;
    }

    private ReviewResult(Parcel in) {
        author = in.readString();
        content =  in.readString();
        id = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(id);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {return url; }

    public void setUrl(String url) { this.url = url; }

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
