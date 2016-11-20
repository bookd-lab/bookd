package bookdlab.bookd.helpers;

import android.animation.Animator;
import android.view.View;

/**
 * Created by akhmedovi on 11/19/16.
 * Copyright - 2016
 */

public class AnimUtils {

    static final int DEFAULT_ANIM_DURATION = 500;

    public static void fadeIn(View view) {
        view.animate().alpha(1).setDuration(DEFAULT_ANIM_DURATION)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        view.setAlpha(0);
                        view.setVisibility(View.VISIBLE);
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
