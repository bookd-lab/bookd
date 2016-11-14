package bookdlab.bookd.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import bookdlab.bookd.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 11/12/16.
 * Copyright - 2016
 */

/**
 * Since {@link RecyclerView.ViewHolder} is an abstract class
 */
public class DefaultViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rootView)
    public
    ViewGroup rootView;

    public DefaultViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
