package agafonova.com.popularmovies.util;

/*
* MovieDB request format by popularity:
* http://api.themoviedb.org/3/movie/popular?api_key=MY_KEY
*
* By rating:
* https://api.themoviedb.org/3/movie/top_rated?api_key=MY_KEY&language=en-US&page=1
*
* Poster request format:
* http://image.tmdb.org/t/p/w185/askg3SMvhqEl4OL52YuvdtY40Yb.jpg
*
* Movie review request format:
* https://api.themoviedb.org/3/movie/60281/reviews?api_key=MY_KEY=en-US&page=1
*
* Trailer request format:
* https://api.themoviedb.org/3/movie/550/videos?api_key=MY_KEY=language=en-US
*/

import android.net.Uri;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String BASE_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular?api_key=";
    private static final String BASE_URL_TOPRATED = "https://api.themoviedb.org/3/movie/top_rated?api_key=";

    static String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/";
    static String BASE_URL_TRAILERS = "https://api.themoviedb.org/3/movie/";

    private static final String API_PARAM = "api_key";
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    public static String getPopularMovies(String apiKey) {

            String resultsJSONString = "";
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                Uri builtURI = Uri.parse(BASE_URL_POPULAR).buildUpon()
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

    public static String getTopRatedMovies(String apiKey) {

        String resultsJSONString = "";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            Uri builtURI = Uri.parse(BASE_URL_TOPRATED).buildUpon()
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

    public static String getMovieReviews(String apiKey, String movieID) {

        String resultsJSONString = "";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        BASE_URL_REVIEWS  =  BASE_URL_REVIEWS + movieID + "/reviews?";

        try {
            Uri builtURI = Uri.parse(BASE_URL_REVIEWS).buildUpon()
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

    public static String getTrailers(String apiKey, String movieID) {

        String resultsJSONString = "";
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        BASE_URL_TRAILERS  =  BASE_URL_TRAILERS + movieID + "/videos?";

        try {
            Uri builtURI = Uri.parse(BASE_URL_TRAILERS).buildUpon()
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
