package bookdlab.bookd.activities;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.parceler.Parcels;

import bookdlab.bookd.R;
import bookdlab.bookd.models.Business;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/12/2016.
 */

public class BusinessActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    private static final String EXTRA_BUSINESS = "EXTRA_BUSINESS";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    @BindView(R.id.profile_toolbar)
    Toolbar toolbar;
    @BindView(R.id.profile_textview_title)
    TextView titleTV;
    @BindView(R.id.profile_linearlayout_title)
    LinearLayout titleContainer;
    @BindView(R.id.profile_appbar)
    AppBarLayout appBarLayout;

    private Business business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        business = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_BUSINESS));

        appBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(titleTV, 0, View.INVISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_business, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                break;
            }
            case R.id.menu_watch: {
                if (item.getItemId() == R.drawable.button_add_watch) {
                    item.setIcon(R.drawable.button_remove_watch);
                    addToEventWatchList();
                    return true;
                } else if (item.getItemId() == R.drawable.button_remove_watch) {
                    item.setIcon(R.drawable.button_add_watch);
                    removeFromEventWatchList();
                    return true;
                }
                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public static void addToEventWatchList() {
    }

    public static void removeFromEventWatchList() {
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int offset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(offset) / (float) maxScroll;

        handleAlphaOnTitle(percentage);
        handleToolbarTitleVisibility(percentage);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if (!mIsTheTitleVisible) {
                startAlphaAnimation(titleTV, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(titleTV, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if (mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(titleContainer, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation(View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }
}
