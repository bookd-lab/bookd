package bookdlab.bookd.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import bookdlab.bookd.R;
import bookdlab.bookd.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by rubab.uddin on 11/19/2016.
 */

public class UserFragment extends Fragment {

    @BindView(R.id.tvUsername) TextView tvUsername;
    @BindView(R.id.tvMemberSince) TextView tvMemberSince;
    @BindView(R.id.tvEmail) TextView tvEmail;
    @BindView(R.id.ivUserProfileImage) CircleImageView ivUserProfileImage;

    @BindView(R.id.vsAbout) ViewSwitcher vsAbout;
    @BindView(R.id.tvAbout) TextView tvAbout;
    @BindView(R.id.etAbout) EditText etAbout;

    @BindView(R.id.vsLocation) ViewSwitcher vsLocation;
    @BindView(R.id.tvLocation) TextView tvLocation;
    @BindView(R.id.etLocation) EditText etLocation;

    @BindView(R.id.vsPhoneNumber) ViewSwitcher vsPhoneNumber;
    @BindView(R.id.tvPhoneNumber) TextView tvPhoneNumber;
    @BindView(R.id.etPhoneNumber) EditText etPhoneNumber;

    FirebaseUser fUser;
    User user = new User(); //get the actual logged in user

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fUser = FirebaseAuth.getInstance().getCurrentUser();

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

        tvUsername.setText(user.getUsername());
        tvMemberSince.setText(user.getPhoneNumber());
        tvEmail.setText(user.getEmail());
        Glide.with(this).load(user.getProfileImageURL()).into(ivUserProfileImage);
        tvAbout.setText(user.getAbout());
        tvLocation.setText(user.getAddress());
        tvPhoneNumber.setText(user.getPhoneNumber());

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void AboutClicked() {
        vsAbout.showNext();
        etAbout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String newAbout = etAbout.getText().toString();
                user.setAbout(newAbout);
                tvAbout.setText(newAbout);
                vsAbout.showPrevious();
            }
        });
    }

    public void LocationClicked() {
        vsLocation.showNext();
        etLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String newLocation = etLocation.getText().toString();
                user.setAddress(newLocation);
                tvLocation.setText(newLocation);
                vsLocation.showPrevious();
            }
        });
    }

    public void PhoneNumberClicked() {
        vsPhoneNumber.showNext();
        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String newPhoneNumber = etPhoneNumber.getText().toString();
                user.setPhoneNumber(newPhoneNumber);
                tvPhoneNumber.setText(newPhoneNumber);
                vsPhoneNumber.showPrevious();
            }
        });
    }
}
