package bookdlab.bookd.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.raizlabs.android.dbflow.StringUtils;

import org.parceler.Parcels;

import java.util.List;

import bookdlab.bookd.R;
import bookdlab.bookd.activities.BusinessActivity;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.ui.UIUtils;
import bookdlab.bookd.views.BusinessItemViewHolder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

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
        Business business = businessList.get(position);

        Glide.with(activity)
                .load(business.getImageURL())
                .bitmapTransform(new RoundedCornersTransformation(activity, 15, 0))
                .into(h.businessImage);

        h.rootView.setOnClickListener((v -> {

            Intent intent = new Intent(activity, BusinessActivity.class);
            intent.putExtra(BusinessActivity.EXTRA_BUSINESS, Parcels.wrap(business));
            activity.startActivity(intent);

        }));

        h.title.setText(business.getName());
        h.address.setText(business.getAddress());
        h.priceLevel.setText(business.formPriceLabel());
        h.rating.setCount((int) business.getRating());

        if (StringUtils.isNotNullOrEmpty(business.getPhone())) {
            CharSequence phone = UIUtils.embedImage(activity, business.getPhone(), R.drawable.phone_green, 0.5);
            h.phone.setText(phone);
            h.phone.setVisibility(View.VISIBLE);
        } else {
            //other views might be adjusting, so not making GONE
            h.phone.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }
}
