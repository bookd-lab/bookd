package bookdlab.bookd.fragments.wizards;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;

import bookdlab.bookd.R;
import bookdlab.bookd.interfaces.WizardNavigator;
import butterknife.BindView;

/**
 * Created by akhmedovi on 11/19/16.
 * Copyright - 2016
 */

public abstract class AbstractEventWizardChild extends Fragment {

    protected WizardNavigator wizardNavigator;

    @BindView(R.id.nextButton)
    Button nextButton;
    @BindView(R.id.backButton)
    Button backButton;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nextButton.setOnClickListener((v) -> wizardNavigator.onNext());
        backButton.setOnClickListener((v) -> wizardNavigator.onPrev());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof WizardNavigator) {
            wizardNavigator = (WizardNavigator) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement " + WizardNavigator.class.getName());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        wizardNavigator = null;
    }
}
