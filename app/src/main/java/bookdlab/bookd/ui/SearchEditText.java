package bookdlab.bookd.ui;

import android.content.Context;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import bookdlab.bookd.R;
import bookdlab.bookd.interfaces.SearchInteractionListener;

/**
 * Created by akhmedovi on 11/19/16.
 * Copyright - 2016
 */

public class SearchEditText extends RelativeLayout {

    EditText searchEdt;
    ImageButton cancelButton;
    SearchInteractionListener listener;

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
        LayoutParams searchEdtParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        searchEdtParams.addRule(CENTER_VERTICAL);

        searchEdt = new EditText(context);
        searchEdt.setLayoutParams(searchEdtParams);
        searchEdt.setHint(UIUtils.embedImage(context, getResources().getString(R.string.query_hint), R.drawable.search, 0.9));
        searchEdt.setGravity(CENTER_VERTICAL);
        searchEdt.setSingleLine();
        searchEdt.setPadding(0, 0, 0, 0);
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
        cancelButton.setAlpha(0.5f);
        cancelButton.setBackgroundResource(R.color.transparent);

        addView(searchEdt);
        addView(cancelButton);

        searchEdt.setOnClickListener((v) -> listener.onOpenSearch());
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                cancelButton.setAlpha(s.length() > 0 ? 1f : 0.5f);
            }
        });

        cancelButton.setOnClickListener((v -> {
            searchEdt.setText("");
            listener.onCancelSearch();
        }));

        searchEdt.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                //cuz also fires for UP twice
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    listener.onPerformSearch();
                }

                return true;
            }

            return false;
        });
    }

    public String getQuery() {
        return searchEdt.getText().toString().trim();
    }

    public void setListener(SearchInteractionListener listener) {
        this.listener = listener;
    }

    public void setEditable(boolean status) {
        searchEdt.setFocusable(status);
        searchEdt.setCursorVisible(status);
    }
}
