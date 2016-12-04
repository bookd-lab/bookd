package bookdlab.bookd.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;

import org.parceler.Parcels;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.Constants;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.CreateEventWizardAdapter;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.interfaces.WizardNavigator;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;


public class EventCreateActivity extends AppCompatActivity implements WizardNavigator {
    private static final String TAG = "EventCreateActivity";

    public static final int CREATE_EVENT_REQUEST = 1002;
    public static final String EVENT_EXTRA = "EVENT_EXTRA";

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private FragmentPagerAdapter adapterViewPager;
    private Event event;

    @Inject
    BookdApiClient bookdApiClient;

    private AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        ButterKnife.bind(this);
        ((BookdApplication) getApplication()).getAppComponent().inject(this);

        adapterViewPager = new CreateEventWizardAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapterViewPager);
        viewPager.setPageTransformer(true, new CubeOutTransformer());

        if (null != getIntent().getParcelableExtra(EVENT_EXTRA)) {
            event = Parcels.unwrap(getIntent().getParcelableExtra(EVENT_EXTRA));
        } else {
            event = new Event();
            event.setCreator(BookdApplication.getCurrentUser().getId());
        }
    }

    @Override
    public void onNext() {
        int nextPos = viewPager.getCurrentItem() + 1;

        if (nextPos < adapterViewPager.getCount()) {
            viewPager.setCurrentItem(nextPos);
        } else {
            bookBusinessesForEvent();
            adapterViewPager.notifyDataSetChanged();
        }
    }

    @Override
    public void onPrev() {
        int prevPos = viewPager.getCurrentItem() - 1;

        if (prevPos >= 0) {
            viewPager.setCurrentItem(prevPos);
        }
    }

    public Event getEvent() {
        return event;
    }

    private void bookBusinessesForEvent() {

        /*loadingDialog = new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.creating_event))
                .setCancelable(false)
                .show();*/

        /*Bundle bundle = new Bundle();
        bundle.putStringArrayList(Constants.EXTRA_EVENT_TAGS, event.getTags());
        bundle.putString(Constants.EXTRA_EVENT_NAME, event.getName());*/

        Intent intent = new Intent(EventCreateActivity.this, BookBusinessActivity.class);
        intent.putExtra(Constants.EXTRA_EVENT, Parcels.wrap(Event.class, event));
//        intent.putExtras(bundle);
        startActivity(intent);

        /*bookdApiClient.saveEvent(event).enqueue(new Callback<Event>() {
            @Override
            public void onResponse(Call<Event> call, Response<Event> response) {
                loadingDialog.dismiss();

                if (!response.isSuccessful()) {
                    Log.d(TAG, response.message());
                    showErrorDialog();
                    return;
                }

                //finish as result to the calling activity
                *//*setResult(RESULT_OK);
                finish();*//*

            }

            @Override
            public void onFailure(Call<Event> call, Throwable t) {
                loadingDialog.dismiss();

                showErrorDialog();
            }
        });*/
    }

    private void showErrorDialog() {
        new AlertDialog.Builder(EventCreateActivity.this)
                .setMessage(R.string.check_network_conn)
                .setPositiveButton(R.string.try_again, (dialog, which) -> bookBusinessesForEvent())
                .show();
    }
}