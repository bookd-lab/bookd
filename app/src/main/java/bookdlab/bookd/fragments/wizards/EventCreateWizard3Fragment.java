package bookdlab.bookd.fragments.wizards;

import android.app.Service;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    InputMethodManager imm;
    Context mContext;

    public static EventCreateWizard3Fragment newInstance() {
        return new EventCreateWizard3Fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getContext();
        imm = (InputMethodManager) mContext.getSystemService(Service.INPUT_METHOD_SERVICE);
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

        tags = event.getTags();
        if(tags != null) {
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
        event.setTags(tags);
        super.onNext();
    }

    public void processTag(){
        imm.hideSoftInputFromWindow(tagsEdt.getWindowToken(), 0);
        String newTag = tagsEdt.getText().toString().replace(" ", "");
        tags.add(newTag);
        htvTags.setData(tags);
        tagsEdt.setText("");
    }
}