package com.example.kazuhiroshigenobu.googlemaptraining;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by KazuhiroShigenobu on 25/4/17.
 *
 */
class ResizeAnimation extends Animation {
    private int targetHeight;
    View view;
    private int startHeight;

    ResizeAnimation(View view, int targetHeight, int startHeight) {
        this.view = view;
        this.targetHeight = targetHeight;
        this.startHeight = startHeight;
    }


    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        //to support decent animation, change new heigt as Nico S. recommended in comments
        //int newHeight = (int) (startHeight+(targetHeight - startHeight) * interpolatedTime);
        view.getLayoutParams().height = (int) (startHeight + (targetHeight * interpolatedTime));
        view.requestLayout();
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
    }

    @Override
    public boolean willChangeBounds() {
        return true;
    }
}
