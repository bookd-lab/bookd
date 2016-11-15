package bookdlab.bookd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.ReviewsAdapter;
import bookdlab.bookd.api.ReviewsClient;
import bookdlab.bookd.models.Review;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class ReviewsFragment extends Fragment {

    @BindView(R.id.reviewsRV)
    RecyclerView recyclerView;

    private ReviewsAdapter reviewsAdapter;
    private List <Review> reviewList;
    private Context mContext;

    //TODO: inject this properly
    private ReviewsClient reviewsClient = new ReviewsClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

    public static ReviewsFragment newInstance() {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reviews, container, false);
        ButterKnife.bind(this, view);

        reviewList = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(getActivity(), reviewList);
        recyclerView.setAdapter(reviewsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Review> myReviews = reviewsClient.getReviews();
        reviewList.addAll(myReviews);
        reviewsAdapter.notifyDataSetChanged();
    }
}
