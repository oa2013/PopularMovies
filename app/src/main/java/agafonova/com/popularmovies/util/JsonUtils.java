package agafonova.com.popularmovies.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import agafonova.com.popularmovies.model.DiscoverMovie;
import agafonova.com.popularmovies.model.GetReview;
import agafonova.com.popularmovies.model.GetTrailer;
import agafonova.com.popularmovies.model.Result;
import agafonova.com.popularmovies.model.ReviewResult;
import agafonova.com.popularmovies.model.TrailerResult;

public class JsonUtils {

    public static ArrayList<Result> parseResults(String data) {

        try {
            DiscoverMovie movies = new DiscoverMovie();
            ArrayList<Result> results = new ArrayList<Result>();
            JSONObject jsonObject = new JSONObject(data);

            movies.setPage(jsonObject.getString("page"));
            movies.setTotalResults(jsonObject.getString("total_results"));
            movies.setTotalPages(jsonObject.getString("total_pages"));

            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for(int i=0; i<resultsArray.length(); i++) {
                JSONObject movieObject = resultsArray.getJSONObject(i);

                Result movieResult = new Result();
                movieResult.setVoteCount(movieObject.getString("vote_count"));
                movieResult.setId(movieObject.getString("id"));
                movieResult.setVideo(movieObject.getString("video"));
                movieResult.setVoteAverage(movieObject.getString("vote_average"));
                movieResult.setTitle(movieObject.getString("title"));
                movieResult.setPopularity(movieObject.getString("popularity"));
                movieResult.setPosterPath(movieObject.getString("poster_path"));
                movieResult.setOriginalLanguage(movieObject.getString("original_language"));
                movieResult.setOriginalTitle(movieObject.getString("original_title"));

                JSONArray genreArray = movieObject.getJSONArray("genre_ids");
                List<String> genreList = new ArrayList<String>();
                for(int j=0; j<genreArray.length(); j++) {
                    genreList.add(genreArray.getString(j));
                }
                movieResult.setGenreIds(genreList);
                movieResult.setBackdropPath(movieObject.getString("backdrop_path"));
                movieResult.setAdult(movieObject.getString("adult"));
                movieResult.setOverview(movieObject.getString("overview"));
                movieResult.setReleaseDate(movieObject.getString("release_date"));

                results.add(movieResult);
            }

            movies.setResults(results);

            return movies.getResults();
        }
        catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<ReviewResult> parseReviews(String data) {

        try {
            GetReview review = new GetReview();
            ArrayList<ReviewResult> results = new ArrayList<ReviewResult>();
            JSONObject jsonObject = new JSONObject(data);

            review.setId(jsonObject.getString("id"));
            review.setPage(jsonObject.getString("page"));

            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for(int i=0; i<resultsArray.length(); i++) {
                JSONObject reviewObject = resultsArray.getJSONObject(i);

                ReviewResult reviewResult = new ReviewResult();
                reviewResult.setAuthor(reviewObject.getString("author"));
                reviewResult.setContent(reviewObject.getString("content"));
                reviewResult.setId(reviewObject.getString("id"));
                reviewResult.setUrl(reviewObject.getString("url"));

                results.add(reviewResult);
            }
            review.setTotalResults(jsonObject.getString("total_results"));
            review.setTotalPages(jsonObject.getString("total_pages"));

            review.setResults(results);

            return review.getResults();
        }
        catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<TrailerResult> parseTrailers(String data) {

        try {
            GetTrailer trailer = new GetTrailer();
            ArrayList<TrailerResult> results = new ArrayList<TrailerResult>();
            JSONObject jsonObject = new JSONObject(data);

            trailer.setId(jsonObject.getString("id"));

            JSONArray resultsArray = jsonObject.getJSONArray("results");

            for(int i=0; i<resultsArray.length(); i++) {
                JSONObject trailerObject = resultsArray.getJSONObject(i);

                TrailerResult trailerResult = new TrailerResult();
                trailerResult.setId(trailerObject.getString("id"));
                trailerResult.setIso_639_1(trailerObject.getString("iso_639_1"));
                trailerResult.setIso_3166_1(trailerObject.getString("iso_3166_1"));
                trailerResult.setKey(trailerObject.getString("key"));
                trailerResult.setName(trailerObject.getString("name"));
                trailerResult.setSite(trailerObject.getString("site"));
                trailerResult.setSize(trailerObject.getString("size"));
                trailerResult.setType(trailerObject.getString("type"));

                results.add(trailerResult);
            }

            trailer.setResults(results);

            return trailer.getResults();
        }
        catch(JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
