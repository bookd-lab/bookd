package bookdlab.bookd.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.AddBusinessAdapter;
import bookdlab.bookd.fragments.ExploreFragment;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pranavkonduru on 12/3/16.
 */

public class BookBusinessActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    @BindView(R.id.viewPager) ViewPager viewPager;

    private ArrayList<Fragment> fragments = new ArrayList<>();
    private ArrayList<String> tags = new ArrayList<>();
    private Bundle extras;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_businesses);
        ButterKnife.bind(this);

        extras = getIntent().getExtras();

        tags = extras.getStringArrayList("tags");
        String eventName = extras.getString("eventName");

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(eventName);

        if (tags != null) {
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
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
