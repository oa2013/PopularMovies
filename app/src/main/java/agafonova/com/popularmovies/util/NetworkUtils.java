package agafonova.com.popularmovies.util;

/*
* MovieDB request format:
* https://api.themoviedb.org/3/discover/movie?page=1&include_video=false&include_adult=false&sort_by=popularity.desc&language=en-US&api_key=MY_KEY'
*/

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie?";
    private static final String PAGE_PARAM = "page";
    private static final String VIDEO_PARAM = "include_video";
    private static final String ADULT_PARAM = "include_adult";
    private static final String SORT_PARAM = "sort_by";
    private static final String LANGUAGE_PARAM = "language";
    private static final String API_PARAM = "api_key";
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static String getMoviesByPopularity(String page, String video, String adult, String sort, String language, String apiKey) {

            String resultsJSONString = "";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                Uri builtURI = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(PAGE_PARAM, page)
                        .appendQueryParameter(VIDEO_PARAM, video)
                        .appendQueryParameter(ADULT_PARAM, sort)
                        .appendQueryParameter(SORT_PARAM, sort)
                        .appendQueryParameter(LANGUAGE_PARAM, language)
                        .appendQueryParameter(API_PARAM, apiKey)
                        .build();

                URL requestURL = new URL(builtURI.toString());

                urlConnection = (HttpURLConnection) requestURL.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();

                StringBuilder builder = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {

                    builder.append(line + "\n");
                }

                if (builder.length() == 0) {
                    return null;
                }
                resultsJSONString = builder.toString();
            }
            catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
            return resultsJSONString;
    }
}
