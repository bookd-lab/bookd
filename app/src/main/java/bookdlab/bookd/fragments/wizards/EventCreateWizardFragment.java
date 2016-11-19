package bookdlab.bookd.fragments.wizards;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Date;

import bookdlab.bookd.R;
import butterknife.ButterKnife;

/**
 * Created by rubab.uddin on 11/13/2016.
 */

public class EventCreateWizardFragment extends AbstractEventWizardChild {

    // Store instance variables
    private String instruction;
    private String hint;
    private Date date;
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
        //handle answers here
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        super.onViewCreated(view, savedInstanceState);
    }
}