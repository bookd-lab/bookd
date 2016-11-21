package bookdlab.bookd.fragments.wizards;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import bookdlab.bookd.R;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/14/2016.
 */

public class EventCreateWizard2Fragment extends AbstractEventWizardChild {

    private SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
    private DatePickerDialog startDatePicker;
    private DatePickerDialog endDatePicker;
    private Calendar calendar;
    private Date startDate = new Date(new Date().getTime() + 3 * 24 * 60 * 60 * 1000L); //3 days form now
    private Date endDate = new Date(new Date().getTime() + 3 * 24 * 60 * 60 * 1000L);
    private Event event;

    @BindView(R.id.tvStartDate)
    TextView tvStartDate;
    @BindView(R.id.btnStartDate)
    Button btnStartDate;
    @BindView(R.id.tvEndDate)
    TextView tvEndDate;
    @BindView(R.id.btnEndDate)
    Button btnEndDate;

    // newInstance constructor for creating fragment with arguments
    public static EventCreateWizard2Fragment newInstance() {
        EventCreateWizard2Fragment fragmentFirst = new EventCreateWizard2Fragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_create_wizard2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);

        event = wizardNavigator.getEvent();
        if (null != event.getDates()) {
            startDate = new Date(event.getStartDate());
        }

        resetCalendar();

        btnStartDate.setOnClickListener((v) -> showDatePicker(0));
        btnEndDate.setOnClickListener((v) -> showDatePicker(1));
    }

    private void resetCalendar() {
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        if (null != startDate) {
            calendar.setTime(startDate);
        }

        updateDate(0);
        updateDate(1);
    }

    private void updateDate(int start_end) {
        if(start_end == 0)
            tvStartDate.setText(sdf.format(calendar.getTime()));
        if(start_end == 1){
            tvEndDate.setText(sdf.format(calendar.getTime()));
        }
    }

    @Override
    public void onNext() {
        event.setStartDate(startDate.getTime());
        event.setEndDate(endDate.getTime());
        super.onNext();
    }

    void showDatePicker(int start_end) {
        if (start_end == 0) {
            if (null == startDatePicker) {
                DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    startDate = calendar.getTime();
                    updateDate(0);
                };

                startDatePicker = new DatePickerDialog(getContext(), onDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));


                //reset
                startDatePicker.setButton(DialogInterface.BUTTON_NEUTRAL, getContext().getString(R.string.reset), (dialog, which) -> {
                    startDate = null;
                    updateDate(0);
                });
            }

            startDatePicker.show();
        }
        else if (start_end == 1) {
            if (null == endDatePicker) {
                DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    startDate = calendar.getTime();
                    updateDate(1);
                };

                endDatePicker = new DatePickerDialog(getContext(), onDateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));


                //reset
                endDatePicker.setButton(DialogInterface.BUTTON_NEUTRAL, getContext().getString(R.string.reset), (dialog, which) -> {
                    endDate = null;
                    updateDate(1);
                });
            }

            endDatePicker.show();
        }
    }
}