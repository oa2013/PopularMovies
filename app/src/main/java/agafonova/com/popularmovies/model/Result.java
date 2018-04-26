package agafonova.com.popularmovies.model;

import java.util.ArrayList;
import java.util.List;

public class Result {

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

}
