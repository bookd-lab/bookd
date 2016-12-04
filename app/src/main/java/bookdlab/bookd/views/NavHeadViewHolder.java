package bookdlab.bookd.views;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import bookdlab.bookd.R;
import bookdlab.bookd.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 12/3/16.
 * Copyright - 2016
 */

public class NavHeadViewHolder {

    private final View view;

    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.tvMemberSince)
    TextView tvMemberSince;
    @BindView(R.id.ivUserProfileImage)
    ImageView ivUserProfileImage;

    public NavHeadViewHolder(View view) {
        ButterKnife.bind(this, view);
        this.view = view;
    }

    public void setData(User user) {
        Context context = view.getContext();

        String memberSince = "Member since " + user.getMemberSince();
        if (null != user.getUsername()) {
            tvUsername.setText(user.getUsername());
        } else {
            tvUsername.setText(R.string.unknown);
        }

        tvMemberSince.setText(memberSince);
        tvEmail.setText(user.getEmail());
        Glide.with(context)
                .load(user.getProfileImageURL())
                .placeholder(R.drawable.ic_account_circle_black_48px)
                .into(ivUserProfileImage);
    }
}
