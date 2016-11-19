package bookdlab.bookd.adapters;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.fragments.wizards.EventCreateWizardDateFragment;
import bookdlab.bookd.fragments.wizards.EventCreateWizardFragment;

/**
 * Created by akhmedovi on 11/19/16.
 * Copyright - 2016
 */

public class CreateEventWizardAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEMS = 4;

    public CreateEventWizardAdapter(FragmentManager fragmentManager) {
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
                return EventCreateWizardDateFragment.newInstance(1, res.getString(R.string.create_event_step_2), res.getString(R.string.create_event_step_2_hint));
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
        return "Step " + position + 1;
    }

}