package ru.lihachev.norm31937.widgets;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class ExpandableLayout {
    private LinearLayout collapsablelayout;

    public ExpandableLayout(LinearLayout collapsablelayout) {
        this.collapsablelayout = collapsablelayout;
        this.collapsablelayout.setVisibility(View.GONE);
    }

    public boolean isVisible() {
        return this.collapsablelayout.getVisibility() == View.GONE;
    }

    public void expand()
    {
        collapsablelayout.setVisibility(View.VISIBLE);
        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        collapsablelayout.measure(widthSpec, heightSpec);
        ValueAnimator mAnimator = slideAnimator(0, collapsablelayout.getMeasuredHeight());
        mAnimator.start();
    }

    public void collapse() {
        int finalHeight = collapsablelayout.getHeight();
        ValueAnimator mAnimator = slideAnimator(finalHeight, 0);
        mAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationEnd(Animator animator) {
                //Height=0, but it set visibility to GONE
                collapsablelayout.setVisibility(View.GONE);
            }
            @Override
            public void onAnimationCancel(Animator animation) {

            }
            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        mAnimator.start();
    }

    private ValueAnimator slideAnimator(int start, int end)
    {

        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //Update Height
                int value = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = collapsablelayout.getLayoutParams();
                layoutParams.height = value;
                collapsablelayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

}
