package bookdlab.bookd.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
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

import com.bumptech.glide.Glide;

import java.util.List;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.HomeTabsAdapter;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.interfaces.EventAware;
import bookdlab.bookd.interfaces.EventProvider;
import bookdlab.bookd.models.Event;
import bookdlab.bookd.models.User;
import bookdlab.bookd.ui.UIUtils;
import bookdlab.bookd.views.NavHeadViewHolder;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements EventProvider {
    private static final String TAG = MainActivity.class.getSimpleName();

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
    private Event currentEvent;

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
        //to avoid reloading
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        setupProfileInfo();
        fetchEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (0 <= item.getItemId() && item.getItemId() < eventList.size()) {
            //bug fix for issue https://guides.codepath.com/android/Fragment-Navigation-Drawer#limitations
            for (int i = 0; i < eventList.size(); i++) {
                navView.getMenu().findItem(i).setChecked(false);
            }

            currentEvent = eventList.get(item.getItemId());
            item.setChecked(true);
            updateEventData();
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

        switch (item.getItemId()) {
            case R.id.create_event:
                openCreateEvent();
                return true;
            case R.id.logout:
                signOut();
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
        NavHeadViewHolder navHeadViewHolder = new NavHeadViewHolder(navView.getHeaderView(0));
        navHeadViewHolder.setData(BookdApplication.getCurrentUser());
    }

    private void fetchEvents() {
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
                .setPositiveButton(R.string.try_again, (dialog, which) -> fetchEvents())
                .show();
    }

    private void setupMenu() {
        navView.getMenu().clear();
        navView.inflateMenu(R.menu.drawer_view);

        currentEvent = null;
        if (!eventList.isEmpty()) {
            currentEvent = eventList.get(0);
            for (int i = 0; i < eventList.size(); i++) {
                MenuItem menuItem = navView.getMenu().add(R.id.events_group, i, Menu.NONE, eventList.get(i).getName());
                menuItem.setChecked(i == 0);
            }
        }

        updateEventData();
        navView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
        navView.invalidate();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EventCreateActivity.CREATE_EVENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                fetchEvents();
                return;
            }

            Log.e(TAG, "Failed to create event...");
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openCreateEvent() {
        startActivityForResult(new Intent(this, EventCreateActivity.class),
                EventCreateActivity.CREATE_EVENT_REQUEST);
    }

    private void signOut() {
        BookdApplication.logout();
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @SuppressWarnings("all")
    private void updateEventData() {
        for (EventAware eventAware : this.eventAwareList) {
            eventAware.updateEventData();
        }
    }

    @Override
    public Event getEvent() {
        return currentEvent;
    }

    @Override
    public void addEventAware(EventAware eventAware) {
        eventAwareList.add(eventAware);
    }

    @Override
    public void removeEventAware(EventAware eventAware) {
        eventAwareList.remove(eventAware);
    }
}
