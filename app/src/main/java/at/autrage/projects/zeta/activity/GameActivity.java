package at.autrage.projects.zeta.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.view.GameView;
import at.autrage.projects.zeta.view.GameViewUI;

/**
 * This activity represents the game view and holds the {@link GameView} object.
 */
public class GameActivity extends SuperActivity implements View.OnClickListener {
    /** Reference to our {@link GameView} object. */
    private GameView m_GameView;
    /** Indicates whether the sound is muted or not. */
    private boolean m_Muted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        m_CurrentActivity = Activities.GameActivity;

        GameViewUI gameViewUI = new GameViewUI();
        gameViewUI.TxtViewPopulation = (TextView)findViewById(R.id.txtViewPopulation);
        gameViewUI.TxtViewMoney = (TextView)findViewById(R.id.txtViewMoney);
        gameViewUI.TxtViewLevel = (TextView)findViewById(R.id.txtViewLevel);
        gameViewUI.TxtViewScore = (TextView)findViewById(R.id.txtViewScore);
        gameViewUI.TxtViewTime = (TextView)findViewById(R.id.txtViewTime);
        gameViewUI.TxtViewFPS = (TextView)findViewById(R.id.txtViewFPS);
        gameViewUI.TxtViewSmallRocketCount = (TextView)findViewById(R.id.txtViewSmallRocketCount);
        gameViewUI.TxtViewBigRocketCount = (TextView)findViewById(R.id.txtViewBigRocketCount);
        gameViewUI.TxtViewSmallNukeCount = (TextView)findViewById(R.id.txtViewSmallNukeCount);
        gameViewUI.TxtViewBigNukeCount = (TextView)findViewById(R.id.txtViewBigNukeCount);
        gameViewUI.TxtViewSmallLaserCount = (TextView)findViewById(R.id.txtViewSmallLaserCount);
        gameViewUI.TxtViewBigLaserCount = (TextView)findViewById(R.id.txtViewBigLaserCount);
        gameViewUI.TxtViewSmallContactBombCount = (TextView)findViewById(R.id.txtViewSmallContactBombCount);
        gameViewUI.TxtViewBigContactBombCount = (TextView)findViewById(R.id.txtViewBigContactBombCount);

        m_GameView = (GameView)findViewById(R.id.gameView);
        m_GameView.setGameViewUI(gameViewUI);

        Button btnFinishGame = (Button)findViewById(R.id.btnFinishGame);
        btnFinishGame.setOnClickListener(this);

        Button btnAreaPause = (Button)findViewById(R.id.btnAreaPause);
        btnAreaPause.setOnClickListener(new PauseButtonAreaListener(this, (ImageView)findViewById(R.id.imgViewPause)));

        Button btnAreaSound = (Button)findViewById(R.id.btnAreaSound);
        btnAreaSound.setOnClickListener(new SoundButtonAreaListener(this, (ImageView)findViewById(R.id.imgViewSound)));

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_game));
    }

    @Override
    public void onClick(View v) {
        // Close current activity
        finish();

        // Start slide animation
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Time.setTimeScale(1f);

        SoundManager.getInstance().setBGMVolume(1f);
        SoundManager.getInstance().setSFXVolume(1f);
    }

    private class PauseButtonAreaListener implements View.OnClickListener {

        private GameActivity m_OwnerActivity;
        private ImageView m_ImageView;

        public PauseButtonAreaListener(GameActivity ownerActivity, ImageView imageView) {
            m_OwnerActivity = ownerActivity;
            m_ImageView = imageView;
        }

        @Override
        public void onClick(View v) {
            if (Time.getTimeScale() == 0f) {
                SoundManager.getInstance().ResumeBGM();
                Time.setTimeScale(1f);
                m_ImageView.setBackgroundResource(R.drawable.gv_icon_pause);
                Logger.D("Clicked Resume Button...");
            }
            else {
                Time.setTimeScale(0f);
                SoundManager.getInstance().PauseBGM();
                m_ImageView.setBackgroundResource(R.drawable.gv_icon_play);
                Logger.D("Clicked Pause Button...");
            }
        }
    }

    private class SoundButtonAreaListener implements View.OnClickListener {

        private GameActivity m_OwnerActivity;
        private ImageView m_ImageView;

        public SoundButtonAreaListener(GameActivity ownerActivity, ImageView imageView) {
            m_OwnerActivity = ownerActivity;
            m_ImageView = imageView;
        }

        @Override
        public void onClick(View v) {
            if (m_Muted) {
                SoundManager.getInstance().setBGMVolume(1f);
                SoundManager.getInstance().setSFXVolume(1f);
                m_ImageView.setBackgroundResource(R.drawable.gv_icon_mute);
                Logger.D("Clicked Unmute Button...");
            }
            else {
                SoundManager.getInstance().setBGMVolume(0f);
                SoundManager.getInstance().setSFXVolume(0f);
                m_ImageView.setBackgroundResource(R.drawable.gv_icon_unmute);
                Logger.D("Clicked Mute Button...");
            }

            m_Muted = !m_Muted;
        }
    }
}
