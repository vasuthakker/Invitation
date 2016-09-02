package com.example.vasu.invitation.cardstatck.animation;

import android.animation.TypeEvaluator;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.RelativeLayout.LayoutParams;

/*
* when card discard or reverse then this animation class is calling
* and this class is overloading the evaluate method of TypeEvaluator class
* */
public class RelativeLayoutParamsEvaluator implements TypeEvaluator<LayoutParams> {


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public LayoutParams evaluate(float fraction, LayoutParams start,
                                 LayoutParams end) {

        LayoutParams result = new LayoutParams(start);
        result.leftMargin += ((end.leftMargin - start.leftMargin) * fraction);
        result.rightMargin += ((end.rightMargin - start.rightMargin) * fraction);
        result.topMargin += ((end.topMargin - start.topMargin) * fraction);
        result.bottomMargin += ((end.bottomMargin - start.bottomMargin) * fraction);
        return result;
    }

}
