package agafonova.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import agafonova.com.popularmovies.R;
import agafonova.com.popularmovies.model.ReviewResult;

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewAdapterViewHolder> {

    private List<ReviewResult> mReviewList;
    final private MovieReviewAdapter.ResourceClickListener mOnClickListener;

    public MovieReviewAdapter(MovieReviewAdapter.ResourceClickListener listener) {
        mOnClickListener = listener;
    }

    public interface ResourceClickListener {
        void onReviewClick(String data);
    }

    @Override
    public MovieReviewAdapter.MovieReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.review_item, parent, false);
        MovieReviewAdapter.MovieReviewAdapterViewHolder viewHolder = new MovieReviewAdapter.MovieReviewAdapterViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieReviewAdapter.MovieReviewAdapterViewHolder holder, int position) {

        ReviewResult oneReview = mReviewList.get(position);

        try {
            holder.authorTextView.setText(oneReview.getAuthor());
            holder.contentTextView.setText(oneReview.getContent());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(mReviewList == null) {
            return 0;
        }
        return mReviewList.size();
    }

    public void setData(List<ReviewResult> results) {
        mReviewList = results;
        notifyDataSetChanged();
    }

    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView authorTextView;
        private TextView contentTextView;

        public MovieReviewAdapterViewHolder(View itemView) {
            super(itemView);
            authorTextView = itemView.findViewById(R.id.review_author_text);
            contentTextView = itemView.findViewById(R.id.review_content);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String reviewID = mReviewList.get(clickedPosition).getId();
            mOnClickListener.onReviewClick(reviewID);
        }
    }
}
