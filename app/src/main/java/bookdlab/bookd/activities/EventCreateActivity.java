package bookdlab.bookd.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;

import org.parceler.Parcels;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.CreateEventWizardAdapter;
import bookdlab.bookd.interfaces.WizardNavigator;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventCreateActivity extends AppCompatActivity implements WizardNavigator {

    public static final String EVENT_EXTRA = "EVENT_EXTRA";

    private FragmentPagerAdapter adapterViewPager;

    private Event event;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

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

    }
}
