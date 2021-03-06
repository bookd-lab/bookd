package bookdlab.bookd.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import java.util.List;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.activities.EventActivity;
import bookdlab.bookd.activities.EventCreateActivity;
import bookdlab.bookd.models.Event;
import bookdlab.bookd.ui.UIUtils;
import bookdlab.bookd.views.DefaultViewHolder;
import bookdlab.bookd.views.EventItemViewHolder;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2016
 */

public class EventsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int EVENT_ITEM = 0;
    private static final int EVENT_ITEM_CREATE = 1;

    public static final String EXTRA_EVENT = "EXTRA_EVENT";
    private Activity activity;
    private List<Event> eventList;

    public EventsAdapter(Activity activity, List<Event> eventList) {
        this.eventList = eventList;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        if (viewType == EVENT_ITEM_CREATE) {
            view = LayoutInflater.from(activity).inflate(R.layout.list_item_event_create, parent, false);
            return new DefaultViewHolder(view);
        }

        view = LayoutInflater.from(activity).inflate(R.layout.list_item_event, parent, false);
        return new EventItemViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return EVENT_ITEM_CREATE;
        }

        return EVENT_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder rh, int position) {
        if (position != 0) {
            EventItemViewHolder h = (EventItemViewHolder) rh;
            Event event = eventList.get(position - 1);
            h.eventTitle.setText(event.getName());
            h.datesInfo.setText(event.getStartDate() + " to " + event.getEndDate());
            h.backgroundOverlay.setBackgroundColor(UIUtils.getMaterialColor(activity));
            //h.backgroundOverlay.setBackgroundColor(Color.parseColor(event.getColor()));

            h.rootView.setOnClickListener((v) -> {
                    Intent intent = new Intent(activity, EventActivity.class);
                    intent.putExtra(EXTRA_EVENT, Parcels.wrap(event));
                    activity.startActivity(intent);
            });
            if(position == eventList.size()) {
                h.bottomDivider.setVisibility(View.VISIBLE);
            } else {
                h.bottomDivider.setVisibility(View.GONE);
            }
        } else {
            DefaultViewHolder holder = (DefaultViewHolder) rh;
            holder.rootView.setOnClickListener((v) -> {
                Intent intent = new Intent(activity, EventCreateActivity.class);
                activity.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return eventList.size() + 1;
    }
}
