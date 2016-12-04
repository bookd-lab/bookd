package bookdlab.bookd.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by pranavkonduru on 12/3/16.
 */

public class AddBusinessAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> businessTypes;
    private ArrayList<String> categories;
    private Context context;
    public static int pos = 0;

    public AddBusinessAdapter(Context context, ArrayList<Fragment> businesses, ArrayList<String> categories, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.businessTypes = businesses;
        this.categories = categories;
    }

    @Override
    public Fragment getItem(int position) {
        return businessTypes.get(position);
    }

    @Override
    public int getCount() {
        return businessTypes.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        setPos(position);
        return categories.get(position);
    }

    public int getPos() {
        return pos;
    }

    public static void setPos(int pos) {
        AddBusinessAdapter.pos = pos;
    }
}
