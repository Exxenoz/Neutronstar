package at.autrage.projects.zeta.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.model.WeaponStockpile;
import at.autrage.projects.zeta.model.Weapons;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.module.Time;
import at.autrage.projects.zeta.tutorial.TutorialManager;
import at.autrage.projects.zeta.module.UpdateFlags;
import at.autrage.projects.zeta.view.GameView;
import at.autrage.projects.zeta.view.GameViewUI;

/**
 * This activity represents the game view and holds the {@link GameView} object.
 */
public class GameActivity extends SuperActivity {
    /** Reference to our {@link GameView} object. */
    private GameView m_GameView;
    /** Indicates whether the sound is muted or not. */
    private boolean m_Muted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        if (!hasGLES20()) {
            // OpenGL ES 2.0 not supported
            Logger.E("Could not create game view, because OpenGL ES 2.0 is not supported.");
            return;
        }

        // Initialize all displays
        GameManager.getInstance().setUpdateFlag(UpdateFlags.All);

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
        gameViewUI.ImgViewTutorialArrow = (ImageView)findViewById(R.id.imgViewTutorialArrow);
        gameViewUI.TxtViewTutorial = (TextView)findViewById(R.id.txtViewTutorial);

        if (!GameManager.getInstance().isTutorialMode()) {
            if (gameViewUI.ImgViewTutorialArrow != null) {
                gameViewUI.ImgViewTutorialArrow.setAlpha(0f);
            }

            if (gameViewUI.TxtViewTutorial != null) {
                gameViewUI.TxtViewTutorial.setAlpha(0f);
            }
        }

        m_GameView = new GameView(this, gameViewUI);

        // Debug
        Button btnDebugWin = (Button)findViewById(R.id.btnDebugWin);
        btnDebugWin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_GameView.win();
            }
        });

        // Debug
        Button btnDebugLoose = (Button)findViewById(R.id.btnDebugLoose);
        btnDebugLoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                m_GameView.lose();
            }
        });

        Button btnAreaPause = (Button)findViewById(R.id.btnAreaPause);
        btnAreaPause.setOnClickListener(new PauseButtonAreaListener(this, (ImageView)findViewById(R.id.imgViewPause)));

        Button btnAreaSound = (Button)findViewById(R.id.btnAreaSound);
        btnAreaSound.setOnClickListener(new SoundButtonAreaListener(this, (ImageView)findViewById(R.id.imgViewSound)));

        ImageView imgViewHighlighted = (ImageView)findViewById(R.id.imgViewHotbarHighlighted);

        Button btnAreaSmallRocket = (Button)findViewById(R.id.btnAreaSmallRocket);
        btnAreaSmallRocket.setOnClickListener(new HotBarButtonAreaListener(this, imgViewHighlighted,
                (int)((146f + 196f * 0f) * getScaleFactor()), Weapons.SmallRocket));

        Button btnAreaBigRocket = (Button)findViewById(R.id.btnAreaBigRocket);
        btnAreaBigRocket.setOnClickListener(new HotBarButtonAreaListener(this, imgViewHighlighted,
                (int)((146f + 196f * 1f) * getScaleFactor()), Weapons.BigRocket));

        Button btnAreaSmallNuke = (Button)findViewById(R.id.btnAreaSmallNuke);
        btnAreaSmallNuke.setOnClickListener(new HotBarButtonAreaListener(this, imgViewHighlighted,
                (int)((146f + 196f * 2f) * getScaleFactor()), Weapons.SmallNuke));

        Button btnAreaBigNuke = (Button)findViewById(R.id.btnAreaBigNuke);
        btnAreaBigNuke.setOnClickListener(new HotBarButtonAreaListener(this, imgViewHighlighted,
                (int)((146f + 196f * 3f) * getScaleFactor()), Weapons.BigNuke));

        Button btnAreaSmallLaser = (Button)findViewById(R.id.btnAreaSmallLaser);
        btnAreaSmallLaser.setOnClickListener(new HotBarButtonAreaListener(this, imgViewHighlighted,
                (int)((146f + 196f * 4f) * getScaleFactor()), Weapons.SmallLaser));

        Button btnAreaBigLaser = (Button)findViewById(R.id.btnAreaBigLaser);
        btnAreaBigLaser.setOnClickListener(new HotBarButtonAreaListener(this, imgViewHighlighted,
                (int)((146f + 196f * 5f) * getScaleFactor()), Weapons.BigLaser));

        Button btnAreaSmallContactBomb = (Button)findViewById(R.id.btnAreaSmallContactBomb);
        btnAreaSmallContactBomb.setOnClickListener(new HotBarButtonAreaListener(this, imgViewHighlighted,
                (int)((146f + 196f * 6f) * getScaleFactor()), Weapons.SmallContactBomb));

        Button btnAreaBigContactBomb = (Button)findViewById(R.id.btnAreaBigContactBomb);
        btnAreaBigContactBomb.setOnClickListener(new HotBarButtonAreaListener(this, imgViewHighlighted,
                (int)((146f + 196f * 7f) * getScaleFactor()), Weapons.BigContactBomb));

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.activity_game);
        layout.addView(m_GameView, 0);

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_game));
    }

    private boolean hasGLES20() {
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CurrentActivity = Activities.GameActivity;

        if (m_GameView != null) {
            m_GameView.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (m_GameView != null) {
            m_GameView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Time.setTimeScale(1f);

        SoundManager.getInstance().setBGMVolume(1f);
        SoundManager.getInstance().setSFXVolume(1f);
    }

    /**
     * Performs a click on the small rocket button area to select small rocket as weapon.
     */
    public void setHighlightedHotbarBoxToSmallRocketArea() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Button btnAreaSmallRocket = (Button) findViewById(R.id.btnAreaSmallRocket);
                btnAreaSmallRocket.performClick();
            }
        });
    }

    private class PauseButtonAreaListener implements View.OnClickListener {

        private GameActivity m_OwnerActivity;
        private ImageView m_ImageView;
        private float m_LastTimeScale;

        public PauseButtonAreaListener(GameActivity ownerActivity, ImageView imageView) {
            m_OwnerActivity = ownerActivity;
            m_ImageView = imageView;
            m_LastTimeScale = Time.getTimeScale();
        }

        @Override
        public void onClick(View v) {
            if (Time.getTimeScale() == 0f) {
                SoundManager.getInstance().ResumeBGM();
                Time.setTimeScale(m_LastTimeScale);
                m_ImageView.setBackgroundResource(R.drawable.gv_icon_pause);
                SoundManager.getInstance().PlaySFX(R.raw.sfx_button_resume);
                Logger.D("Clicked Resume Button...");
            }
            else {
                m_LastTimeScale = Time.getTimeScale();
                Time.setTimeScale(0f);
                SoundManager.getInstance().PauseBGM();
                m_ImageView.setBackgroundResource(R.drawable.gv_icon_play);
                SoundManager.getInstance().PlaySFX(R.raw.sfx_button_pause);
                Logger.D("Clicked Pause Button...");
            }
        }
    }

    private class HotBarButtonAreaListener implements View.OnClickListener {
        private GameActivity m_OwnerActivity;
        private ImageView m_Highlighted;
        private int m_HighlightedPositionX;
        private Weapons m_Weapon;

        public HotBarButtonAreaListener(GameActivity ownerActivity, ImageView highlighted, int highlightedPositionX, Weapons weapon) {
            this.m_OwnerActivity = ownerActivity;
            this.m_Highlighted = highlighted;
            this.m_HighlightedPositionX = highlightedPositionX;
            this.m_Weapon = weapon;
        }

        @Override
        public void onClick(View v) {
            if (m_OwnerActivity.m_GameView == null) {
                return;
            }

            WeaponStockpile weaponStockpile = GameManager.getInstance().getWeaponStockpile(m_Weapon);
            if (weaponStockpile == null) {
                return;
            }

            if (weaponStockpile.getCount() == 0) {
                return;
            }

            if (GameManager.getInstance().isWeaponUpgradeResearched(weaponStockpile.getRequiredWeaponUpgrade())) {
                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) m_Highlighted.getLayoutParams();

                int leftMargin = m_HighlightedPositionX;
                int rightMargin = layoutParams.rightMargin;
                int topMargin = layoutParams.topMargin;
                int bottomMargin = layoutParams.bottomMargin;

                layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
                m_Highlighted.setLayoutParams(layoutParams);

                m_OwnerActivity.m_GameView.changeSelectedWeapon(m_Weapon);
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
                m_ImageView.setBackgroundResource(R.drawable.gv_icon_unmute);
                Logger.D("Clicked Unmute Button...");
            }
            else {
                SoundManager.getInstance().setBGMVolume(0f);
                SoundManager.getInstance().setSFXVolume(0f);
                m_ImageView.setBackgroundResource(R.drawable.gv_icon_mute);
                Logger.D("Clicked Mute Button...");
            }

            m_Muted = !m_Muted;
        }
    }
}
