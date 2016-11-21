package bookdlab.bookd.fragments;

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
import bookdlab.bookd.adapters.EventsAdapter;
import bookdlab.bookd.api.EventsClient;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/21/2016.
 */

public class EventListFragment extends Fragment{
    @BindView(R.id.eventsListRV)
    RecyclerView eventsListRV;

    private EventsAdapter eventsAdapter;
    private List<Event> eventList;

    private EventsClient eventsClient = new EventsClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(getActivity(), eventList);
        eventsListRV.setAdapter(eventsAdapter);
        eventsListRV.setLayoutManager(linearLayoutManager);

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
