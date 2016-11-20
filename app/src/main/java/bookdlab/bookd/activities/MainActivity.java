package bookdlab.bookd.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import bookdlab.bookd.Constants;
import bookdlab.bookd.R;
import bookdlab.bookd.adapters.HomeTabsAdapter;
import bookdlab.bookd.database.QueryHelper;
import bookdlab.bookd.interfaces.UserCheckCallback;
import bookdlab.bookd.models.User;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase mDatabase;

    private DatabaseReference mUsersDatabaseReference;

    private static final int RC_SIGN_IN = 1001;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Setup FireBase
        mFirebaseAuth = FirebaseAuth.getInstance();
        setupAuthStateListener();

        viewPager.setAdapter(new HomeTabsAdapter(this, getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuthStateListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: ");
        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult: RC_SIGN_IN");
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "onActivityResult: Result is okay");
                Toast.makeText(this, "Signed in", Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Log.d(TAG, "onActivityResult: Result is cancelled");
                Toast.makeText(this, "Signing cancelled", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupAuthStateListener() {
        Log.d(TAG, "setupAuthStateListener: ");
        mDatabase = FirebaseDatabase.getInstance();
        mUsersDatabaseReference = mDatabase.getReference().child("users");

        mAuthStateListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                Log.d(TAG, "setupAuthStateListener: "+mUsersDatabaseReference.child(user.getUid()).getKey());
                QueryHelper.isUserPresentInDatabase(user.getUid(), new UserCheckCallback() {
                    @Override
                    public void userIsPresent() {
                        Log.d(TAG, "userIsPresent: User is present on database. Saving locally");
                        storeUserInfoLocally(user);
                    }

                    @Override
                    public void userIsNotPresent() {
                        Log.d(TAG, "userIsNotPresent: User is not present in database. Create entry");
                        User signedInUser = new User();
                        signedInUser.setId(user.getUid());
                        signedInUser.setEmail(user.getDisplayName());
                        if(user.getPhotoUrl() != null){
                            signedInUser.setProfileImageURL(user.getPhotoUrl().toString());
                        }

                        mUsersDatabaseReference.push().setValue(signedInUser);
                        storeUserInfoLocally(user);
                    }
                });
                Log.d(TAG, "setupAuthStateListener: showing splash screen");
            } else {
                Log.d(TAG, "setupAuthStateListener: starting firebase auth ui");
                startActivityForResult(AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setProviders(Arrays.asList(
                                new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()))
                        .build(), RC_SIGN_IN);
            }
        };
    }

    private void storeUserInfoLocally(FirebaseUser user) {
        SharedPreferences preferences = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constants.EXTRA_USER_ID, user.getUid());
        editor.apply();
    }
}
