package bookdlab.bookd.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.EventsAdapter;
import bookdlab.bookd.api.EventsClient;
import bookdlab.bookd.database.Queries;
import bookdlab.bookd.models.Event;
import bookdlab.bookd.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class EventsFragment extends Fragment {

    @BindView(R.id.eventsRV)
    RecyclerView recyclerView;

    private EventsAdapter eventsAdapter;
    private List<Event> eventList;

    User currentUser;
    private static final String TAG = "EventsFragment";

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

        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(getActivity(), eventList);
        recyclerView.setAdapter(eventsAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            Log.d(TAG, "setUserVisibleHint: "+isVisible());
            currentUser = BookdApplication.getCurrentUser();
            Log.d(TAG, "setUserVisibleHint: "+currentUser);
            updateEventsOfUser();
        } else {
            Log.d(TAG, "setUserVisibleHint: invisible");
        }
    }

    private void updateEventsOfUser(){
        if (currentUser != null) {
            new Queries().getEventsOfUser(currentUser.getId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    if (children.iterator().hasNext()) {
                        eventList.clear();
                        recyclerView.setVisibility(View.VISIBLE);
                        for (DataSnapshot child : children) {
                            Event event = child.getValue(Event.class);
                            eventList.add(event);
                        }
                        eventsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
