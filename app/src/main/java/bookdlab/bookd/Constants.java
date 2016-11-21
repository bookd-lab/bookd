package bookdlab.bookd;

import com.google.android.gms.wallet.WalletConstants;

import bookdlab.bookd.models.ItemInfo;

/**
 * Created by pranavkonduru on 11/19/16.
 */

public class Constants {

    public static final String EXTRA_USER_ID = "USER_ID";

    public static final int WALLET_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST;

    public static final String MERCHANT_NAME = "Bookd";

    // Intent extra keys
    public static final String EXTRA_ITEM_ID = "EXTRA_ITEM_ID";
    public static final String EXTRA_MASKED_WALLET = "EXTRA_MASKED_WALLET";
    public static final String EXTRA_FULL_WALLET = "EXTRA_FULL_WALLET";

    public static final String CURRENCY_CODE_USD = "USD";

    // values to use with KEY_DESCRIPTION
    public static final String DESCRIPTION_LINE_ITEM_SHIPPING = "Shipping";
    public static final String DESCRIPTION_LINE_ITEM_TAX = "Tax";

    public static final ItemInfo[] ITEMS_FOR_SALE = {
            new ItemInfo("Simple Bike", "Features", 1, 1, CURRENCY_CODE_USD,
                    "seller data 0", R.drawable.add),
            /*new ItemInfo("Adjustable Bike", "More features", 1, 2, CURRENCY_CODE_USD,
                    "seller data 1", R.drawable.close),
            new ItemInfo("Conference Bike", "Even more features", 1, 3,
                    CURRENCY_CODE_USD, "seller data 2", R.drawable.account)*/
    };
}
