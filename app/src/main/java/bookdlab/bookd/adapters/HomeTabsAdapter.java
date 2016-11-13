package bookdlab.bookd.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import bookdlab.bookd.R;
import bookdlab.bookd.fragments.ExploreFragment;
import bookdlab.bookd.fragments.EventsFragment;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */

public class HomeTabsAdapter extends FragmentPagerAdapter {

    private Integer tabImages[] = new Integer[]{R.drawable.explore, R.drawable.event};
    private Context context;

    public HomeTabsAdapter(Context context, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return ExploreFragment.newInstance();
            case 1:
                return EventsFragment.newInstance();
        }

        return null;
    }

    @Override
    public int getCount() {
        return tabImages.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        Drawable image = ContextCompat.getDrawable(context, tabImages[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());

        SpannableString sb = new SpannableString(" ");

        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;
    }
}
