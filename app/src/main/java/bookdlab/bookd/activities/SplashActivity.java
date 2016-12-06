package bookdlab.bookd.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.VideoView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.R;
import bookdlab.bookd.api.BookdApiClient;
import bookdlab.bookd.database.QueryHelper;
import bookdlab.bookd.interfaces.UserCheckCallback;
import bookdlab.bookd.models.Event;
import bookdlab.bookd.models.Favorite.Favorite;
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
        new Handler().postDelayed(this::setupUserProfile, 3000);
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

        if (null == BookdApplication.getFavorites()) {
            fetchFavorites(this::setupEvents);
            return;
        }

        setupEvents();
    }

    private void setupEvents() {
        bookdApiClient.getEvents(BookdApplication.getCurrentUser().getId(), null, null).enqueue(new Callback<List<Event>>() {
            @Override
            public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                if (!response.isSuccessful()) {
                    Log.e(TAG, String.valueOf(response.message()));
                    showTryAgainDialog();
                    return;
                }

                boolean openCreateEventFlow = response.body().isEmpty();
                if (openCreateEventFlow) {
                    startActivityForResult(new Intent(SplashActivity.this, EventCreateActivity.class), EventCreateActivity.CREATE_EVENT_REQUEST);
                } else {
                    openMainDashboard();
                }
            }

            @Override
            public void onFailure(Call<List<Event>> call, Throwable t) {
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
                setupUserProfile();
            } else {
                new AlertDialog.Builder(this).
                        setMessage(R.string.authentication_failed)
                        .setCancelable(false)
                        .setPositiveButton(R.string.try_again, (dialog, which) -> openLoginActivity())
                        .show();
            }

            return;
        } else if (requestCode == EventCreateActivity.CREATE_EVENT_REQUEST) {
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
        new Handler().postDelayed(() -> {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 1000);
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        Log.d(TAG, "onAuthStateChanged: " + String.valueOf(firebaseAuth));
    }

    private void fetchUserData(FirebaseUser user, bookdlab.bookd.interfaces.Callback callback) {
        QueryHelper.isUserPresentInDatabase(user.getEmail(), new UserCheckCallback() {
            @Override
            public void userIsPresent(User signedInUser) {
                BookdApplication.setCurrentUser(signedInUser);
                fetchFavorites(callback);
            }

            @Override
            public void userIsNotPresent() {
                Log.d(TAG, "userIsNotPresent: User is not present in database. Create entry");
                User signedInUser = new User();
                signedInUser.setId(user.getUid());
                signedInUser.setEmail(user.getEmail());
                signedInUser.setUsername(user.getDisplayName());

                DateFormat df = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
                String date = df.format(Calendar.getInstance().getTime());
                signedInUser.setMemberSince(date);

                if (user.getPhotoUrl() != null) {
                    signedInUser.setProfileImageURL(user.getPhotoUrl().toString());
                }

                QueryHelper.saveUser(signedInUser, null);
                BookdApplication.setCurrentUser(signedInUser);
                fetchFavorites(callback);
            }
        });

        Log.d(TAG, "setupAuthStateListener: showing splash screen");
    }

    private void fetchFavorites(bookdlab.bookd.interfaces.Callback callback) {
        bookdApiClient.getFavorite(BookdApplication.getCurrentUser().getId()).enqueue(new Callback<List<Favorite>>() {
            @Override
            public void onResponse(Call<List<Favorite>> call, Response<List<Favorite>> response) {
                if (response.isSuccessful()) {
                    setupFavorites(response.body());
                } else {
                    setupFavorites(Collections.emptyList());
                }

                callback.done();
            }

            @Override
            public void onFailure(Call<List<Favorite>> call, Throwable t) {
                setupFavorites(Collections.emptyList());
                callback.done();
            }
        });
    }

    private void setupFavorites(List<Favorite> data) {
        HashMap<String, Favorite> favorites = new HashMap<>();
        for (Favorite f : data) {
            favorites.put(f.getBusiness(), f);
        }

        BookdApplication.setFavorites(favorites);
    }
}
