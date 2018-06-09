package agafonova.com.popularmovies.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable{

    private String mVoteCount;
    private String mId;
    private String mVideo;
    private String mVoteAverage;
    private String mTitle;
    private String mPopularity;
    private String mPosterPath;
    private String mOriginalLanguage;
    private String mOriginalTitle;
    private List<String> mGenreIds = null;
    private String mBackdropPath;
    private String mAdult;
    private String mOverview;
    private String mReleaseDate;
    private int mIsFavorite = 0;

    public Result() {

    }

    public Result(String iVoteCount, String iId, String iVideo, String iVoteAverage, String iTitle, String iPopularity, String iPosterPath, String iOriginalLanguage,
                  String iOriginalTitle, ArrayList<String> iGenreIds, String iBackdropPath, String iAdult, String iOverview, String iReleaseDate, int iIsFavorite) {
        this.mVoteCount = iVoteCount;
        this.mId = iId;
        this.mVideo = iVideo;
        this.mVoteAverage = iVoteAverage;
        this.mTitle = iTitle;
        this.mPopularity = iPopularity;
        this.mPosterPath = iPosterPath;
        this.mOriginalLanguage = iOriginalLanguage;
        this.mOriginalTitle = iOriginalTitle;
        this.mGenreIds = iGenreIds;
        this.mBackdropPath = iBackdropPath;
        this.mAdult = iAdult;
        this.mOverview = iOverview;
        this.mReleaseDate = iReleaseDate;
        this.mIsFavorite = iIsFavorite;
    }

    Result(Parcel in) {
        this.mVoteCount = in.readString();
        this.mId = in.readString();
        this.mVideo = in.readString();
        this.mVoteAverage = in.readString();
        this.mTitle = in.readString();
        this.mPopularity = in.readString();
        this.mPosterPath = in.readString();
        this.mOriginalLanguage = in.readString();
        this.mOriginalTitle = in.readString();

        this.mGenreIds = new ArrayList<String>();
        in.readStringList(mGenreIds);

        this.mBackdropPath = in.readString();
        this.mAdult = in.readString();
        this.mOverview = in.readString();
        this.mReleaseDate = in.readString();
        this.mIsFavorite = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mVoteCount);
        dest.writeString(mId);
        dest.writeString(mVideo);
        dest.writeString(mVoteAverage);
        dest.writeString(mTitle);
        dest.writeString(mPopularity);
        dest.writeString(mPosterPath);
        dest.writeString(mOriginalLanguage);
        dest.writeString(mOriginalTitle);
        dest.writeStringList(mGenreIds);
        dest.writeString(mBackdropPath);
        dest.writeString(mAdult);
        dest.writeString(mOverview);
        dest.writeString(mReleaseDate);
        dest.writeInt(mIsFavorite);
    }

    public static class RatingComparator implements Comparator<Result> {

        public int compare(Result one, Result two)
        {
            double comparison = Double.parseDouble(two.mVoteAverage)-Double.parseDouble(one.mVoteAverage);
            if(comparison > 0) return 1;
            if(comparison < 0) return -1;
            return 0;
        }
    }

    public static class PopularityComparator implements Comparator<Result> {

        public int compare(Result one, Result two)
        {
            double comparison = Double.parseDouble(two.mPopularity)-Double.parseDouble(one.mPopularity);
            if(comparison > 0) return 1;
            if(comparison < 0) return -1;
            return 0;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(String voteCount) {
        this.mVoteCount = voteCount;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getVideo() {
        return mVideo;
    }

    public void setVideo(String video) {
        this.mVideo = video;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.mVoteAverage = voteAverage;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getPopularity() {
        return mPopularity;
    }

    public void setPopularity(String popularity) {
        this.mPopularity = popularity;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String posterPath) {
        this.mPosterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return mOriginalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.mOriginalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.mOriginalTitle = originalTitle;
    }

    public List<String> getGenreIds() {
        return mGenreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.mGenreIds = genreIds;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.mBackdropPath = backdropPath;
    }

    public String getAdult() {
        return mAdult;
    }

    public void setAdult(String adult) {
        this.mAdult = adult;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String overview) {
        this.mOverview = overview;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
    }

    public int getIsFavorite() {return mIsFavorite; }

    public void setIsFavorite(int isFavorite) { this.mIsFavorite = isFavorite; }

    public static final Parcelable.Creator<Result> CREATOR = new Parcelable.Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };
}
