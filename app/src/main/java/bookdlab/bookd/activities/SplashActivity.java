package bookdlab.bookd.activities;

import android.content.Intent;
import android.media.audiofx.AudioEffect;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
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
    private static final int CREATE_EVENT_REQUEST = 1002;

    @BindView(R.id.shimmerView)
    ShimmerFrameLayout shimmerFrameLayout;

    @Inject
    BookdApiClient bookdApiClient;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

        ((BookdApplication) getApplication()).getAppComponent().inject(this);

        mAuth = FirebaseAuth.getInstance();

        shimmerFrameLayout.setDuration(1500);
        shimmerFrameLayout.startShimmerAnimation();
        new Handler().postDelayed(this::setupUserProfile, 1500);
    }

    private void setupUserProfile() {
        if (null == mAuth.getCurrentUser()) {
            openLoginActivity();
            return;
        }

        if (null == BookdApplication.getCurrentUser()) {
            fetchUserData(FirebaseAuth.getInstance().getCurrentUser(), this::setupEvents);
            return;
        }

        setupEvents();
    }

    private void setupEvents() {
        bookdApiClient.getEvents(BookdApplication.getCurrentUser().getId(), null, null).enqueue(new Callback<List<Business>>() {
            @Override
            public void onResponse(Call<List<Business>> call, Response<List<Business>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, String.valueOf(response.message()));
                    showTryAgainDialog();
                    return;
                }

                boolean openCreateEventFlow = response.body().isEmpty();
                if (openCreateEventFlow) {
                    startActivityForResult(new Intent(SplashActivity.this, EventCreateActivity.class), CREATE_EVENT_REQUEST);
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
        if (requestCode == LOGIN_ACTIVITY_REQUEST) {
            if (resultCode == RESULT_OK) {
                setupEvents();
                return;
            }

            new AlertDialog.Builder(this).
                    setMessage(R.string.authentication_failed)
                    .setCancelable(false)
                    .setPositiveButton(R.string.try_again, (dialog, which) -> openLoginActivity())
                    .show();
            return;
        }

        if (requestCode == CREATE_EVENT_REQUEST) {
            if (resultCode == RESULT_OK) {
                openMainDashboard();
                return;
            }

            Log.e(TAG, "Failed to create event...");
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        new Handler().postDelayed(() ->
                startActivity(new Intent(SplashActivity.this, MainActivity.class)), 1000);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d(TAG, "onAuthStateChanged: " + String.valueOf(firebaseAuth));
    }

    private void fetchUserData(FirebaseUser user, bookdlab.bookd.interfaces.Callback callback) {
        QueryHelper.isUserPresentInDatabase(user.getUid(), new UserCheckCallback() {
            @Override
            public void userIsPresent(User signedInUser) {
                BookdApplication.setCurrentUser(signedInUser);
                callback.done();
            }

            @Override
            public void userIsNotPresent() {
                Log.d(TAG, "userIsNotPresent: User is not present in database. Create entry");
                User signedInUser = new User();
                signedInUser.setId(user.getUid());
                signedInUser.setEmail(user.getDisplayName());

                DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                String date = df.format(Calendar.getInstance().getTime());
                signedInUser.setMemberSince(date);

                if (user.getPhotoUrl() != null) {
                    signedInUser.setProfileImageURL(user.getPhotoUrl().toString());
                }

                QueryHelper.saveUser(signedInUser, null);
                BookdApplication.setCurrentUser(signedInUser);
                callback.done();
            }
        });

        Log.d(TAG, "setupAuthStateListener: showing splash screen");
    }
}
