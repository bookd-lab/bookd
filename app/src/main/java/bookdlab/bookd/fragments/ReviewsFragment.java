package bookdlab.bookd.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.ReviewsAdapter;
import bookdlab.bookd.api.EventsClient;
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

    //TODO: inject this properly
    private EventsClient eventsClient = new EventsClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static EventsFragment newInstance() {
        EventsFragment fragment = new EventsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);
        ButterKnife.bind(this, view);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);

        reviewList = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(getActivity(), reviewList);
        recyclerView.setAdapter(reviewsAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //List<Event> myReviews = ReviewsClient.get;
        //reviewList.addAll(myReviews);
        reviewsAdapter.notifyDataSetChanged();
    }
}
