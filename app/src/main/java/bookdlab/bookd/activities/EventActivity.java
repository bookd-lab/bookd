package bookdlab.bookd.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.BusinessAdapter;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT = "EXTRA_EVENT";

    @BindView(R.id.tvEventName)
    TextView tvEventName;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTags)
    TextView tvTags;
    @BindView(R.id.bookingsRV)
    RecyclerView bookingsRV;

    Event eventData;
    private List<Business> bookingsList;
    private BusinessAdapter businessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ButterKnife.bind(this);

        eventData = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_EVENT));

        bookingsList = new ArrayList<>();
        businessAdapter = new BusinessAdapter(this, bookingsList);

        initData();

    }

    private void initData(){
        tvEventName.setText(eventData.getName());
        tvDate.setText(eventData.getDates());
        //tvTags.setText(eventData.getTags());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bookingsRV.setLayoutManager(layoutManager);
        bookingsRV.setAdapter(businessAdapter);
        //loadingIndicatorView.show();
    }


}
