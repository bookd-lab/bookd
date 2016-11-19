package bookdlab.bookd.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.CreateEventWizardAdapter;
import bookdlab.bookd.interfaces.WizardNavigator;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventCreateActivity extends AppCompatActivity implements WizardNavigator {

    private FragmentPagerAdapter adapterViewPager;

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
    }

    @Override
    public void onNext() {
        int nextPos = viewPager.getCurrentItem() + 1;

        if (nextPos < adapterViewPager.getCount()) {
            viewPager.setCurrentItem(nextPos);
        } else {
            //TODO: aggregate all the data
        }
    }

    @Override
    public void onPrev() {
        int prevPos = viewPager.getCurrentItem() - 1;

        if (prevPos >= 0) {
            viewPager.setCurrentItem(prevPos);
        }
    }
}
