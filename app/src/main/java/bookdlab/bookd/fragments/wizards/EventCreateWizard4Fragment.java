package bookdlab.bookd.fragments.wizards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import bookdlab.bookd.R;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/13/2016.
 */

public class EventCreateWizard4Fragment extends AbstractEventWizardChild {

    public static EventCreateWizard4Fragment newInstance() {
        EventCreateWizard4Fragment fragmentFirst = new EventCreateWizard4Fragment();
        Bundle args = new Bundle();
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_event_create_wizard4, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);

        nextButton.setText(R.string.button_done);
        nextButton.setCompoundDrawables(null, null, ResourcesCompat.getDrawable(getResources(), R.drawable.done, null), null);
    }
}