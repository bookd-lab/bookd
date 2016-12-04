package bookdlab.bookd.fragments.wizards;

import android.inputmethodservice.Keyboard;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.greenfrvr.hashtagview.HashtagView;

import java.util.ArrayList;

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
    private ArrayList<String> tags = new ArrayList<>();

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

        tags = null == event.getTags() ? new ArrayList<>() : event.getTags();
        htvTags.setData(tags);

        tagsEdt.setImeActionLabel(getResources().getString(R.string.add), KeyEvent.KEYCODE_ENTER);
        tagsEdt.setOnKeyListener((v, keyCode, event1) -> {
            if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
                processTag();
                return true;
            }

            return false;
        });

        htvTags.addOnTagClickListener(item -> {
            htvTags.removeItem(item);
        });
    }

    @Override
    public void onNext() {
        event.setTags(tags);
        super.onNext();
    }

    public void processTag() {
        String newTag = tagsEdt.getText().toString().trim();
        if (newTag.isEmpty()) {
            return;
        }

        tags.add(newTag);
        htvTags.setData(tags);
        htvTags.invalidate();

        tagsEdt.setText("");
    }
}