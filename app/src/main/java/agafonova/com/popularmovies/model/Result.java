package agafonova.com.popularmovies.model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.os.Parcel;
import android.os.Parcelable;

public class Result implements Parcelable{

    private String voteCount;
    private String id;
    private String video;
    private String voteAverage;
    private String title;
    private String popularity;
    private String posterPath;
    private String originalLanguage;
    private String originalTitle;
    private List<String> genreIds = null;
    private String backdropPath;
    private String adult;
    private String overview;
    private String releaseDate;

    public Result() {

    }

    public Result(String iVoteCount, String iId, String iVideo, String iVoteAverage, String iTitle, String iPopularity, String iPosterPath, String iOriginalLanguage,
                  String iOriginalTitle, ArrayList<String> iGenreIds, String iBackdropPath, String iAdult, String iOverview, String iReleaseDate) {
        this.voteCount = iVoteCount;
        this.id = iId;
        this.video = iVideo;
        this.voteAverage = iVoteAverage;
        this.title = iTitle;
        this.popularity = iPopularity;
        this.posterPath = iPosterPath;
        this.originalLanguage = iOriginalLanguage;
        this.originalTitle = iOriginalTitle;
        this.genreIds = iGenreIds;
        this.backdropPath = iBackdropPath;
        this.adult = iAdult;
        this.overview = iOverview;
        this.releaseDate = iReleaseDate;
    }

    Result(Parcel in) {
        this.voteCount = in.readString();
        this.id = in.readString();
        this.video = in.readString();
        this.voteAverage = in.readString();
        this.title = in.readString();
        this.popularity = in.readString();
        this.posterPath = in.readString();
        this.originalLanguage = in.readString();
        this.originalTitle = in.readString();

        this.genreIds = new ArrayList<String>();
        in.readStringList(genreIds);

        this.backdropPath = in.readString();
        this.adult = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(voteCount);
        dest.writeString(id);
        dest.writeString(video);
        dest.writeString(voteAverage);
        dest.writeString(title);
        dest.writeString(popularity);
        dest.writeString(posterPath);
        dest.writeString(originalLanguage);
        dest.writeString(originalTitle);
        dest.writeStringList(genreIds);
        dest.writeString(backdropPath);
        dest.writeString(adult);
        dest.writeString(overview);
        dest.writeString(releaseDate);
    }

    public static class RatingComparator implements Comparator<Result> {

        public int compare(Result one, Result two)
        {
            double comparison = Double.parseDouble(two.voteAverage)-Double.parseDouble(one.voteAverage);
            if(comparison > 0) return 1;
            if(comparison < 0) return -1;
            return 0;
        }
    }

    public static class PopularityComparator implements Comparator<Result> {

        public int compare(Result one, Result two)
        {
            double comparison = Double.parseDouble(two.popularity)-Double.parseDouble(one.popularity);
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
        return voteCount;
    }

    public void setVoteCount(String voteCount) {
        this.voteCount = voteCount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public List<String> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<String> genreIds) {
        this.genreIds = genreIds;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getAdult() {
        return adult;
    }

    public void setAdult(String adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

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
