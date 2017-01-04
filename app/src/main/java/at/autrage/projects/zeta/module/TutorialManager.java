package at.autrage.projects.zeta.module;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.MainMenuActivity;
import at.autrage.projects.zeta.activity.SuperActivity;
import at.autrage.projects.zeta.model.Asteroid;
import at.autrage.projects.zeta.model.Enemy;
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

    private ImageView m_ImgViewTutorialArrow;
    private TextView m_TxtViewTutorial;

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
    }

    public void startTutorial(GameView gameView) {
        setCurrentTutorialIndex(0);

        updateTutorialEntry(gameView);

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
            case 1: // Show planet
                if (gameView.getPlayer() != null) {
                    gameView.getPlayer().setVisible(true);
                }
                break;
            case 7: // Asteroid state
            case 9: // Asteroid repeat state
                gameView.getEnemySpawner().spawnTutorialAsteroid();
                break;
            case 11: // Hide planet
                if (gameView.getPlayer() != null) {
                    gameView.getPlayer().setVisible(false);
                }
                break;
        }

        updateTutorialEntry(gameView);
    }

    public void onDestroyEnemy(Enemy enemy) {
        if (enemy instanceof Asteroid) {
            if (enemy.getHealth() > 0) {
                setCurrentTutorialIndex(8);
            }
            else {
                setCurrentTutorialIndex(10);
            }

            enemy.getGameView().getGameActivity().runOnUiThread(new UpdateTutorialEntryFromUIThread(enemy.getGameView()));
        }
    }

    private void updateTutorialEntry(GameView gameView) {
        if (m_CurrentTutorialIndex < 0 ||
            m_CurrentTutorialIndex >= m_TutorialEntries.length) {
            return;
        }

        if (m_ImgViewTutorialArrow == null ||
            m_TxtViewTutorial == null) {
            return;
        }

        TutorialEntry tutorialEntry = m_TutorialEntries[m_CurrentTutorialIndex];

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

        m_TxtViewTutorial.setAlpha(1f);

        Util.setViewLeftAndTopMargin(m_ImgViewTutorialArrow, tutorialEntry.ArrowPositionX, tutorialEntry.ArrowPositionY);
        Util.setViewLeftAndTopMargin(m_TxtViewTutorial, tutorialEntry.TextPositionX, tutorialEntry.TextPositionY);

        Util.setViewWidth(m_TxtViewTutorial, tutorialEntry.TextBoxWidth);
        m_TxtViewTutorial.setText(gameView.getGameActivity().getString(tutorialEntry.TextResourceId));
    }

    public ImageView getImgViewTutorialArrow() {
        return m_ImgViewTutorialArrow;
    }

    public void setImgViewTutorialArrow(ImageView imgViewTutorialArrow) {
        this.m_ImgViewTutorialArrow = imgViewTutorialArrow;
    }

    public TextView getTxtViewTutorial() {
        return m_TxtViewTutorial;
    }

    public void setTxtViewTutorial(TextView txtViewTutorial) {
        this.m_TxtViewTutorial = txtViewTutorial;
    }

    public int getCurrentTutorialIndex() {
        return m_CurrentTutorialIndex;
    }

    private void setCurrentTutorialIndex(int currentTutorialIndex) {
        this.m_CurrentTutorialIndex = currentTutorialIndex;

        Logger.D("Set current tutorial index to %d", m_CurrentTutorialIndex);
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
