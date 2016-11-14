package bookdlab.bookd.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.activities.BusinessActivity;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.views.BusinessItemViewHolder;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2016
 */

public class BusinessAdapter extends RecyclerView.Adapter<BusinessItemViewHolder> {

    private Activity activity;
    private List<Business> businessList;

    public BusinessAdapter(Activity activity, List<Business> businessList) {
        this.businessList = businessList;
        this.activity = activity;
    }

    @Override
    public BusinessItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_business, parent, false);

        return new BusinessItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusinessItemViewHolder h, int position) {
        Glide.with(activity)
                .load(businessList.get(position).getImageUrl())
                .into(h.businessImage);

        h.rootView.setOnClickListener((v -> activity.startActivity(new Intent(activity, BusinessActivity.class))));
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }
}
