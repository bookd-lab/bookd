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

import bookdlab.bookd.R;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/13/2016.
 */

public class EventCreateWizardFragment extends AbstractEventWizardChild {

    // Store instance variables
    private String instruction;
    private String hint;

    private String answer;

    private int page;

    // newInstance constructor for creating fragment with arguments
    public static EventCreateWizardFragment newInstance(int page, String instruction, String hint) {
        EventCreateWizardFragment fragmentFirst = new EventCreateWizardFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someInstruction", instruction);
        args.putString("someHint", hint);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        instruction = getArguments().getString("someInstruction");
        hint = getArguments().getString("someHint");
    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_create_wizard, container, false);

        TextView tvInstruction = (TextView) view.findViewById(R.id.tvInstruction);
        tvInstruction.setText(instruction);

        EditText etAnswer = (EditText) view.findViewById(R.id.etAnswer);
        etAnswer.setHint(hint);

        if(page == 3) {
            etAnswer.setVisibility(View.GONE);
            Button btnNextButton = (Button) view.findViewById(R.id.nextButton);
            btnNextButton.setText(R.string.finish);
        }

        etAnswer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                answer = etAnswer.getText().toString();
                switch(page){
                    case 0:
                        wizardNavigator.getEvent().setName(answer);
                    //case 1 is in EventCreateWizardDateFragment
                    case 2:
                        //wizardNavigator.getEvent().setTags();
                }


            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
    }
}