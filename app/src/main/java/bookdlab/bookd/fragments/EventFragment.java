package bookdlab.bookd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenfrvr.hashtagview.HashtagView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import bookdlab.bookd.R;
import bookdlab.bookd.interfaces.EventAware;
import bookdlab.bookd.interfaces.EventProvider;
import bookdlab.bookd.ui.EventUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/21/2016.
 */
public class EventFragment extends Fragment implements EventAware {

    @BindView(R.id.tvEventName)
    TextView tvEventName;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.bookingsRV)
    RecyclerView bookingsRV;
    @BindView(R.id.noBookingsYet)
    TextView noBookingsYet;
    @BindView(R.id.hashTVTags)
    HashtagView hashTVTags;
    @BindView(R.id.backgroundImage)
    ImageView backgroundImage;

    private EventProvider eventProvider;
    private DateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static EventFragment newInstance() {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        setEventData();
    }

    @Override
    public void onAttach(Context context) {
        if (!(context instanceof EventProvider)) {
            throw new ClassCastException("context has to implement " + EventProvider.class.getName());
        }

        super.onAttach(context);
        this.eventProvider = (EventProvider) context;
        this.eventProvider.addEventAware(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.eventProvider.removeEventAware(this);
        this.eventProvider = null;
    }

    public void setEventData() {
        if (null == eventProvider.getEvent()) {
            setEmptyData();
            return;
        }

        tvEventName.setText(eventProvider.getEvent().getName());


        Date startDate = new Date(eventProvider.getEvent().getStartDate());
        Date endDate = new Date(eventProvider.getEvent().getEndDate());

        String whenLabel = sdf.format(startDate) + " - " + sdf.format(endDate);
        tvDate.setText(whenLabel);

        List<String> eventTagsList = eventProvider.getEvent().getTags();
        if (eventTagsList != null) {
            hashTVTags.setData(eventTagsList);
        }

        int eventBackgroundResource = EventUtils.getEventBackgroundResouce(eventProvider.getEvent().getType());
        backgroundImage.setImageResource(eventBackgroundResource);
    }

    private void setEmptyData() {
        tvEventName.setText("");
        tvDate.setText("");
        hashTVTags.setData(new ArrayList<>());
    }

    @Override
    public void updateEventData() {
        setEventData();
    }
}
