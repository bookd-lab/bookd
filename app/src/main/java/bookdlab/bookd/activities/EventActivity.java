package bookdlab.bookd.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.greenfrvr.hashtagview.HashtagView;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.BusinessAdapter;
import bookdlab.bookd.database.Queries;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventActivity extends AppCompatActivity {

    public static final String EXTRA_EVENT = "EXTRA_EVENT";
    private static final String TAG = "EventActivity";

    @BindView(R.id.tvEventName)
    TextView tvEventName;
    @BindView(R.id.tvDate)
    TextView tvDate;
    //@BindView(R.id.tvTags) TextView tvTags;
    @BindView(R.id.bookingsRV)
    RecyclerView bookingsRV;
    @BindView(R.id.noBookingsYet)
    TextView noBookingsYet;
    @BindView(R.id.hashTVTags)
    HashtagView hashTVTags;

    Event eventData;
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


        eventData = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_EVENT));

        new Queries().getBookedBusinessesForEvent(eventData.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if(children.iterator().hasNext()) {
                    for (DataSnapshot child : children) {
                        Business business = child.getValue(Business.class);
                        bookingsList.add(business);
                    }
                    businessAdapter.notifyDataSetChanged();
                    bookingsRV.setVisibility(View.VISIBLE);
                } else {
                    Log.d(TAG, "No more businesses found for event id " + eventData.getId());
                    bookingsRV.setVisibility(View.GONE);
                    noBookingsYet.setVisibility(View.VISIBLE);

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        initData();
    }

    private void initData(){
        tvEventName.setText(eventData.getName());
        tvDate.setText(eventData.getDates());
        String [] eventTags = eventData.getTags();

        //List<String> data = Arrays.asList("DJs", "Photographers", "Caterers");
        if(eventTags != null){
            List<String> eventTagsList = Arrays.asList(eventTags);
            hashTVTags.setData(eventTagsList);
        }
        /*String formattedTags = "";
        if(eventTags != null){
            for(int i= 0; i<eventTags.length; i++){
                formattedTags = formattedTags + eventTags[1] + ", ";
            }
        }
        tvTags.setText(formattedTags);
        */

        //loadingIndicatorView.show();
    }


}
