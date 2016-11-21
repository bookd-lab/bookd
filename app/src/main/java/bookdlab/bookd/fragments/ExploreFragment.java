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
import android.view.WindowManager;
import android.widget.Button;

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
import java.util.List;
import java.util.Locale;
import java.util.Random;

import bookdlab.bookd.R;
import bookdlab.bookd.adapters.BusinessAdapter;
import bookdlab.bookd.database.Queries;
import bookdlab.bookd.database.QueryHelper;
import bookdlab.bookd.helpers.AnimUtils;
import bookdlab.bookd.interfaces.NearbyBusinessCallback;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.ui.SearchEditText;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class ExploreFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private List<Business> businessList;
    private BusinessAdapter businessAdapter;

    @BindView(R.id.businessRV)
    RecyclerView recyclerView;
    @BindView(R.id.searchEdt)
    SearchEditText searchEdt;
    @BindView(R.id.advancedSearch)
    View advancedSearch;
    @BindView(R.id.searchButton)
    Button searchButton;
    @BindView(R.id.loadingIndicatorView)
    AVLoadingIndicatorView loadingIndicatorView;

    private GoogleApiClient mGoogleApiClient;
    private static final String TAG = "ExploreFragment";
    private Location lastLocationFetched;
    private Random rand;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rand = new Random();
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

        //remove initial focus on edit text
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        searchEdt.setOnClickListener((v) -> AnimUtils.fadeIn(advancedSearch));

        searchButton.setOnClickListener((v -> {
            //remove initial focus on edit text
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            AnimUtils.fadeOut(advancedSearch);
            //TODO: perform search
        }));

        return view;
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

        lastLocationFetched = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocationFetched != null) {
            fetchDataByLocation();
            QueryHelper.getBusinessesInArea(lastLocationFetched.getLatitude(), lastLocationFetched.getLongitude(), 3.0, new NearbyBusinessCallback() {
                @Override
                public void onNearbyBusinessesFound(ArrayList<Business> businesses) {
                    Log.d(TAG, "onNearbyBusinessesFound: business size: "+businesses.size());
                    for(Business business: businesses){
                        Log.d(TAG, "onNearbyBusinessesFound: "+business);
                    }
                }
            });
        } else {
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
            List<Address> addresses = geo.getFromLocation(lastLocationFetched.getLatitude(), lastLocationFetched.getLongitude(), 1);
            for (Address address : addresses) {
                Log.d(TAG, "onConnected: address is: " + address.getLocality());
                queryBusinesses(address.getLocality());
            }
        } catch (IOException e) {
            Log.e(TAG, "Exception occurred in onConnected: ", e);
            e.printStackTrace();
        }
    }

    private void queryBusinesses(String locality) {
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
