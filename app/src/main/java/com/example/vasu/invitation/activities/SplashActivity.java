package com.example.vasu.invitation.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.example.vasu.invitation.R;

import tyrantgit.explosionfield.ExplosionField;

public class SplashActivity extends AppCompatActivity {

    private ExplosionField mExplosionField;

    private TextView txtContinue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mExplosionField = ExplosionField.attach2Window(this);

        TextView txtNote = (TextView) findViewById(R.id.note);
        TextView txtThe = (TextView) findViewById(R.id.the);
        TextView txtDate = (TextView) findViewById(R.id.date);
        TextView txtDate2 = (TextView) findViewById(R.id.date2);

        txtContinue = (TextView) findViewById(R.id.continuetxt);

        translate(0, txtNote, dpToPx(getApplicationContext(), -360), false);
        translate(1000, txtThe, dpToPx(getApplicationContext(), -305), false);
        translate(2000, txtDate, dpToPx(getApplicationContext(), -250), false);
        translate(3000, txtDate2, dpToPx(getApplicationContext(), -150), true);

    }


    private void translate(int offset, final View view, final float y, final boolean isLastAnimation) {

        TranslateAnimation translateAnim = new TranslateAnimation(0, 0, 0, y);
        translateAnim.setDuration(1000);
        translateAnim.setStartOffset(offset);
        translateAnim.setFillAfter(true);
        translateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setY(view.getY() + y);
                if (!isLastAnimation) {
                    mExplosionField.explode(view, null);
                } else {
                    ScaleAnimation anim1 = new ScaleAnimation(1f, 1.5f, 1f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    anim1.setDuration(1500);
                    view.startAnimation(anim1);
                    anim1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            txtContinue.setVisibility(View.VISIBLE);
                            txtContinue.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    mExplosionField.explode(txtContinue, new ExplosionField.OnEnd() {
                                        @Override
                                        public void end() {
                                            Intent intent = new Intent(SplashActivity.this, MainScreenActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            });
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    anim1.setFillAfter(true);

                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(translateAnim);
    }

    private float dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

}

