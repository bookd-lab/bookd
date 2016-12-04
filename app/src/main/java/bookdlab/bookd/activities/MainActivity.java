package bookdlab.bookd.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.HomeTabsAdapter;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.models.Event;
import bookdlab.bookd.models.User;
import bookdlab.bookd.ui.UIUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int CREATE_EVENT = 5000;

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.navView)
    NavigationView navView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    @Inject
    BookdApiClient bookdApiClient;

    private List<Event> eventList;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        ButterKnife.bind(this);
        ((BookdApplication) getApplication()).getAppComponent().inject(this);

        setSupportActionBar(toolbar);
        if (null != getSupportActionBar()) {
            getSupportActionBar().setTitle("");
        }

        viewPager.setAdapter(new HomeTabsAdapter(this, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        drawerToggle = setupDrawerToggle();
        drawerLayout.addDrawerListener(drawerToggle);

        setupProfileInfo();
        loadEvents();

        new Handler().postDelayed(() -> UIUtils.hideSoftInput(this), 200);
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    private void setupProfileInfo() {
        View headerView = navView.getHeaderView(0);

        TextView tvUsername = (TextView) headerView.findViewById(R.id.tvUsername);
        TextView tvEmail = (TextView) headerView.findViewById(R.id.tvEmail);
        TextView tvMemberSince = (TextView) headerView.findViewById(R.id.tvMemberSince);
        ImageView ivUserProfileImage = (ImageView) headerView.findViewById(R.id.ivUserProfileImage);

        User user = BookdApplication.getCurrentUser();
        String memberSince = "Member since " + user.getEmail();
        if (null != user.getUsername()) {
            tvUsername.setText(user.getUsername());
        } else {
            tvUsername.setText(R.string.unknown);
        }

        tvMemberSince.setText(memberSince);
        tvEmail.setText(user.getEmail());

        Glide.with(this)
                .load(user.getProfileImageURL())
                .placeholder(R.drawable.ic_account_circle_black_48px)
                .into(ivUserProfileImage);
    }

    private void loadEvents() {
        bookdApiClient.getEvents(BookdApplication.getCurrentUser().getId(), null, null).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (!response.isSuccessful()) {
                    Log.d(TAG, response.errorBody().toString());
                    showErrorDialog();
                    return;
                }

                MainActivity.this.eventList = response.body();
                setupMenu();
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
                Log.d(TAG, t.getMessage());
                showErrorDialog();
            }
        });
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.check_network_conn)
                .setPositiveButton(R.string.try_again, (dialog, which) -> loadEvents())
                .show();
    }

    private void setupMenu() {
        navView.getMenu().clear();
        navView.inflateMenu(R.menu.drawer_view);

        for (int i = 0; i < eventList.size(); i++) {
            MenuItem menuItem = navView.getMenu().add(R.id.events_group, i, Menu.NONE, eventList.get(i).getName());
            menuItem.setChecked(i == 0);
        }

        navView.getMenu().add(R.id.events_group, CREATE_EVENT, Menu.NONE, R.string.create_event);
        navView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case CREATE_EVENT:
                    openCreateEvent();
                    break;
            }

            return false;
        });

        navView.invalidate();
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EventCreateActivity.CREATE_EVENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                loadEvents();
                return;
            }

            Log.e(TAG, "Failed to create event...");
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openCreateEvent() {
        startActivityForResult(new Intent(this, EventCreateActivity.class), EventCreateActivity.CREATE_EVENT_REQUEST);
    }
}
