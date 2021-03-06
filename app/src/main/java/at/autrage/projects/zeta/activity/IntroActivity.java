package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.Logger;

/**
 * This activity immediately starts an intro video in full screen mode.
 */
public class IntroActivity extends SuperActivity implements MediaPlayer.OnCompletionListener, View.OnTouchListener {

    private VideoView m_IntroView;
    private boolean m_Skipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set to fullscreen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        // Initialize VideoView
        m_IntroView = (VideoView)findViewById(R.id.introView);
        m_IntroView.setOnCompletionListener(this);
        m_IntroView.setOnTouchListener(this);

        // Set Video URI
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        m_IntroView.setVideoURI(video);

        // Start the video
        m_IntroView.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CurrentActivity = Activities.IntroActivity;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // Is called when the video has finished
        skipVideo();
    }

    /**
     * This method skips the current video activity
     * by starting the {@link MainMenuActivity}.
     */
    private void skipVideo() {
        if (!m_Skipped) {
            m_Skipped = true;

            // Redirect to splash screen activity
            startActivity(new Intent(this, SplashScreenActivity.class));

            // Close current activity
            finish();

            Logger.D("Skipped video intro...");
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        skipVideo();
        return true;
    }
}
