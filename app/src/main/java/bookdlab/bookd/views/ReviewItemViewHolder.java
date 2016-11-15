package bookdlab.bookd.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import bookdlab.bookd.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class ReviewItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tvReviewer)
    public TextView reviewer;
    @BindView(R.id.tvReviewDate)
    public TextView reviewDate;
    @BindView(R.id.tvReviewBody)
    public TextView reviewBody;
    @BindView(R.id.ratingBar)
    public RatingBar ratingBar;
    @BindView(R.id.ivReviewer)
    public CircleImageView reviewerImage;

    public ReviewItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
