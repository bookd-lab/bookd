package bookdlab.bookd.fragments.wizards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.SuperscriptSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.greenfrvr.hashtagview.HashtagView;

import java.util.ArrayList;
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

        tags = null == event.getTags() ? new ArrayList<>() : event.getTags();
        htvTags.setData(tags);

        tagsEdt.setOnEditorActionListener((v, actionId, event1) -> {
            switch (actionId) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    processTag();
                    return true;
                default:
                    break;
            }

            return false;
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

        tagsEdt.setText("");
    }
}