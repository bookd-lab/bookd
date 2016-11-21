package bookdlab.bookd.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import bookdlab.bookd.fragments.wizards.EventCreateWizard1Fragment;
import bookdlab.bookd.fragments.wizards.EventCreateWizard2Fragment;
import bookdlab.bookd.fragments.wizards.EventCreateWizard3Fragment;
import bookdlab.bookd.fragments.wizards.EventCreateWizard4Fragment;

/**
 * Created by akhmedovi on 11/19/16.
 * Copyright - 2016
 */

public class CreateEventWizardAdapter extends FragmentPagerAdapter {

    public CreateEventWizardAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EventCreateWizard1Fragment.newInstance();
            case 1:
                return EventCreateWizard2Fragment.newInstance();
            case 2:
                return EventCreateWizard3Fragment.newInstance();
            case 3:
                return EventCreateWizard4Fragment.newInstance();
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