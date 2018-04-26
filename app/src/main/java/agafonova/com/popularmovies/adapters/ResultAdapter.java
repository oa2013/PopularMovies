package agafonova.com.popularmovies.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import java.util.List;
import agafonova.com.popularmovies.R;
import agafonova.com.popularmovies.model.Result;

public class ResultAdapter extends ArrayAdapter<Result> {

    private ImageView posterView;
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    public ResultAdapter(Activity context, List<Result> movieResults) {
        super(context,0,movieResults);
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        Result oneMovie = getItem(position);

        if(view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
        }

        String imagePath = IMAGE_URL + oneMovie.getPosterPath();

        try {
            posterView = (ImageView) view.findViewById(R.id.movie_poster);
            Picasso.with(getContext()).load(imagePath).into(posterView);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }
}

