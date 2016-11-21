package bookdlab.bookd.fragments.wizards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.greenfrvr.hashtagview.HashtagView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    @BindView(R.id.htvTags)
    HashtagView htvTags;

    private Event event;
    private List<String> tags = new ArrayList<String>();

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

        String [] eventTags = event.getTags();
        if(eventTags != null) {
            tags = Arrays.asList(eventTags);
            htvTags.setData(tags);
        }
        tagsEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(tagsEdt.getText().toString().contains(" "))
                    processTag();
            }
        });
    }

    @Override
    public void onNext() {
        event.setTags(tags.toArray(new String[0]));
        super.onNext();
    }

    public void processTag(){
        String newTag = tagsEdt.getText().toString().replace(" ", "");
        tags.add(newTag);
        htvTags.setData(tags);
        tagsEdt.setText("");
    }
}