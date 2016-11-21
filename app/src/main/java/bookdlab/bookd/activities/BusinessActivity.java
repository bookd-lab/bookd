package bookdlab.bookd.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.database.Queries;
import bookdlab.bookd.fragments.EventListFragment;
import bookdlab.bookd.fragments.ReviewsFragment;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.Review;
import bookdlab.bookd.views.ReviewItemViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
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
    @BindView(R.id.ivMapView)
    ImageView ivMapView;
    @BindView(R.id.btnShowReviews)
    Button showReviewsButton;
    @BindView(R.id.aboutTV)
    TextView aboutTV;
    @BindView(R.id.subtitle)
    TextView subtitleTV;
    @BindView(R.id.featuredReviewContaner)
    View featuredReviewContaner;

    Business businessData;
    private ReviewsFragment reviewsFragment;

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
        showReviewsButton.setOnClickListener((v) -> showMoreReviews());
        startAlphaAnimation(toolbarTitle, 0, View.INVISIBLE);

        businessData = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_BUSINESS));

        initData();
        loadFeaturedReview();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void initData() {
        String address = businessData.getLatitude() + "," + businessData.getLongitude();
        String mapUrl = "https://maps.google.com/maps/api/staticmap?center=" + address + "&zoom=16&size=600x400&sensor=true&markers=color:red%7C\"" + address + "\"&key=" + BookdApplication.MAP_API_KEY;

        Glide.with(this).load(businessData.getImageURL()).into(headerImageView);
        Glide.with(this).load(businessData.getLogoURL()).into(businessCircleIV);
        Glide.with(this).load(mapUrl).into(ivMapView);

        toolbarTitle.setText(businessData.getName());
        businessNameTV.setText(businessData.getName());
        subtitleTV.setText(businessData.getCity());
        aboutTV.setText(businessData.getAbout());
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
            case R.id.menu_add:{
                //TODO: Display fragment with list of in-progress events, on selection add Business to Event
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.add(EventListFragment.newInstance(), null);
                ft.commit();
                return true;
            }
            /*case R.id.menu_watch: {
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
            }*/
            case R.id.menu_call: {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + businessData.getPhone()));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
                startActivity(intent);
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

    public void showMoreReviews() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        reviewsFragment = ReviewsFragment.newInstance(businessData);
        ft.replace(R.id.fragmentPlaceholder, reviewsFragment);
        ft.commit();

        showReviewsButton.setVisibility(View.GONE);
    }

    public void loadFeaturedReview() {
        ReviewItemViewHolder h = new ReviewItemViewHolder(featuredReviewContaner);

        new Queries().getFeaturedReviewForBusiness(businessData.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren().iterator().hasNext()) {
                    Review review = dataSnapshot.getChildren().iterator().next().getValue(Review.class);
                    h.reviewBody.setText(review.getReviewBody());
                    h.reviewDate.setText(review.getReviewDate());
                    h.reviewer.setText(review.getReviewerId());
                    h.ratingBar.setRating(review.getStarRating().floatValue());

                    Glide.with(BusinessActivity.this)
                            .load(review.getReviewerImgUrl())
                            .into(h.reviewerImage);

                } else {
                    featuredReviewContaner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
