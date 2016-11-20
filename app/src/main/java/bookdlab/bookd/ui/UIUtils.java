package bookdlab.bookd.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

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

}
