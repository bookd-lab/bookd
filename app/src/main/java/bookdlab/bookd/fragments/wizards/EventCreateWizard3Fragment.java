package bookdlab.bookd.fragments.wizards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.StringJoiner;

import bookdlab.bookd.R;
import bookdlab.bookd.models.Event;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/13/2016.
 */

public class EventCreateWizard3Fragment extends AbstractEventWizardChild {

    @BindView(R.id.tagsEdt)
    EditText tagsEdt;
    private Event event;

    public static EventCreateWizard3Fragment newInstance() {
        return new EventCreateWizard3Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_create_wizard3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);


        event = wizardNavigator.getEvent();
        String data = "";
        if (null != event.getTags()) {
            for (String tag : event.getTags()) {
                data += tag + " ";
            }
        }

        tagsEdt.setText(data);


    }

    @Override
    public void onNext() {
        event.setTags(tagsEdt.getText().toString().split(" "));

        super.onNext();
    }
}