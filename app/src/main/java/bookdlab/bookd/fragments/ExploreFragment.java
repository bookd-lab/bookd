package bookdlab.bookd.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.BusinessAdapter;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.helpers.AnimUtils;
import bookdlab.bookd.helpers.EndlessRecyclerViewScrollListener;
import bookdlab.bookd.interfaces.EventAware;
import bookdlab.bookd.interfaces.EventProvider;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.ui.SearchEditText;
import bookdlab.bookd.ui.UIUtils;
import bookdlab.bookd.views.AdvancedSearchView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class ExploreFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, EventAware {

    private static final Double DEFAULT_RADIUS = 30.0;
    private static final Integer PAGE_SIZE = 20;
    private List<Business> businessList;
    private BusinessAdapter businessAdapter;

    @BindView(R.id.businessRV)
    RecyclerView recyclerView;
    @BindView(R.id.searchEdt)
    SearchEditText searchEdt;
    @BindView(R.id.advancedSearch)
    View advancedSearch;
    @BindView(R.id.advancedSearchContent)
    AdvancedSearchView advancedSearchContent;
    @BindView(R.id.searchButton)
    Button searchButton;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;
    @BindView(R.id.sortByRadioGroup)
    RadioGroup sortByRadioGroup;
    @BindView(R.id.byRating)
    RadioButton byRating;

    @Inject
    BookdApiClient bookdApiClient;

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "ExploreFragment";
    private Location lastLocationFetched;
    private EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener;
    private String searchQuery = "";
    public static final String EXTRA_SEARCH_QUERY = "extra_search_query";
    private EventProvider eventProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((BookdApplication)getActivity().getApplication()).getAppComponent().inject(this);

        lastLocationFetched = new Location("");
        lastLocationFetched.setLatitude(37.4530);
        lastLocationFetched.setLongitude(122.1817);

        initGoogleLocationApi();

        Bundle bundle = this.getArguments();
        if(bundle != null){
            searchQuery = bundle.getString(EXTRA_SEARCH_QUERY);
        }
    }

    public static ExploreFragment newInstance(Bundle args) {
        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, view);

        businessList = new ArrayList<>();
        businessAdapter = new BusinessAdapter(getActivity(), businessList, eventProvider.getEvent(), bookdApiClient);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(businessAdapter);

        recyclerView.setVisibility(View.GONE);
        loadingIndicatorView.show();

        byRating.setChecked(true);

        searchEdt.setListener(new SearchEditText.InteractionListener() {
            @Override
            public void onClose() {
                hideSearchUI();
            }

            @Override
            public void onOpen() {
                AnimUtils.fadeIn(advancedSearch);
            }
        });

        advancedSearchContent.setListener(this::performSearch);
        UIUtils.hideSoftInput(getActivity());

        endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                queryBusinesses(page + 1);
            }
        };

        recyclerView.addOnScrollListener(endlessRecyclerViewScrollListener);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Handler().postDelayed(() -> UIUtils.hideSoftInput(getActivity()), 200);
    }

    private void hideSearchUI() {
        UIUtils.hideSoftInput(getActivity());
        AnimUtils.fadeOut(advancedSearch);
    }

    private void performSearch() {
        hideSearchUI();
        queryBusinesses(1);
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    private void initGoogleLocationApi() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this.getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        FragmentActivity ctx = getActivity();
        if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(ctx, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.e(TAG, "Location couldn't be retrieved! Check permissions!");
            return;
        }

        if (null != lastLocationFetched) {
            queryBusinesses(1);
            return;
        }

        lastLocationFetched = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocationFetched != null) {
            queryBusinesses(1);
        } else {
            Log.i(TAG, "Requesting for location update!");
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(60 * 60 * 1000L);
            locationRequest.setFastestInterval(60 * 60 * 1000L);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    private void queryBusinesses(int page) {
        if (page == 1) {
            businessList.clear();
            businessAdapter.notifyDataSetChanged();
            endlessRecyclerViewScrollListener.resetState();
            recyclerView.setVisibility(View.GONE);
            loadingIndicatorView.setVisibility(View.VISIBLE);
            loadingIndicatorView.show();
        }

        int priceMax = advancedSearchContent.getPrice();
        double ratingMin = advancedSearchContent.getRating();
        String querySortBy = advancedSearchContent.getSortByField().name();

        bookdApiClient.getBusinesses(getSearchQuery(), priceMax, ratingMin, page, 20, querySortBy)
                .enqueue(new Callback<List<Business>>() {
                    @Override
                    public void success(Result<List<Business>> result) {
                        businessList.addAll(result.data);
                        businessAdapter.notifyDataSetChanged();

                        loadingIndicatorView.hide();
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.e(TAG, exception.getMessage(), exception);
                        loadingIndicatorView.hide();
                    }
                });
    }

    private String getSearchQuery() {
        String searchKey;
        if(!TextUtils.isEmpty(searchQuery)){
            searchKey = searchQuery;
            searchEdt.setEditable(false);
        } else {
            searchKey = searchEdt.getQuery();
            searchEdt.setEditable(true);
        }
        return searchKey;
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection to Google API suspended. Querying in Menlo Park");
        queryBusinesses(1);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection to Google API failed. Querying in Menlo Park");
        queryBusinesses(1);
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location has been changed");
        lastLocationFetched = location;
        queryBusinesses(1);
    }


    @Override
    public void onAttach(Context context) {
        if (!(context instanceof EventProvider)) {
            throw new ClassCastException("context has to implement " + EventProvider.class.getName());
        }

        super.onAttach(context);
        this.eventProvider = (EventProvider) context;
        this.eventProvider.addEventAware(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.eventProvider.removeEventAware(this);
        this.eventProvider = null;
    }

    @Override
    public void updateEventData() {
        businessAdapter.setEvent(eventProvider.getEvent());
        businessAdapter.notifyDataSetChanged();
    }
}
