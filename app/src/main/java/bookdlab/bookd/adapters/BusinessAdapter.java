package bookdlab.bookd.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.List;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.activities.BusinessActivity;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.interfaces.EventProvider;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.Event;
import bookdlab.bookd.models.Favorite.Favorite;
import bookdlab.bookd.views.BusinessItemViewHolder;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2016
 */

public class BusinessAdapter extends RecyclerView.Adapter<BusinessItemViewHolder> {
    private static final String TAG = BusinessAdapter.class.getSimpleName();

    private BookdApiClient bookdApiClient;
    private Activity activity;
    private List<Business> businessList;
    private EventProvider eventProvider;

    public BusinessAdapter(Activity activity, List<Business> businessList, EventProvider eventProvider, BookdApiClient bookdApiClient) {
        this.businessList = businessList;
        this.activity = activity;
        this.eventProvider = eventProvider;
        this.bookdApiClient = bookdApiClient;
    }

    @Override
    public BusinessItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.list_item_business, parent, false);
        return new BusinessItemViewHolder(view);
    }

    public void setEventProvider(EventProvider eventProvider) {
        this.eventProvider = eventProvider;
    }

    @Override
    public void onBindViewHolder(BusinessItemViewHolder h, int position) {
        Business business = businessList.get(position);

        Glide.with(activity)
                .load(business.getImageURL())
                .bitmapTransform(new RoundedCornersTransformation(activity, 15, 0))
                .override(200, 200)
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

        if (BookdApplication.getFavorites().containsKey(business.getId())) {
            h.ivFavorite.setImageResource(R.drawable.ic_favorite_red);
            h.ivFavorite.setOnClickListener(v -> {
                BookdApplication.getFavorites().remove(business.getId());
                eventProvider.notifyEventAware();
            });
        } else {
            h.ivFavorite.setImageResource(R.drawable.ic_favorite);
            h.ivFavorite.setOnClickListener(v -> {
                Favorite fav = new Favorite(BookdApplication.getCurrentUser().getId(), business.getId());
                BookdApplication.getFavorites().put(business.getId(), fav);
                bookdApiClient.addFavorite(fav).enqueue(getDefaultCallback());
                eventProvider.notifyEventAware();
            });
        }

        Event event = eventProvider.getEvent();

        if (event.getBusinesses().contains(business.getId())) {
            h.addButton.setImageResource(R.drawable.close_red);
            h.addButton.setOnClickListener(v -> {
                event.getBusinesses().remove(business.getId());
                bookdApiClient.updateEvent(event).enqueue(getDefaultCallback());
                eventProvider.notifyEventAware();
            });
        } else {
            h.addButton.setImageResource(R.drawable.add);
            h.addButton.setOnClickListener(v -> {
                event.getBusinesses().add(business.getId());
                bookdApiClient.updateEvent(event).enqueue(getDefaultCallback());
                eventProvider.notifyEventAware();
            });
        }
    }

    @Override
    public int getItemCount() {
        return businessList.size();
    }

    private <T> Callback<T> getDefaultCallback() {
        //TODO: ignore for now, but should add snack bar with try again...
        return new Callback<T>() {
            @Override
            public void onResponse(Call<T> call, Response<T> response) {
                Log.d(TAG, "Add/Remove business to event success: " + response.isSuccessful());
            }

            @Override
            public void onFailure(Call<T> call, Throwable t) {
                Log.d(TAG, t.getMessage());
            }
        };
    }
}
