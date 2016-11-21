package bookdlab.bookd.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.parceler.Parcels;
import java.util.ArrayList;
import bookdlab.bookd.BookdApplication;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.CreateEventWizardAdapter;
import bookdlab.bookd.interfaces.WizardNavigator;
import bookdlab.bookd.models.BookedBusiness;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventCreateActivity extends AppCompatActivity implements WizardNavigator {

    public static final String EVENT_EXTRA = "EVENT_EXTRA";

    private FragmentPagerAdapter adapterViewPager;
    private Event event;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private static final String TAG = "EventCreateActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        ButterKnife.bind(this);

        adapterViewPager = new CreateEventWizardAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapterViewPager);
        viewPager.setPageTransformer(true, new CubeOutTransformer());

        if (null != getIntent().getParcelableExtra(EVENT_EXTRA)) {
            event = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_EXTRA));
        } else {
            event = new Event();
        }
    }

    @Override
    public void onNext() {
        int nextPos = viewPager.getCurrentItem() + 1;

        if (nextPos < adapterViewPager.getCount()) {
            viewPager.setCurrentItem(nextPos);
        } else {
            saveAndFinish();
            adapterViewPager.notifyDataSetChanged();
        }
    }

    @Override
    public void onPrev() {
        int prevPos = viewPager.getCurrentItem() - 1;

        if (prevPos >= 0) {
            viewPager.setCurrentItem(prevPos);
        }
    }

    public Event getEvent() {
        return event;
    }

    private void saveAndFinish() {
        ArrayList<BookedBusiness> bookedBusinessArrayList = new ArrayList<>();
        createEvent(event, bookedBusinessArrayList);
        updateEvent(event, bookedBusinessArrayList);
        finish();
    }

    private void createEvent(Event event, ArrayList<BookedBusiness> bookedBusinesses){
        Log.d(TAG, "createEvent: ");
        DatabaseReference eventsDbReference = FirebaseDatabase.getInstance().getReference().child("events");
        DatabaseReference eventReference = eventsDbReference.push();
        String eventId = eventReference.getKey();
        event.setId(eventId);
        event.setCreator(BookdApplication.getCurrentUser().getId());
        eventReference.setValue(event);
        createBookedBusinesses(event, bookedBusinesses);
    }

    private void createBookedBusinesses(Event event, ArrayList<BookedBusiness> bookedBusinesses){
        Log.d(TAG, "createBookedBusinesses: ");
        DatabaseReference bookedBusinessDbReference = FirebaseDatabase.getInstance().getReference().child("event_booked_business");
        String eventId = event.getId();
        for(BookedBusiness bookedBusiness : bookedBusinesses) {
            DatabaseReference bookedBusinessRef = bookedBusinessDbReference.push();
            String id = bookedBusinessRef.getKey();             // Get unique ID

            bookedBusiness.setId(id);                           // Set that unique ID as bookedBusiness ID
            bookedBusiness.setEventId(eventId);                 // Set event ID. used while querying

            bookedBusinessRef.setValue(bookedBusiness);
        }
    }

    private void updateEvent(Event event, ArrayList<BookedBusiness> bookedBusinesses){
        Log.d(TAG, "updateEvent: ");
        DatabaseReference eventReference = FirebaseDatabase.getInstance().getReference().child("events").child(event.getId());
        eventReference.setValue(event);

        updateBookedBusinesses(event, bookedBusinesses);
    }

    private void updateBookedBusinesses(Event event, ArrayList<BookedBusiness> bookedBusinesses){
        Log.d(TAG, "updateBookedBusinesses: ");
        DatabaseReference bookedBusinessDbReference = FirebaseDatabase.getInstance().getReference().child("event_booked_business");
        for(BookedBusiness bookedBusiness : bookedBusinesses){
            DatabaseReference child = bookedBusinessDbReference.child(bookedBusiness.getId());
            child.setValue(bookedBusiness);
        }
    }
}