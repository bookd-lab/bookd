package bookdlab.bookd.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.greenfrvr.hashtagview.HashtagView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringJoiner;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.BusinessAdapter;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.interfaces.EventAware;
import bookdlab.bookd.interfaces.EventProvider;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.ui.EventUtils;
import bookdlab.bookd.views.TagItemView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rubab.uddin on 11/21/2016.
 */
public class EventFragment extends Fragment implements EventAware {
    private static final String TAG = EventFragment.class.getSimpleName();

    @BindView(R.id.tvEventName)
    TextView tvEventName;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.bookingsRV)
    RecyclerView bookingsRV;
    @BindView(R.id.noBookingsYet)
    TextView noBookingsYet;
    @BindView(R.id.backgroundImage)
    ImageView backgroundImage;
    @BindView(R.id.tagsCompletedContainer)
    LinearLayout tagsCompletedContainer;

    @Inject
    BookdApiClient bookdApiClient;

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
        ((BookdApplication) getActivity().getApplication()).getAppComponent().inject(this);
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
        tagsCompletedContainer.removeAllViews();
        for (String tag : eventTagsList) {
            tagsCompletedContainer.addView(new TagItemView(getActivity(), tag, isTagDoneYet(tag)));
        }

        int eventBackgroundResource = EventUtils.getEventBackgroundResouce(eventProvider.getEvent().getType());
        backgroundImage.setImageResource(eventBackgroundResource);


        StringBuilder idsParam = new StringBuilder("");
        for (String s : eventProvider.getEvent().getBusinesses()) {
            if (idsParam.length() > 0) {
                idsParam.append(",");
            }

            idsParam.append(s);
        }

        bookdApiClient.getBusinessesByIds(idsParam.toString()).enqueue(new Callback<List<Business>>() {
            @Override
            public void onResponse(Call<List<Business>> call, Response<List<Business>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, response.message());
                    setData(Collections.emptyList());
                    return;
                }

                setData(response.body());
            }

            @Override
            public void onFailure(Call<List<Business>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                setData(Collections.emptyList());
            }
        });
    }

    void setData(List<Business> businessList) {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        BusinessAdapter adapter = new BusinessAdapter(getActivity(), businessList, eventProvider.getEvent(), bookdApiClient);

        bookingsRV.setLayoutManager(gridLayoutManager);
        bookingsRV.setAdapter(adapter);
    }

    boolean isTagDoneYet(String tag) {
        //TODO: see if it is in businesses
        return false;
    }

    private void setEmptyData() {
        tvEventName.setText("");
        tvDate.setText("");
    }

    @Override
    public void updateEventData() {
        setEventData();
    }
}
