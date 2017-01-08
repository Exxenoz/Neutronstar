package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.Logger;

public class SplashScreenActivity extends SuperActivity {
    private boolean m_Skipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_splash_screen);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipSplashScreen();
            }
        });

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_splash_screen));

        TextView txtViewSSContinue = (TextView) findViewById(R.id.txtViewSSContinue);
        android.view.animation.Animation textAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_zooming);
        txtViewSSContinue.startAnimation(textAnimation);

        ImageView imgViewSSSpaceCow = (ImageView) findViewById(R.id.imgViewSSSpaceCow);
        AnimationSet spaceCowAnimationSet = new AnimationSet(true);
        RotateAnimation rotate1 = new RotateAnimation(0, 359*1000, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate1.setInterpolator(new LinearInterpolator());
        rotate1.setDuration(7200*100);
        rotate1.setRepeatMode(Animation.RESTART);
        rotate1.setRepeatCount(Animation.INFINITE);
        spaceCowAnimationSet.addAnimation(rotate1);

        TranslateAnimation trans1 = new TranslateAnimation
        (
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.1f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f
        );
        trans1.setDuration(30000);
        trans1.setRepeatMode(Animation.RESTART);
        trans1.setRepeatCount(Animation.INFINITE);
        trans1.setInterpolator(new LinearInterpolator());
        spaceCowAnimationSet.addAnimation(trans1);

        imgViewSSSpaceCow.startAnimation(spaceCowAnimationSet);
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CurrentActivity = Activities.SplashScreenActivity;
    }

    /**
     * This method skips the current splash screen activity
     * by starting the {@link SplashScreenActivity}.
     */
    private void skipSplashScreen() {
        if (!m_Skipped) {
            m_Skipped = true;

            // Redirect to main menu activity
            startActivity(new Intent(this, MainMenuActivity.class));

            // Close current activity
            finish();

            Logger.D("Skipped splash screen view...");
        }
    }
}
