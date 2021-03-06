package bookdlab.bookd.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.DecimalFormat;

import bookdlab.bookd.R;
import bookdlab.bookd.interfaces.SearchInteractionListener;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by akhmedovi on 11/20/16.
 * Copyright - 2016
 */

public class AdvancedSearchView extends RelativeLayout {

    private static final String TAG = AdvancedSearchView.class.getSimpleName();

    public enum SortByField {
        rating, price
    }

    private SearchInteractionListener listener;

    @BindView(R.id.resetButton)
    Button resetButton;
    @BindView(R.id.searchButton)
    Button searchButton;
    @BindView(R.id.priceSeekBar)
    SeekBar priceSeekBar;
    @BindView(R.id.priceIndicator)
    TextView priceIndicator;
    @BindView(R.id.ratingSeekBar)
    SeekBar ratingSeekBar;
    @BindView(R.id.ratingIndicator)
    TextView ratingIndicator;
    @BindView(R.id.byRating)
    RadioButton byRating;

    public AdvancedSearchView(Context context) {
        super(context);
        initView(context);
    }

    public AdvancedSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AdvancedSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.advanced_search_view, this, true);

        ButterKnife.bind(this);

        priceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int priceLevel = 1 + (progress / 25);
                String indicator = "";
                for (int i = 0; i < priceLevel; i++) {
                    indicator += "$";
                }

                priceIndicator.setText(indicator);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        ratingSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double ratingVal = 5 * (progress / 100.0d);

                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                df.setMinimumFractionDigits(2);

                ratingIndicator.setText(df.format(ratingVal));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        searchButton.setOnClickListener((v) -> {
            if (null != listener) {
                listener.onPerformSearch();
            }
        });

        resetButton.setOnClickListener(v -> {
            priceSeekBar.setProgress(100); //max include
            ratingSeekBar.setProgress(0); //min set
            byRating.setChecked(true);
        });
    }

    public void setListener(SearchInteractionListener listener) {
        this.listener = listener;
    }

    public double getRating() {
        return 5 * (ratingSeekBar.getProgress() / 100.0d);
    }

    public int getPrice() {
        return 1 + (priceSeekBar.getProgress() / 25);
    }

    public SortByField getSortByField() {
        if (byRating.isChecked()) {
            return SortByField.rating;
        }

        return SortByField.price;
    }
}
