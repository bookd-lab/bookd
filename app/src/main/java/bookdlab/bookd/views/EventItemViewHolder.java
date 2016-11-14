package bookdlab.bookd.views;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import bookdlab.bookd.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 10/30/16.
 * Copyright - 2016
 */

public class EventItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.eventTitle)
    public TextView eventTitle;
    @BindView(R.id.datesInfo)
    public TextView datesInfo;
    @BindView(R.id.backgroundOverlay)
    public View backgroundOverlay;
    @BindView(R.id.rootView)
    public View rootView;
    @BindView(R.id.bottomDivider)
    public View bottomDivider;

    public EventItemViewHolder(View itemView) {
        super(itemView);

        ButterKnife.bind(this, itemView);
    }
}
