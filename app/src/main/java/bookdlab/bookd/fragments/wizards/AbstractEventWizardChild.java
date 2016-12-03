package bookdlab.bookd.fragments.wizards;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import bookdlab.bookd.R;
import bookdlab.bookd.interfaces.WizardNavigator;
import bookdlab.bookd.ui.EventUtils;
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
    @BindView(R.id.backgroundImage)
    ImageView backgroundImage;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        nextButton.setOnClickListener((v) -> onNext());
        backButton.setOnClickListener((v) -> onPrev());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (null == wizardNavigator) return; //not attached yet...
        setupBackgroundImage(false);
        Log.d(this.getClass().getSimpleName(), "Setting background...");
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

    protected void setupBackgroundImage(boolean animate) {
        final int eventBackgroundResource = EventUtils.getEventBackgroundResouce(wizardNavigator.getEvent().getType());
        if (!animate) {
            backgroundImage.setImageResource(eventBackgroundResource);
            return;
        }

        final float originalAlpha = backgroundImage.getAlpha();
        backgroundImage.animate().alpha(0).setDuration(300).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                backgroundImage.setImageResource(eventBackgroundResource);
                backgroundImage.animate().alpha(originalAlpha).setDuration(300).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).start();
    }

    public void onNext() {
        wizardNavigator.onNext();
    }

    public void onPrev() {
        wizardNavigator.onPrev();
    }
}
