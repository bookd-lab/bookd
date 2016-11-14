package bookdlab.bookd.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import bookdlab.bookd.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2016
 */

public class BusinessItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rootView)
    public ViewGroup rootView;
    @BindView(R.id.businessImage)
    public ImageView businessImage;

    public BusinessItemViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
