package bookdlab.bookd.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.Constants;
import bookdlab.bookd.R;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.database.QueryHelper;
import bookdlab.bookd.interfaces.UserCheckCallback;
import bookdlab.bookd.models.Business;
import bookdlab.bookd.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by akhmedovi on 11/10/16.
 * Copyright - 2016
 */
public class SplashActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener {
    private String TAG = SplashActivity.class.getSimpleName();

    private static final int LOGIN_ACTIVITY_REQUEST = 1001;

    @BindView(R.id.shimmerView)
    ShimmerFrameLayout shimmerFrameLayout;

    private FirebaseAuth mAuth;

    @Inject
    BookdApiClient bookdApiClient;

    private FirebaseUser currentUser;
    private DatabaseReference mUsersDatabaseReference;
    private FirebaseDatabase mDatabase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        ((BookdApplication) getApplication()).getAppComponent().inject(this);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        shimmerFrameLayout.setDuration(1500);
        shimmerFrameLayout.startShimmerAnimation();
        new Handler().postDelayed(this::setupUserProfile, 1500);
    }

    private void setupUserProfile() {
        currentUser = mAuth.getCurrentUser();
        if (null == currentUser) {
            openLoginActivity();
            return;
        }

        setupEvents();
    }

    private void setupEvents() {
        bookdApiClient.getEvents(currentUser.getUid(), null, null).enqueue(new Callback<List<Business>>() {
            @Override
            public void onResponse(Call<List<Business>> call, Response<List<Business>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, String.valueOf(response.message()));
                    showTryAgainDialog();
                    return;
                }

                boolean openCreateEventFlow = response.body().isEmpty();
                if (openCreateEventFlow) {
                    startActivity(new Intent(SplashActivity.this, EventCreateActivity.class));
                } else {
                    openMainDashboard();
                }
            }

            @Override
            public void onFailure(Call<List<Business>> call, Throwable t) {
                showTryAgainDialog();
            }
        });
    }

    private void showTryAgainDialog() {
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(R.string.check_network_conn)
                .setPositiveButton(R.string.try_again, (dialog, which) -> setupEvents())
                .show();
    }

    private void openLoginActivity() {
        List<AuthUI.IdpConfig> identityProviders = Arrays.asList(
                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build());

        Intent intent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setProviders(identityProviders)
                .build();

        startActivityForResult(intent, LOGIN_ACTIVITY_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setupEvents();
            return;
        }

        new AlertDialog.Builder(this).
                setMessage(R.string.authentication_failed)
                .setCancelable(false)
                .setPositiveButton(R.string.try_again, (dialog, which) -> openLoginActivity())
                .show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    void openMainDashboard() {
        new Handler().postDelayed(() -> startActivity(new Intent(SplashActivity.this, MainActivity.class)), 1000);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d(TAG, "onAuthStateChanged: " + String.valueOf(firebaseAuth.getCurrentUser()));
        mUsersDatabaseReference = mDatabase.getReference().child("users");
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Log.d(TAG, "setupAuthStateListener: " + mUsersDatabaseReference.child(user.getUid()).getKey());
            QueryHelper.isUserPresentInDatabase(user.getEmail(), new UserCheckCallback() {
                @Override
                public void userIsPresent(User signedInUser) {
                    Log.d(TAG, "userIsPresent: User is present on database. Saving locally");
                    storeUserInfoLocally(signedInUser);
                    DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                    String date = df.format(Calendar.getInstance().getTime());
                    signedInUser.setLastSeenTime(date);

                    BookdApplication.setCurrentUser(signedInUser);
                }

                @Override
                public void userIsNotPresent() {
                    Log.d(TAG, "userIsNotPresent: User is not present in database. Create entry");
                    User signedInUser = new User();
                    DatabaseReference userRef = mUsersDatabaseReference.push();
                    signedInUser.setId(userRef.getKey());
                    signedInUser.setUsername(user.getDisplayName());

                    if (user.getProviderData().size() > 0) {
                        signedInUser.setEmail(user.getProviderData().get(0).getEmail());
                    } else {
                        signedInUser.setEmail(user.getEmail());
                    }

                    DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                    String date = df.format(Calendar.getInstance().getTime());
                    signedInUser.setMemberSince(date);
                    signedInUser.setLastSeenTime(date);

                    if (user.getPhotoUrl() != null) {
                        signedInUser.setProfileImageURL(user.getPhotoUrl().toString());
                    }

                    userRef.setValue(signedInUser);
                    BookdApplication.setCurrentUser(signedInUser);

                    storeUserInfoLocally(signedInUser);
                }
            });
        }
    }

    private void storeUserInfoLocally(User user) {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.EXTRA_USER_ID, user.getId());
        editor.apply();
    }
}
