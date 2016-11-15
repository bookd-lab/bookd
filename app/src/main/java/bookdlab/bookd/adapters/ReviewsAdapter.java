package bookdlab.bookd.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.models.Review;
import bookdlab.bookd.views.ReviewItemViewHolder;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Activity activity;
    private List<Review> reviewList;

    public ReviewsAdapter(Activity activity, List<Review> reviewList) {
        this.reviewList = reviewList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_review, parent, false);

        return new ReviewItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rh, int position) {
            ReviewItemViewHolder h = (ReviewItemViewHolder) rh;
            Review review = reviewList.get(position);
            h.reviewBody.setText(review.getReviewBody());
            h.reviewDate.setText(review.getReviewDate());
            h.reviewer.setText(review.getReviewerName());
            h.ratingBar.setRating(review.getStarRating());
            Glide.with(activity)
                    .load(review.getReviewerImgUrl())
                    .into(h.reviewerImage);
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}
