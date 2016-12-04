package bookdlab.bookd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.HomeTabsAdapter;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.models.Event;
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

    @Inject
    BookdApiClient bookdApiClient;

    private List<Event> eventList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate");

        ButterKnife.bind(this);
        ((BookdApplication) getApplication()).getAppComponent().inject(this);

        viewPager.setAdapter(new HomeTabsAdapter(this, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        loadEvents();
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
