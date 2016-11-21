package bookdlab.bookd.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.BusinessAdapter;
import bookdlab.bookd.database.Queries;
import bookdlab.bookd.database.QueryHelper;
import bookdlab.bookd.helpers.AnimUtils;
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
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final Double DEFAULT_RADIUS = 30.0;
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

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "ExploreFragment";
    private Location lastLocationFetched;
    private Address lastAddress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lastLocationFetched = new Location("");
        lastLocationFetched.setLatitude(37.4530);
        lastLocationFetched.setLongitude(122.1817);
        lastAddress = new Address(Locale.US);
        lastAddress.setLocality("Menlo Park");

        initGoogleLocationApi();
    }

    public static ExploreFragment newInstance() {
        ExploreFragment fragment = new ExploreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        ButterKnife.bind(this, view);

        businessList = new ArrayList<>();
        businessAdapter = new BusinessAdapter(getActivity(), businessList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
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

        return view;
    }

    private void hideSearchUI() {
        UIUtils.hideSoftInput(getActivity());
        AnimUtils.fadeOut(advancedSearch);
    }

    private void performSearch() {
        hideSearchUI();
        queryBusinesses(lastAddress.getLocality());
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
            fetchDataByLocation();
            return;
        }

        lastLocationFetched = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocationFetched != null) {
            fetchDataByLocation();

            QueryHelper.getBusinessesInArea(lastLocationFetched.getLatitude(), lastLocationFetched.getLongitude(), DEFAULT_RADIUS, businesses -> {
                Log.d(TAG, "onNearbyBusinessesFound: business size: " + businesses.size());
                for (Business business : businesses) {
                    Log.d(TAG, "onNearbyBusinessesFound: " + business);
                }
            });
        } else {
            Log.i(TAG, "Requesting for location update!");
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(60 * 60 * 1000L);
            locationRequest.setFastestInterval(60 * 60 * 1000L);

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);
        }
    }

    private void fetchDataByLocation() {
        Geocoder geo = new Geocoder(this.getActivity().getApplicationContext(), Locale.getDefault());
        try {

            if (lastAddress == null) {
                //TODO: This is a network call, has to be taken out of here
                List<Address> addresses = geo.getFromLocation(lastLocationFetched.getLatitude(), lastLocationFetched.getLongitude(), 1);
                lastAddress = addresses.get(0);
                if (addresses.isEmpty()) {
                    return;
                }
            }

            queryBusinesses(lastAddress.getLocality());
        } catch (IOException e) {
            Log.e(TAG, "Exception occurred in onConnected: ", e);
            e.printStackTrace();
        }
    }

    private void queryBusinesses(String locality) {
        recyclerView.setVisibility(View.GONE);
        loadingIndicatorView.setVisibility(View.VISIBLE);
        loadingIndicatorView.show();

        Queries queries = new Queries();
        queries.getBusinessInArea(locality).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                if (!children.iterator().hasNext()) {
                    Log.d(TAG, "No businesses in this locality");
                } else {
                    Log.d(TAG, "Businesses found in locality");
                    businessList.clear();

                    for (DataSnapshot child : children) {
                        Business business = child.getValue(Business.class);
                        businessList.add(business);
                    }

                    List<Business> newList = new ArrayList<>();
                    for (Business b : businessList) {
                        if (advancedSearchContent.getRating() <= b.getRating() && advancedSearchContent.getPrice() >= b.getPrice()) {
                            newList.add(b);
                        }
                    }

                    businessList.clear();
                    businessList.addAll(newList);

                    Collections.sort(businessList, (o1, o2) -> {
                        if (advancedSearchContent.getSortByField() == AdvancedSearchView.SortByField.PRICE) {
                            return o2.getPrice() - o1.getPrice();
                        }

                        return o2.getRating() - o1.getRating() > 0 ? 1 : -1;
                    });


                    businessAdapter.notifyDataSetChanged();

                    loadingIndicatorView.hide();
                    recyclerView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "Connection to Google API suspended. Querying in Menlo Park");
        queryBusinesses("Menlo Park");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection to Google API failed. Querying in Menlo Park");
        queryBusinesses("Menlo Park");
    }


    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location has been changed");
        lastLocationFetched = location;
        fetchDataByLocation();
    }
}
