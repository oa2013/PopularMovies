package agafonova.com.popularmovies.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import agafonova.com.popularmovies.model.DiscoverMovie;
import agafonova.com.popularmovies.model.Result;

public class JsonUtils {

    public static List<Result> parseResults(String data) {

        try {
            DiscoverMovie movies = new DiscoverMovie();
            List<Result> results = new ArrayList<Result>();
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
}
