package bookdlab.bookd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.ReviewsAdapter;
import bookdlab.bookd.database.Queries;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.Review;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class ReviewsFragment extends Fragment {

    private static final String TAG = "ReviewsFragment";

    @BindView(R.id.reviewsRV)
    RecyclerView recyclerView;
    @BindView(R.id.noReviewsYet)
    TextView noReviewsYet;


    private ReviewsAdapter reviewsAdapter;
    private List<Review> reviewList;
    private Context mContext;

    private Business businessData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
    }

    public static ReviewsFragment newInstance(Business business) {
        ReviewsFragment fragment = new ReviewsFragment();
        Bundle args = new Bundle();
        args.putParcelable("business", Parcels.wrap(business));
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

        businessData = Parcels.unwrap(getArguments().getParcelable("business"));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //this should be done via pagination, but for now it's ok
        new Queries().getReviewsForBusiness(businessData.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if (children.iterator().hasNext()) {
                    for (DataSnapshot child : children) {
                        Review review = child.getValue(Review.class);
                        reviewList.add(review);
                    }

                    reviewsAdapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "No more reviews found for business id " + businessData.getId());
                    recyclerView.setVisibility(View.GONE);
                    noReviewsYet.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
