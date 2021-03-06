package bookdlab.bookd.fragments;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseUser;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rubab.uddin on 11/19/2016.
 */
public class UserFragment extends Fragment {

    @BindView(R.id.tvUsername)
    TextView tvUsername;
    @BindView(R.id.tvMemberSince)
    TextView tvMemberSince;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.ivUserProfileImage)
    CircleImageView ivUserProfileImage;

    @BindView(R.id.vsAbout)
    ViewSwitcher vsAbout;
    @BindView(R.id.tvAbout)
    TextView tvAbout;
    @BindView(R.id.etAbout)
    EditText etAbout;

    @BindView(R.id.vsLocation)
    ViewSwitcher vsLocation;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.etLocation)
    EditText etLocation;

    @BindView(R.id.vsPhoneNumber)
    ViewSwitcher vsPhoneNumber;
    @BindView(R.id.tvPhoneNumber)
    TextView tvPhoneNumber;
    @BindView(R.id.etPhoneNumber)
    EditText etPhoneNumber;

    User user;
    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        user = BookdApplication.getCurrentUser();
    }

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        ButterKnife.bind(this, view);

        String memberSince = "Member since " + user.getMemberSince();
        if (null != user.getUsername()) {
            tvUsername.setText(user.getUsername());
        } else {
            tvUsername.setText(R.string.unknown);
        }

        tvMemberSince.setText(memberSince);
        tvEmail.setText(user.getEmail());
        tvAbout.setText(user.getAbout());
        tvLocation.setText(user.getAddress());
        tvPhoneNumber.setText(user.getPhoneNumber());

        Glide.with(this)
                .load(user.getProfileImageURL())
                .placeholder(R.drawable.ic_account_circle_black_48px)
                .into(ivUserProfileImage);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void GenericViewSwitcher(ViewSwitcher vs, EditText et, TextView tv) {
        vs.showNext();

        et.setText(tv.getText().toString());
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                et.setFocusable(true);
                et.setSelection(et.getText().length());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newField = et.getText().toString();

                if (et.getId() == R.id.etAbout) {
                    user.setAbout(newField);
                } else if (et.getId() == R.id.etLocation) {
                    user.setAddress(newField);
                } else if (et.getId() == R.id.etPhoneNumber) {
                    user.setPhoneNumber(newField);
                }

                BookdApplication.setCurrentUser(user);
            }
        });
    }

    @OnClick(R.id.containerAbout)
    public void AboutClicked(View v) {
        GenericViewSwitcher(vsAbout, etAbout, tvAbout);
    }

    @OnClick(R.id.containerLocation)
    public void LocationClicked(View v) {
        GenericViewSwitcher(vsLocation, etLocation, tvLocation);
    }

    @OnClick(R.id.containerPhoneNumber)
    public void PhoneNumberClicked(View v) {
        GenericViewSwitcher(vsPhoneNumber, etPhoneNumber, tvPhoneNumber);
    }
}