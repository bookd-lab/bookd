package bookdlab.bookd.fragments.wizards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import bookdlab.bookd.R;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/13/2016.
 */

public class EventCreateWizard1Fragment extends AbstractEventWizardChild {

    @BindView(R.id.eventName)
    EditText eventNameEdt;

    private Event event;

    public static EventCreateWizard1Fragment newInstance() {
        return new EventCreateWizard1Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_create_wizard1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);

        event = wizardNavigator.getEvent();
        eventNameEdt.setText(event.getName());
        backButton.setVisibility(View.GONE);
    }

    @Override
    public void onNext() {
        super.onNext();

        event.setName(String.valueOf(eventNameEdt.getText()));
    }
}