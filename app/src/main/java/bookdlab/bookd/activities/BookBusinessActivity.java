package bookdlab.bookd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.parceler.Parcels;

import java.util.ArrayList;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.Constants;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.AddBusinessAdapter;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.fragments.ExploreFragment;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pranavkonduru on 12/3/16.
 */

public class BookBusinessActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    @Inject
    BookdApiClient bookdApiClient;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    private Event event;
    private static final String TAG = "BookBusinessActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_businesses);
        ButterKnife.bind(this);

        ((BookdApplication) getApplication()).getAppComponent().inject(this);
//        Bundle extras = getIntent().getExtras();

//        tags = extras.getStringArrayList(Constants.EXTRA_EVENT_TAGS);
//        String eventName = extras.getString(Constants.EXTRA_EVENT_NAME);

        event = Parcels.unwrap(getIntent().getParcelableExtra(Constants.EXTRA_EVENT));
        tags = event.getTags();
        String eventName = event.getName();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(eventName);

        if(tags == null) {
            tags = new ArrayList<>();
        }

        if(tags.size() == 0){
            tags.add("Food");
            tags.add("Bar");
        }

        if (tags.size() > 0) {
            for(String tag: tags){
                Bundle bundle = new Bundle();
                bundle.putString(ExploreFragment.EXTRA_SEARCH_QUERY, tag);
                fragments.add(0, ExploreFragment.newInstance(bundle));
            }
        }

        viewPager.setAdapter(new AddBusinessAdapter(this, fragments, tags, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_booked_business, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_save:
                Log.d(TAG, "onOptionsItemSelected: Save clicked");
                saveAndFinish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveAndFinish() {
        AlertDialog loadingDialog = new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.creating_event))
                .setCancelable(false)
                .show();

        bookdApiClient.saveEvent(event).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                loadingDialog.dismiss();

                if (!response.isSuccessful()) {
                    Log.d(TAG, response.message());
                    showErrorDialog();
                    return;
                }

                //finish as result to the calling activity
                /*setResult(RESULT_OK);*/
                Log.d(TAG, "onResponse: Saved the event to databse");
                gotoMainActivity();
                finish();

            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                loadingDialog.dismiss();
                Log.e(TAG, "onFailure: Failure in saving event", t);
                showErrorDialog();
            }
        });
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(BookBusinessActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        BookBusinessActivity.this.startActivity(intent);
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(BookBusinessActivity.this)
                .setMessage(R.string.check_network_conn)
                .setPositiveButton(R.string.try_again, (dialog, which) -> saveAndFinish())
                .show();
    }
}
