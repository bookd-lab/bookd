package bookdlab.bookd.helpers;

import android.animation.Animator;
import android.view.View;

/**
 * Created by akhmedovi on 11/19/16.
 * Copyright - 2016
 */

public class AnimUtils {

    private static final int DEFAULT_ANIM_DURATION = 500;

    public static void fadeIn(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            return;
        }

        view.setAlpha(0);
        view.setVisibility(View.VISIBLE);

        view.animate().alpha(1).setDuration(DEFAULT_ANIM_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                })
                .start();
    }

    public static void fadeOut(View view) {
        if(view.getVisibility() == View.GONE) {
            return;
        }

        view.animate().alpha(0).setDuration(DEFAULT_ANIM_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setAlpha(1);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .start();
    }
}
