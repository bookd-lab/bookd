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
    private DatePickerDialog datePicker;
    private Calendar calendar;
    private Date chosenDate = new Date(new Date().getTime() + 3 * 24 * 60 * 60 * 1000L); //3 days form now
    private Event event;

    @BindView(R.id.startDateTV)
    TextView startDateTV;
    @BindView(R.id.pickDateButton)
    Button pickDateButton;

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
            chosenDate = new Date(event.getStartDate());
        }

        resetCalendar();

        pickDateButton.setOnClickListener((v) -> showDatePicker());
    }

    private void resetCalendar() {
        calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, 1);

        if (null != chosenDate) {
            calendar.setTime(chosenDate);
        }

        updateChosenDate();
    }

    private void updateChosenDate() {
        startDateTV.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onNext() {
        super.onNext();

        event.setStartDate(chosenDate.getTime());
    }

    void showDatePicker() {
        if (null == datePicker) {
            DatePickerDialog.OnDateSetListener onDateSetListener = (view, year, month, dayOfMonth) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                chosenDate = calendar.getTime();
                updateChosenDate();
            };

            datePicker = new DatePickerDialog(getContext(), onDateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));


            //reset
            datePicker.setButton(DialogInterface.BUTTON_NEUTRAL, getContext().getString(R.string.reset), (dialog, which) -> {
                chosenDate = null;
                updateChosenDate();
            });
        }

        datePicker.show();
    }
}