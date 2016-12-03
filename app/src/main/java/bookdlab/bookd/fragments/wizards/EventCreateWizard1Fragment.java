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
    private static final String TAG = EventCreateWizard1Fragment.class.getSimpleName();

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

        eventNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Event.Type eventType = findType(s.toString());
                if (eventType == event.getType()) {
                    return;
                }

                event.setType(eventType);
                setupBackgroundImage(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private Event.Type findType(String s) {
        for (Event.Type type : Event.Type.values()) {
            if (s.toLowerCase().contains(type.name().toLowerCase())) {
                return type;
            }
        }

        return Event.Type.DEFAULT;
    }

    @Override
    public void onNext() {
        event.setName(String.valueOf(eventNameEdt.getText()));
        super.onNext();
    }
}