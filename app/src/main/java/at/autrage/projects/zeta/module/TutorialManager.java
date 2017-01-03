package at.autrage.projects.zeta.module;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.MainMenuActivity;
import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.model.Asteroid;
import at.autrage.projects.zeta.model.Enemy;
import at.autrage.projects.zeta.plugin.TextViewEx;
import at.autrage.projects.zeta.view.GameView;

public class TutorialManager {
    private static TutorialManager m_Instance;

    public static TutorialManager getInstance() {
        if (m_Instance == null) {
            m_Instance = new TutorialManager();
        }

        return m_Instance;
    }

    private class TutorialEntry {
        public int ArrowPositionX;
        public int ArrowPositionY;
        public boolean ArrowDirectionDown;
        public boolean ArrowVisible;

        public int TextResourceId;
        public int TextPositionX;
        public int TextPositionY;
        public int TextBoxWidth;
        public boolean TextJustified;

        public TutorialEntry(int arrowPositionX, int arrowPositionY, boolean arrowDirectionDown, boolean arrowVisible, int textResourceId, int textPositionX, int textPositionY, int textBoxWidth, boolean textJustified) {
            ArrowPositionX = arrowPositionX;
            ArrowPositionY = arrowPositionY;
            ArrowDirectionDown = arrowDirectionDown;
            ArrowVisible = arrowVisible;

            TextResourceId = textResourceId;
            TextPositionX = textPositionX;
            TextPositionY = textPositionY;
            TextBoxWidth = textBoxWidth;
            TextJustified = textJustified;
        }
    }

    private ImageView m_ImgViewArrow;
    private TextViewEx m_TxtViewTutorialText;

    private TutorialEntry[] m_TutorialEntries = new TutorialEntry[] {
        /*00*/ new TutorialEntry(0, 0, false, false, R.string.tv_desc_welcome, 390, 420, 1140, false),
        /*01*/ new TutorialEntry(896, 274, true, true, R.string.tv_desc_planet1, 40, 250, 480, true),
        /*02*/ new TutorialEntry(309, 725, true, true, R.string.tv_desc_weapon1, 133, 475, 480, true),
        /*03*/ new TutorialEntry(309, 725, true, true, R.string.tv_desc_weapon2, 133, 435, 480, true),
        /*04*/ new TutorialEntry(210, 725, true, true, R.string.tv_desc_small_rocket1, 40, 525, 480, true),
        /*05*/ new TutorialEntry(210, 725, true, true, R.string.tv_desc_small_rocket2, 40, 475, 480, true),
        /*06*/ new TutorialEntry(394, 795, true, true, R.string.tv_desc_big_rocket1, 218, 560, 480, true),
        /*07*/ new TutorialEntry(896, 274, true, true, R.string.tv_desc_asteroid1, 40, 250, 480, true),
        /*08*/ new TutorialEntry(896, 274, true, false, R.string.tv_desc_asteroid3, 40, 250, 480, true),
        /*09*/ new TutorialEntry(896, 274, true, true, R.string.tv_desc_asteroid4, 40, 250, 480, true),
        /*10*/ new TutorialEntry(896, 274, true, false, R.string.tv_desc_asteroid2, 40, 250, 480, true),
        /*11*/ new TutorialEntry(200, 110, false, true, R.string.tv_desc_population1, 40, 250, 480, true),
        /*12*/ new TutorialEntry(200, 110, false, true, R.string.tv_desc_population2, 40, 250, 480, true),
        /*13*/ new TutorialEntry(580, 110, false, true, R.string.tv_desc_money1, 404, 250, 480, true),
        /*14*/ new TutorialEntry(580, 110, false, true, R.string.tv_desc_money2, 404, 250, 480, true),
        /*15*/ new TutorialEntry(1230, 110, false, true, R.string.tv_desc_score1, 1054, 250, 480, true),
        /*16*/ new TutorialEntry(1590, 110, false, true, R.string.tv_desc_time1, 1414, 250, 480, true),
        /*17*/ new TutorialEntry(16, 812, true, true, R.string.tv_desc_pause, 40, 542, 480, true),
        /*18*/ new TutorialEntry(1776, 812, true, true, R.string.tv_desc_mute, 1400, 542, 480, true),
        /*19*/ new TutorialEntry(0, 0, false, false, R.string.tv_desc_finish, 390, 420, 1140, false)
    };

    private int m_CurrentTutorialIndex;

    private boolean m_Finished;

    private TutorialManager() {
        reset();
    }

    public void startTutorial(GameView gameView) {
        reset();

        updateTutorialEntry(gameView);

        // Call game views update method to initialize UI elements
        gameView.update();

        if (gameView.getPlayer() != null) {
            gameView.getPlayer().setRemainingTime(0);
            gameView.getPlayer().setVisible(false);
        }

        m_Finished = false;
    }

    public void onClickEvent(GameView gameView) {
        if (m_Finished) {
            return;
        }

        if (m_ImgViewArrow == null) {
            return;
        }

        if (m_TxtViewTutorialText == null) {
            return;
        }

        m_CurrentTutorialIndex++;

        if (m_CurrentTutorialIndex >= m_TutorialEntries.length) {
            m_Finished = true;
            SoundManager.getInstance().PlaySFX(R.raw.sfx_ending_win);

            // Open main menu activity
            Intent redirectIntent = new Intent(gameView.getGameActivity(), MainMenuActivity.class);
            gameView.getGameActivity().startActivity(redirectIntent);

            // Close game activity
            gameView.getGameActivity().finish();

            // Start slide animation
            gameView.getGameActivity().overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            return;
        }

        switch (m_CurrentTutorialIndex) {
            case 1:
                if (gameView.getPlayer() != null) {
                    gameView.getPlayer().setVisible(true);
                }
                break;
            case 7:
            case 9:
                gameView.getEnemySpawner().spawnTutorialAsteroid();
                break;
            case 8: // Asteroid state
            case 10:// Asteroid repeat state
                m_CurrentTutorialIndex--;
                return;
            case 11:
                if (gameView.getPlayer() != null) {
                    gameView.getPlayer().setVisible(false);
                }
                break;
        }

        updateTutorialEntry(gameView);

        return;
    }

    public void onDestroyEnemy(Enemy enemy) {
        if (enemy instanceof Asteroid) {
            if (enemy.getHealth() > 0) {
                m_CurrentTutorialIndex = 8;
            }
            else {
                m_CurrentTutorialIndex = 10;
            }

            enemy.getGameView().getGameActivity().runOnUiThread(new UpdateTutorialEntryFromUIThread(enemy.getGameView()));
        }
    }

    private void updateTutorialEntry(GameView gameView) {
        TutorialEntry tutorialEntry = m_TutorialEntries[m_CurrentTutorialIndex];

        if (tutorialEntry.ArrowDirectionDown) {
            m_ImgViewArrow.setRotation(180f);
        }
        else {
            m_ImgViewArrow.setRotation(0f);
        }

        if (tutorialEntry.ArrowVisible) {
            m_ImgViewArrow.setAlpha(1f);
        }
        else {
            m_ImgViewArrow.setAlpha(0f);
        }

        m_TxtViewTutorialText.setAlpha(1f);

        Util.setLeftAndTopMargin(m_ImgViewArrow, tutorialEntry.ArrowPositionX, tutorialEntry.ArrowPositionY);
        Util.setLeftAndTopMargin(m_TxtViewTutorialText, tutorialEntry.TextPositionX, tutorialEntry.TextPositionY);

        Util.setViewWidth(m_TxtViewTutorialText, (int) (tutorialEntry.TextBoxWidth * SuperActivity.getScaleFactor()));
        m_TxtViewTutorialText.setText(gameView.getGameActivity().getString(tutorialEntry.TextResourceId), tutorialEntry.TextJustified);

        m_ImgViewArrow.setVisibility(View.VISIBLE);
        m_TxtViewTutorialText.setVisibility(View.VISIBLE);
    }

    public void reset() {
        m_CurrentTutorialIndex = 0;
    }

    public ImageView getImgViewArrow() {
        return m_ImgViewArrow;
    }

    public void setImgViewArrow(ImageView imgViewArrow) {
        this.m_ImgViewArrow = imgViewArrow;
    }

    public TextViewEx getTxtViewTutorialText() {
        return m_TxtViewTutorialText;
    }

    public void setTxtViewTutorialText(TextViewEx txtViewTutorialText) {
        this.m_TxtViewTutorialText = txtViewTutorialText;
    }

    private class UpdateTutorialEntryFromUIThread implements Runnable {
        private GameView m_GameView;

        public UpdateTutorialEntryFromUIThread(GameView gameView) {
            m_GameView = gameView;
        }

        @Override
        public void run() {
            TutorialManager.getInstance().updateTutorialEntry(m_GameView);
        }
    }
}
