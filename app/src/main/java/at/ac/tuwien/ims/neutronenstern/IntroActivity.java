package at.ac.tuwien.ims.neutronenstern;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.VideoView;

public class IntroActivity extends Activity implements MediaPlayer.OnCompletionListener, View.OnTouchListener {

    private VideoView m_IntroView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Set to fullscreen:
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // Set layout:
        setContentView(R.layout.activity_intro);

        // Initialize VideoView:
        m_IntroView = (VideoView)findViewById(R.id.introView);
        m_IntroView.setOnCompletionListener(this);
        m_IntroView.setOnTouchListener(this);

        // Set Video URI:
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.intro);
        m_IntroView.setVideoURI(video);

        // Start the video:
        m_IntroView.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        // Is called when the video has finished
        skipVideo();
    }

    private void skipVideo() {
        startActivity(new Intent(this, MainMenuActivity.class));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        skipVideo();
        return true;
    }
}
