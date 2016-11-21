package bookdlab.bookd.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.BusinessAdapter;
import bookdlab.bookd.models.Business;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventActivity extends AppCompatActivity {

    @BindView(R.id.tvEventName)
    TextView tvEventName;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvTags)
    TextView tvTags;
    @BindView(R.id.bookingsRV)
    RecyclerView bookingsRV;


    private List<Business> bookingsList;
    private BusinessAdapter businessAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ButterKnife.bind(this);

        bookingsList = new ArrayList<>();
        businessAdapter = new BusinessAdapter(this, bookingsList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bookingsRV.setLayoutManager(layoutManager);
        bookingsRV.setAdapter(businessAdapter);
        //loadingIndicatorView.show();

    }


}
