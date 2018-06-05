package agafonova.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import java.util.Locale;
import agafonova.com.popularmovies.R;
import agafonova.com.popularmovies.db.FavoriteItem;
import agafonova.com.popularmovies.db.FavoritesDBHelper;
import agafonova.com.popularmovies.model.Result;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ResultAdapterViewHolder>  {

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";
    private ImageView posterView;
    private List<Result> mMovieList;
    final private ResultAdapter.ResourceClickListener mOnClickListener;
    FavoritesDBHelper moviesDB;

    public ResultAdapter(ResultAdapter.ResourceClickListener listener, FavoritesDBHelper db) {
        mOnClickListener = listener;
        moviesDB = db;
    }

    public interface ResourceClickListener {
        void onPosterClick(String data);
    }

    @Override
    public ResultAdapter.ResultAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);
        ResultAdapter.ResultAdapterViewHolder viewHolder = new ResultAdapter.ResultAdapterViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ResultAdapter.ResultAdapterViewHolder holder, int position) {

        Result oneMovie = mMovieList.get(position);
        String imagePath = IMAGE_URL + oneMovie.getPosterPath();

        try {
            Picasso.with(holder.itemView.getContext()).load(imagePath).placeholder(R.mipmap.ic_image_placeholder)
                    .error(R.mipmap.ic_image_placeholder).into(holder.posterView);
            holder.popularityTextView.setText(String.format(Locale.US,"%.1f",Float.parseFloat(oneMovie.getPopularity())));
            holder.ratingTextView.setText(String.format(Locale.US, "%.1f",Float.parseFloat(oneMovie.getVoteAverage())));

            //If this movie is in the favorites database, then we display a favorite icon
            //Otherwise, we hide the icon
            FavoriteItem existingItem = moviesDB.query(oneMovie.getTitle());
            if(existingItem.getFavorite() != null)
            {
                holder.favoriteView.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.favoriteView.setVisibility(View.INVISIBLE);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(mMovieList == null) {
            return 0;
        }
        return mMovieList.size();
    }

    public void setData(List<Result> results) {
        mMovieList = results;
        notifyDataSetChanged();
    }

    public class ResultAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView posterView;
        private ImageView favoriteView;
        private TextView popularityTextView;
        private TextView ratingTextView;

        public ResultAdapterViewHolder(View itemView) {
            super(itemView);
            posterView = itemView.findViewById(R.id.movie_poster);
            popularityTextView = itemView.findViewById(R.id.movie_popularity);
            ratingTextView = itemView.findViewById(R.id.movie_rating);
            favoriteView = itemView.findViewById(R.id.favorite_img);
            posterView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String movieID = mMovieList.get(clickedPosition).getId();
            mOnClickListener.onPosterClick(movieID);
        }
    }
}

