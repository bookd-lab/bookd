package bookdlab.bookd.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Random;

import bookdlab.bookd.R;

/**
 * Created by akhmedovi on 11/20/16.
 * Copyright - 2016
 */

public class UIUtils {

    public static CharSequence embedImage(Context context, String str, int drawableResource, double scale) {
        Drawable image = ContextCompat.getDrawable(context, drawableResource);
        image.setBounds(0, 0, (int) (scale * image.getIntrinsicWidth()), (int) (scale * image.getIntrinsicHeight()));
        SpannableString sb = new SpannableString(" " + str);
        ImageSpan imageSpan = new ImageSpan(image);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    public static void hideSoftInput(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static int getMaterialColor(Context context){
        int returnColor = R.color.deep_orange_A100;
        int[] androidColors = context.getResources().getIntArray(R.array.androidColors);

        if (androidColors != null)
        {
            returnColor = androidColors[new Random().nextInt(androidColors.length)];
        }
        return returnColor;
    }

}
