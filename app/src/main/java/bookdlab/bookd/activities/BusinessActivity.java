package bookdlab.bookd.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.fragments.ReviewsFragment;
import bookdlab.bookd.models.Business;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by rubab.uddin on 11/12/2016.
 */

public class BusinessActivity extends AppCompatActivity
        implements AppBarLayout.OnOffsetChangedListener {

    public static final String EXTRA_BUSINESS = "EXTRA_BUSINESS";

    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS = 0.3f;
    private static final int ALPHA_ANIMATIONS_DURATION = 200;

    private boolean mIsTheTitleVisible = false;
    private boolean mIsTheTitleContainerVisible = true;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarTitle)
    TextView toolbarTitle;

    @BindView(R.id.businessNameTV)
    TextView businessNameTV;
    @BindView(R.id.businessTitleContainer)
    LinearLayout titleContainer;
    @BindView(R.id.profileAppBar)
    AppBarLayout appBarLayout;
    @BindView(R.id.headerBackgroundImage)
    ImageView headerImageView;
    @BindView(R.id.businessCircleIV)
    ImageView businessCircleIV;
    @BindView(R.id.ivMapView) ImageView ivMapView;
    @BindView(R.id.btnShowReviews)
    Button showReviewsButton;

    Business businessData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }

        appBarLayout.addOnOffsetChangedListener(this);
        startAlphaAnimation(toolbarTitle, 0, View.INVISIBLE);

        businessData = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_BUSINESS));
        setData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void setData() {

        //String address = businessData.getAddress();
        String address = "185 Park Avenue, San Jose, CA";
        String mapUrl = "https://maps.google.com/maps/api/staticmap?center=\""+ address + "\"&zoom=16&size=600x400&sensor=true&markers=color:red%7C\"" + address + "\"&key=" + BookdApplication.MAP_API_KEY;

        Glide.with(this).load(businessData.getImageURL()).into(headerImageView);
        Glide.with(this).load(businessData.getLogoURL()).into(businessCircleIV);
        Glide.with(this).load(mapUrl).into(ivMapView);

        toolbarTitle.setText(businessData.getName());
        businessNameTV.setText(businessData.getName());
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
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(toolbarTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
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

    @OnClick(R.id.btnShowReviews)
    public void showMoreReviews(){
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
// Replace the contents of the container with the new fragment
        ft.replace(R.id.fragmentPlaceholder, new ReviewsFragment());
// or ft.add(R.id.your_placeholder, new FooFragment());
// Complete the changes added above
        ft.commit();
    }
}
