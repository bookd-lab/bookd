package bookdlab.bookd.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import bookdlab.bookd.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 12/4/16.
 * Copyright - 2015
 */

public class TagItemView extends RelativeLayout {

    String title;
    boolean done = false;

    @BindView(R.id.title)
    TextView titleTV;
    @BindView(R.id.icon)
    ImageView icon;

    public TagItemView(Context context, String title, boolean done) {
        super(context);

        this.title = title;
        this.done = done;

        LayoutInflater.from(context).inflate(R.layout.event_tag_item_view, this, true);
        ButterKnife.bind(this);

        setData();
    }


    private void setData() {
        titleTV.setText(title);
        icon.setImageResource(done ? R.drawable.done : R.drawable.close_red);
    }

    public void setDone(boolean done) {
        this.done = done;
        setData();
    }
}
