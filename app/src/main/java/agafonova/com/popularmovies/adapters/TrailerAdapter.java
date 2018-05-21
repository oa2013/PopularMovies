package agafonova.com.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;
import agafonova.com.popularmovies.R;
import agafonova.com.popularmovies.model.TrailerResult;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder>{

    private static final String NO_TRAILERS = "No trailers available.";
    private List<TrailerResult> mTrailerList;
    final private TrailerAdapter.ResourceClickListener mOnClickListener;

    public TrailerAdapter(TrailerAdapter.ResourceClickListener listener) {
        mOnClickListener = listener;
    }

    public interface ResourceClickListener {
        void onTrailerClick(String data);
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.trailer_item, parent, false);
        TrailerAdapter.TrailerAdapterViewHolder viewHolder = new TrailerAdapter.TrailerAdapterViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerAdapterViewHolder holder, int position) {

        TrailerResult oneTrailer = mTrailerList.get(position);

        try {
            if(mTrailerList.size()>0) {
                holder.textViewTrailer.setText(oneTrailer.getName());
            }
            else {
                holder.textViewTrailer.setText(NO_TRAILERS);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(mTrailerList == null) {
            return 0;
        }
        return mTrailerList.size();
    }

    public void setData(List<TrailerResult> results) {
        mTrailerList = results;
        notifyDataSetChanged();
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textViewTrailer;
        private ImageView imageViewTrailer;

        public TrailerAdapterViewHolder(View itemView) {
            super(itemView);
            textViewTrailer = itemView.findViewById(R.id.textViewTrailer);
            imageViewTrailer = itemView.findViewById(R.id.imageViewTrailer);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            String trailerID = mTrailerList.get(clickedPosition).getId();
            mOnClickListener.onTrailerClick(trailerID);
        }
    }
}
