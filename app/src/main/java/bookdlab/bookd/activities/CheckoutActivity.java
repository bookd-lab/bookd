package bookdlab.bookd.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.MaskedWalletRequest;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;
import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
import com.google.android.gms.wallet.fragment.WalletFragmentMode;
import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
import com.google.android.gms.wallet.fragment.WalletFragmentStyle;

import bookdlab.bookd.BookdApplication;
import bookdlab.bookd.Constants;
import bookdlab.bookd.R;
import bookdlab.bookd.utils.WalletUtil;

/**
 * Created by pranavkonduru on 11/21/16.
 */

public class CheckoutActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_MASKED_WALLET = 1432;
    private GoogleApiClient mGoogleApiClient;
    private SupportWalletFragment mWalletFragment;
    private static final String TAG = "CheckoutActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        initGoogleApiClient();
        launchPayment();
    }

    private void initGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(Constants.WALLET_ENVIRONMENT)
                        .build())
                .enableAutoManage(this, this)
                .build();
    }


    private void launchPayment() {
        Wallet.Payments.isReadyToPay(mGoogleApiClient).setResultCallback(
                booleanResult -> {
//                    hideProgressDialog();

                    if (booleanResult.getStatus().isSuccess()) {
                        if (booleanResult.getValue()) {
                            // Show Android Pay buttons and hide regular checkout button
                            // [START_EXCLUDE]
                            Log.d(TAG, "isReadyToPay:true");
                            createAndAddWalletFragment();
                            /*findViewById(R.id.button_regular_checkout)
                                    .setVisibility(View.GONE);*/
                            // [END_EXCLUDE]
                        } else {
                            // Hide Android Pay buttons, show a message that Android Pay
                            // cannot be used yet, and display a traditional checkout button
                            // [START_EXCLUDE]
                            Log.d(TAG, "isReadyToPay:false:" + booleanResult.getStatus());
                            /*findViewById(R.id.layout_android_pay_checkout)
                                    .setVisibility(View.GONE);
                            findViewById(R.id.android_pay_message)
                                    .setVisibility(View.VISIBLE);
                            findViewById(R.id.button_regular_checkout)
                                    .setVisibility(View.VISIBLE);*/
                            // [END_EXCLUDE]
                        }
                    } else {
                        // Error making isReadyToPay call
                        Log.e(TAG, "isReadyToPay:" + booleanResult.getStatus());
                    }
                });
    }

    private void createAndAddWalletFragment() {
        // [START fragment_style_and_options]
        WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                .setBuyButtonAppearance(WalletFragmentStyle.BuyButtonAppearance.ANDROID_PAY_DARK)
                .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);

        WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                .setEnvironment(Constants.WALLET_ENVIRONMENT)
                .setFragmentStyle(walletFragmentStyle)
                .setTheme(WalletConstants.THEME_LIGHT)
                .setMode(WalletFragmentMode.BUY_BUTTON)
                .build();
        mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
        // [END fragment_style_and_options]

        // Now initialize the Wallet Fragment
        //String accountName = BookdApplication.getCurrentUser().getUsername();
        MaskedWalletRequest maskedWalletRequest;
        int mItemId = 1;
        boolean mUseStripe = true;
        maskedWalletRequest = WalletUtil.createStripeMaskedWalletRequest(
                Constants.ITEMS_FOR_SALE[0],
                getString(R.string.stripe_publishable_key),
                getString(R.string.stripe_version));
        /*if (mUseStripe) {
            // Stripe integration
        } else {
            // Direct integration
            maskedWalletRequest = WalletUtil.createMaskedWalletRequest(
                    Constants.ITEMS_FOR_SALE[mItemId],
                    getString(R.string.public_key));
        }*/

        // [START params_builder]
        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(maskedWalletRequest)
                .setMaskedWalletRequestCode(REQUEST_CODE_MASKED_WALLET);
                //.setAccountName(accountName);
        mWalletFragment.initialize(startParamsBuilder.build());

        // add Wallet fragment to the UI
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dynamic_wallet_button_fragment, mWalletFragment)
                .commit();
        // [END params_builder]
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_MASKED_WALLET: {
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (data != null) {
                            MaskedWallet maskedWallet =
                                    data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
//                            launchConfirmationPage(maskedWallet);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        break;
                }
            }
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

