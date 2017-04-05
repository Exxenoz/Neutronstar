package at.autrage.projects.zeta.tutorial;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.GameActivity;
import at.autrage.projects.zeta.activity.MainMenuActivity;
import at.autrage.projects.zeta.collision.CircleCollider;
import at.autrage.projects.zeta.model.Asteroid;
import at.autrage.projects.zeta.model.Enemy;
import at.autrage.projects.zeta.model.GameObject;
import at.autrage.projects.zeta.model.Player;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.Pustafin;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.opengl.MeshRenderer;
import at.autrage.projects.zeta.ui.TouchEvent;
import at.autrage.projects.zeta.util.Util;
import at.autrage.projects.zeta.view.GameView;

public class TutorialManager {
    private GameActivity gameActivity;
    private GameView gameView;

    private ImageView m_ImgViewTutorialArrow;
    private TextView m_TxtViewTutorial;

    private boolean clickEventActive;

    private TutorialEntry[] m_TutorialEntries = new TutorialEntry[] {
        /*00*/ new TutorialEntry(0, 0, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, false, R.string.tv_desc_welcome, 0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.CENTER_VERTICAL),
        /*01*/ new TutorialEntry(0, 274, RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_planet1, 40, 128, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*02*/ new TutorialEntry(309, 225, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM, true, true, R.string.tv_desc_weapon1, 40, 400, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*03*/ new TutorialEntry(309, 225, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM, true, true, R.string.tv_desc_weapon2, 40, 400, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*04*/ new TutorialEntry(210, 250, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM, true, true, R.string.tv_desc_small_rocket1, 40, 400, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*05*/ new TutorialEntry(210, 250, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM, true, true, R.string.tv_desc_small_rocket2, 40, 400, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*06*/ new TutorialEntry(394, 190, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM, true, true, R.string.tv_desc_big_rocket1, 218, 345, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*07*/ new TutorialEntry(0, 274, RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_asteroid1, 40, 128, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*08*/ new TutorialEntry(0, 0, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, false, R.string.tv_desc_asteroid3, 40, 128, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*09*/ new TutorialEntry(0, 274, RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_asteroid4, 40, 128, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*10*/ new TutorialEntry(0, 0, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, false, R.string.tv_desc_asteroid2, 40, 128, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*11*/ new TutorialEntry(210, 115, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_population1, 40, 256, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*12*/ new TutorialEntry(210, 115, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_population2, 40, 256, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*13*/ new TutorialEntry(580, 115, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_money1, 350, 256, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*14*/ new TutorialEntry(580, 115, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_money2, 350, 256, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*15*/ new TutorialEntry(1250, 115, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_score1, 320, 256, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP),
        /*16*/ new TutorialEntry(1555, 115, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_time1, 40, 256, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_TOP),
        /*17*/ new TutorialEntry(18, 155, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM, true, true, R.string.tv_desc_pause, 40, 310, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*18*/ new TutorialEntry(18, 155, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM, true, true, R.string.tv_desc_mute, 40, 310, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*19*/ new TutorialEntry(0, 0, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, false, R.string.tv_desc_finish, 0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.CENTER_VERTICAL)
    };

    private int m_CurrentTutorialIndex;
    private Timer m_RedirectionTimer;
    private boolean m_Finished;

    public TutorialManager(GameView gameView) {
        this.gameActivity = gameView.GameActivity;
        this.gameView = gameView;

        m_ImgViewTutorialArrow = gameView.UI.ImgViewTutorialArrow;
        m_TxtViewTutorial = gameView.UI.TxtViewTutorial;

        if (m_ImgViewTutorialArrow != null) {
            android.view.animation.Animation arrowAnimation = AnimationUtils.loadAnimation(gameActivity.getApplicationContext(), R.anim.arrow_up_down);
            m_ImgViewTutorialArrow.startAnimation(arrowAnimation);
        }

        clickEventActive = false;

        setCurrentTutorialIndex(0);
        m_RedirectionTimer = new Timer();
        m_Finished = false;

        setPlayerVisibility(gameView, false);
        updateTutorialEntryFromMainThread();
    }

    private void setPlayerVisibility(GameView gameView, boolean visible) {
        Player player = gameView.getPlayer();
        if (player != null) {
            player.setRemainingTime(0);

            MeshRenderer meshRenderer = player.getGameObject().getComponent(MeshRenderer.class);
            if (meshRenderer != null) {
                if (visible) {
                    meshRenderer.enable();
                }
                else {
                    meshRenderer.disable();
                }
            }

            GameObject weaponLaunchGameObject = gameView.UserInterfacePrefab.WeaponLaunchAreaGameObject;
            if (weaponLaunchGameObject != null) {
                CircleCollider circleCollider = weaponLaunchGameObject.getComponent(CircleCollider.class);
                if (circleCollider != null) {
                    if (visible) {
                        circleCollider.enable();
                    }
                    else {
                        circleCollider.disable();
                    }
                }
            }
        }
    }

    public void touch(TouchEvent touchEvent) {
        switch (touchEvent.Action) {
            case MotionEvent.ACTION_DOWN:
                if (!clickEventActive) {
                    clickEventActive = true;
                    clickEvent();
                }
                break;
            case MotionEvent.ACTION_UP:
                clickEventActive = false;
                break;
        }
    }

    private void clickEvent() {
        if (m_Finished) {
            return;
        }

        if (m_ImgViewTutorialArrow == null ||
            m_TxtViewTutorial == null) {
            return;
        }

        switch (m_CurrentTutorialIndex) {
            case 7: // Asteroid state
            case 9: // Asteroid repeat state
                return;
        }

        setCurrentTutorialIndex(m_CurrentTutorialIndex + 1);

        if (m_CurrentTutorialIndex >= m_TutorialEntries.length) {
            m_Finished = true;

            m_RedirectionTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    gameActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Open main menu activity
                            Intent redirectIntent = new Intent(gameActivity, MainMenuActivity.class);
                            gameActivity.startActivity(redirectIntent);

                            // Close game activity
                            gameActivity.finish();

                            // Start slide animation
                            //gameView.getGameActivity().overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
                        }
                    });
                }
            }, Pustafin.GameActivityRedirectionDelayOnTutorialFinish);

            SoundManager.getInstance().PlaySFX(R.raw.sfx_drumhits_new_highscore);
            return;
        }

        switch (m_CurrentTutorialIndex) {
            case 1: // Show planet
                setPlayerVisibility(gameView, true);
                break;
            case 7: // Asteroid state
            case 9: // Asteroid repeat state
                gameView.getEnemySpawner().spawnTutorialAsteroid();
                break;
            case 11: // Hide planet
                setPlayerVisibility(gameView, false);
                break;
        }

        updateTutorialEntryFromMainThread();
    }

    public void onDestroyEnemy(Enemy enemy) {
        if (enemy instanceof Asteroid) {
            if (enemy.getHealth() > 0) {
                setCurrentTutorialIndex(8);
            }
            else {
                setCurrentTutorialIndex(10);
            }

            updateTutorialEntryFromMainThread();
        }
    }

    private void updateTutorialEntryFromMainThread() {
        gameActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateTutorialEntry(gameActivity, m_CurrentTutorialIndex);
            }
        });
    }

    private void updateTutorialEntry(GameActivity gameActivity, int tutorialIndex) {
        if (m_CurrentTutorialIndex < 0 ||
            m_CurrentTutorialIndex >= m_TutorialEntries.length) {
            return;
        }

        if (m_ImgViewTutorialArrow == null ||
            m_TxtViewTutorial == null) {
            return;
        }

        TutorialEntry tutorialEntry = m_TutorialEntries[tutorialIndex];

        if (tutorialEntry.ArrowDirectionDown) {
            m_ImgViewTutorialArrow.setRotation(180f);
        }
        else {
            m_ImgViewTutorialArrow.setRotation(0f);
        }

        if (tutorialEntry.ArrowVisible) {
            m_ImgViewTutorialArrow.setAlpha(1f);
        }
        else {
            m_ImgViewTutorialArrow.setAlpha(0f);
        }

        Util.resetViewLayoutRules(m_ImgViewTutorialArrow);

        if (tutorialEntry.ArrowAlignmentX == RelativeLayout.ALIGN_PARENT_LEFT) {
            Util.addViewLayoutRule(m_ImgViewTutorialArrow, RelativeLayout.ALIGN_PARENT_LEFT);
            Util.setViewLeftMargin(m_ImgViewTutorialArrow, tutorialEntry.ArrowPositionX);
            Util.setViewRightMargin(m_ImgViewTutorialArrow, 0);
        }
        else if (tutorialEntry.ArrowAlignmentX == RelativeLayout.ALIGN_PARENT_RIGHT) {
            Util.addViewLayoutRule(m_ImgViewTutorialArrow, RelativeLayout.ALIGN_PARENT_RIGHT);
            Util.setViewLeftMargin(m_ImgViewTutorialArrow, 0);
            Util.setViewRightMargin(m_ImgViewTutorialArrow, tutorialEntry.ArrowPositionX);
        }
        else if (tutorialEntry.ArrowAlignmentX == RelativeLayout.CENTER_HORIZONTAL) {
            Util.addViewLayoutRule(m_ImgViewTutorialArrow, RelativeLayout.CENTER_HORIZONTAL);
            Util.setViewLeftMargin(m_ImgViewTutorialArrow, 0);
            Util.setViewRightMargin(m_ImgViewTutorialArrow, 0);
        }

        if (tutorialEntry.ArrowAlignmentY == RelativeLayout.ALIGN_PARENT_TOP) {
            Util.addViewLayoutRule(m_ImgViewTutorialArrow, RelativeLayout.ALIGN_PARENT_TOP);
            Util.setViewTopMargin(m_ImgViewTutorialArrow, tutorialEntry.ArrowPositionY);
            Util.setViewBottomMargin(m_ImgViewTutorialArrow, 0);
        }
        else if (tutorialEntry.ArrowAlignmentY == RelativeLayout.ALIGN_PARENT_BOTTOM) {
            Util.addViewLayoutRule(m_ImgViewTutorialArrow, RelativeLayout.ALIGN_PARENT_BOTTOM);
            Util.setViewTopMargin(m_ImgViewTutorialArrow, 0);
            Util.setViewBottomMargin(m_ImgViewTutorialArrow, tutorialEntry.ArrowPositionY);
        }
        else if (tutorialEntry.ArrowAlignmentY == RelativeLayout.CENTER_VERTICAL) {
            Util.addViewLayoutRule(m_ImgViewTutorialArrow, RelativeLayout.CENTER_VERTICAL);
            Util.setViewTopMargin(m_ImgViewTutorialArrow, 0);
            Util.setViewBottomMargin(m_ImgViewTutorialArrow, 0);
        }

        m_TxtViewTutorial.setAlpha(1f);
        Util.resetViewLayoutRules(m_TxtViewTutorial);

        if (tutorialEntry.TextAlignmentX == RelativeLayout.ALIGN_PARENT_LEFT) {
            Util.addViewLayoutRule(m_TxtViewTutorial, RelativeLayout.ALIGN_PARENT_LEFT);
            Util.setViewLeftMargin(m_TxtViewTutorial, tutorialEntry.TextPositionX);
            Util.setViewRightMargin(m_TxtViewTutorial, 0);
        }
        else if (tutorialEntry.TextAlignmentX == RelativeLayout.ALIGN_PARENT_RIGHT) {
            Util.addViewLayoutRule(m_TxtViewTutorial, RelativeLayout.ALIGN_PARENT_RIGHT);
            Util.setViewLeftMargin(m_TxtViewTutorial, 0);
            Util.setViewRightMargin(m_TxtViewTutorial, tutorialEntry.TextPositionX);
        }
        else if (tutorialEntry.TextAlignmentX == RelativeLayout.CENTER_HORIZONTAL) {
            Util.addViewLayoutRule(m_TxtViewTutorial, RelativeLayout.CENTER_HORIZONTAL);
            Util.setViewLeftMargin(m_TxtViewTutorial, 0);
            Util.setViewRightMargin(m_TxtViewTutorial, 0);
        }

        if (tutorialEntry.TextAlignmentY == RelativeLayout.ALIGN_PARENT_TOP) {
            Util.addViewLayoutRule(m_TxtViewTutorial, RelativeLayout.ALIGN_PARENT_TOP);
            Util.setViewTopMargin(m_TxtViewTutorial, tutorialEntry.TextPositionY);
            Util.setViewBottomMargin(m_TxtViewTutorial, 0);
        }
        else if (tutorialEntry.TextAlignmentY == RelativeLayout.ALIGN_PARENT_BOTTOM) {
            Util.addViewLayoutRule(m_TxtViewTutorial, RelativeLayout.ALIGN_PARENT_BOTTOM);
            Util.setViewTopMargin(m_TxtViewTutorial, 0);
            Util.setViewBottomMargin(m_TxtViewTutorial, tutorialEntry.TextPositionY);
        }
        else if (tutorialEntry.TextAlignmentY == RelativeLayout.CENTER_VERTICAL) {
            Util.addViewLayoutRule(m_TxtViewTutorial, RelativeLayout.CENTER_VERTICAL);
            Util.setViewTopMargin(m_TxtViewTutorial, 0);
            Util.setViewBottomMargin(m_TxtViewTutorial, 0);
        }

        m_TxtViewTutorial.setText(gameActivity.getString(tutorialEntry.TextResourceId));
        Util.setViewWidth(m_TxtViewTutorial, tutorialEntry.TextBoxWidth);
    }

    public int getCurrentTutorialIndex() {
        return m_CurrentTutorialIndex;
    }

    private void setCurrentTutorialIndex(int currentTutorialIndex) {
        this.m_CurrentTutorialIndex = currentTutorialIndex;

        Logger.D("Set current tutorial index to %d", m_CurrentTutorialIndex);
    }
}
