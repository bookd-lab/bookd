package bookdlab.bookd.activities;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.fragments.EventCreateWizardFragment;


public class EventCreateActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setPageTransformer(true, new CubeOutTransformer());
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            Resources res = BookdApplication.getContext().getResources();
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return EventCreateWizardFragment.newInstance(0, res.getString(R.string.create_event_step_1), res.getString(R.string.create_event_step_1_hint));
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return EventCreateWizardFragment.newInstance(1, res.getString(R.string.create_event_step_2), res.getString(R.string.create_event_step_2_hint));
                case 2: // Fragment # 1 - This will show SecondFragment
                    return EventCreateWizardFragment.newInstance(2, res.getString(R.string.create_event_step_3), res.getString(R.string.create_event_step_3_hint));
                case 3: // Fragment # 1 - This will show SecondFragment
                    return EventCreateWizardFragment.newInstance(3, res.getString(R.string.create_event_step_4), "no hint needed here");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Step " + position+1;
        }

    }
}
