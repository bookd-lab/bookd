package bookdlab.bookd.views;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import bookdlab.bookd.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 12/3/16.
 * Copyright - 2016
 */

public class NavHeadViewHolder {

    @BindView(R.id.tvUsername)
    public TextView tvUsername;
    @BindView(R.id.tvEmail)
    public TextView tvEmail;
    @BindView(R.id.tvMemberSince)
    public TextView tvMemberSince;
    @BindView(R.id.ivUserProfileImage)
    public ImageView ivUserProfileImage;

    public NavHeadViewHolder(View view) {
        ButterKnife.bind(this, view);
    }
}
