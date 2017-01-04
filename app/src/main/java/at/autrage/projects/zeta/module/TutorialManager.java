package at.autrage.projects.zeta.module;

import android.content.Intent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.MainMenuActivity;
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
        public int ArrowAlignmentX;
        public int ArrowAlignmentY;
        public boolean ArrowDirectionDown;
        public boolean ArrowVisible;

        public int TextResourceId;
        public int TextPositionX;
        public int TextPositionY;
        public int TextBoxWidth;
        public int TextAlignmentX;
        public int TextAlignmentY;

        public TutorialEntry(int arrowPositionX, int arrowPositionY, int arrowAlignmentX, int arrowAlignmentY, boolean arrowDirectionDown, boolean arrowVisible,
                             int textResourceId, int textPositionX, int textPositionY, int textBoxWidth, int textAlignmentX, int textAlignmentY) {
            ArrowPositionX = arrowPositionX;
            ArrowPositionY = arrowPositionY;
            ArrowAlignmentX = arrowAlignmentX;
            ArrowAlignmentY = arrowAlignmentY;
            ArrowDirectionDown = arrowDirectionDown;
            ArrowVisible = arrowVisible;

            TextResourceId = textResourceId;
            TextPositionX = textPositionX;
            TextPositionY = textPositionY;
            TextBoxWidth = textBoxWidth;
            TextAlignmentX = textAlignmentX;
            TextAlignmentY = textAlignmentY;
        }
    }

    private ImageView m_ImgViewTutorialArrow;
    private TextView m_TxtViewTutorial;

    private TutorialEntry[] m_TutorialEntries = new TutorialEntry[] {
        /*00*/ new TutorialEntry(0, 0, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, false, R.string.tv_desc_welcome, 0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.CENTER_VERTICAL),
        /*01*/ new TutorialEntry(896, 274, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_planet1, 40, 128, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*02*/ new TutorialEntry(309, 725, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_weapon1, 40, 400, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*03*/ new TutorialEntry(309, 725, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_weapon2, 40, 400, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_BOTTOM),
        /*04*/ new TutorialEntry(210, 725, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_small_rocket1, 40, 525, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*05*/ new TutorialEntry(210, 725, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_small_rocket2, 40, 475, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*06*/ new TutorialEntry(394, 795, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_big_rocket1, 218, 560, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*07*/ new TutorialEntry(896, 274, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_asteroid1, 40, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*08*/ new TutorialEntry(896, 274, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, false, R.string.tv_desc_asteroid3, 40, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*09*/ new TutorialEntry(896, 274, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_asteroid4, 40, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*10*/ new TutorialEntry(896, 274, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, false, R.string.tv_desc_asteroid2, 40, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*11*/ new TutorialEntry(200, 110, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_population1, 40, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*12*/ new TutorialEntry(200, 110, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_population2, 40, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*13*/ new TutorialEntry(580, 110, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_money1, 404, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*14*/ new TutorialEntry(580, 110, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_money2, 404, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*15*/ new TutorialEntry(1230, 110, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_score1, 1054, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*16*/ new TutorialEntry(1590, 110, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, true, R.string.tv_desc_time1, 1414, 250, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*17*/ new TutorialEntry(16, 812, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_pause, 40, 542, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*18*/ new TutorialEntry(1776, 812, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, true, true, R.string.tv_desc_mute, 1400, 542, 480, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP),
        /*19*/ new TutorialEntry(0, 0, RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.ALIGN_PARENT_TOP, false, false, R.string.tv_desc_finish, 0, 0, ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.CENTER_VERTICAL)
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

        m_TxtViewTutorial.setText(gameView.getGameActivity().getString(tutorialEntry.TextResourceId));
        Util.setViewWidth(m_TxtViewTutorial, tutorialEntry.TextBoxWidth);
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
