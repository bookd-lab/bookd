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
import bookdlab.bookd.adapters.EventsAdapter;
import bookdlab.bookd.api.EventsClient;
import bookdlab.bookd.models.Event;
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

        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(getActivity(), eventList);
        recyclerView.setAdapter(eventsAdapter);
        recyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Event> myEvents = eventsClient.getMyEvents();
        eventList.addAll(myEvents);
        eventsAdapter.notifyDataSetChanged();
    }
}
