package bookdlab.bookd.ui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import bookdlab.bookd.R;

/**
 * Created by akhmedovi on 11/19/16.
 * Copyright - 2016
 */

public class SearchEditText extends RelativeLayout {

    EditText searchEdt;
    ImageButton cancelButton;

    public SearchEditText(Context context) {
        super(context);
        init(context);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutParams searchEdtParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        searchEdtParams.addRule(CENTER_VERTICAL);

        searchEdt = new EditText(context);
        searchEdt.setLayoutParams(searchEdtParams);
        searchEdt.setHint(getQueryHint(context));
        searchEdt.setGravity(CENTER_VERTICAL);
        searchEdt.setPadding(0, 10, 0, 0);
        searchEdt.setBackgroundResource(R.color.transparent);

        LayoutParams buttonParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.addRule(CENTER_VERTICAL, getId());
        buttonParams.addRule(ALIGN_PARENT_RIGHT, getId());
        buttonParams.rightMargin = 10;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            buttonParams.addRule(ALIGN_PARENT_END, getId());
        }

        cancelButton = new ImageButton(context);
        cancelButton.setLayoutParams(buttonParams);
        cancelButton.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.close, null));
        cancelButton.setEnabled(false);
        cancelButton.setAlpha(0.5f);
        cancelButton.setBackgroundResource(R.color.transparent);

        addView(searchEdt);
        addView(cancelButton);

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                cancelButton.setEnabled(s.length() > 0);
                cancelButton.setAlpha(s.length() > 0 ? 1f : 0.5f);
            }
        });

        cancelButton.setOnClickListener((v -> searchEdt.setText("")));
    }

    private CharSequence getQueryHint(Context context) {
        Drawable image = ContextCompat.getDrawable(context, R.drawable.search);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" " + getResources().getString(R.string.query_hint));
        ImageSpan imageSpan = new ImageSpan(image, ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
