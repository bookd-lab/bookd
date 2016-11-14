package bookdlab.bookd.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.activities.ProfileActivity;
import bookdlab.bookd.adapters.BusinessAdapter;
import bookdlab.bookd.api.BusinessesClient;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.uihelpers.ItemClickSupport;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class ExploreFragment extends Fragment {

    private List<Business> businessList;
    private BusinessAdapter businessAdapter;

    @BindView(R.id.businessRV)
    RecyclerView recyclerView;

    //TODO: inject properly
    BusinessesClient businessesClient = new BusinessesClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        setupOnClickListener();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Business> res = businessesClient.findBusinesses("");
        businessList.addAll(res);
        businessAdapter.notifyDataSetChanged();
    }

    private void setupOnClickListener(){
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(
                new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Business business = businessList.get(position);
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        //intent.putExtra("businessId", Parcels.wrap(business));
                        startActivity(intent);
                    }
                });


    }
}
