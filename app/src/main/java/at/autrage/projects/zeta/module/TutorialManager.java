package at.autrage.projects.zeta.module;


import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.activity.GameActivity;
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
        new TutorialEntry(0, 0, false, false, R.string.tv_desc_welcome, 510, 500, 900, false),
        new TutorialEntry(200, 110, false, true, R.string.tv_desc_population1, 40, 250, 480, true),
        new TutorialEntry(200, 110, false, true, R.string.tv_desc_population2, 40, 250, 480, true),
        new TutorialEntry(580, 110, false, true, R.string.tv_desc_money1, 404, 250, 480, true),
        new TutorialEntry(580, 110, false, true, R.string.tv_desc_money2, 404, 250, 480, true)
    };

    private int m_CurrentTutorialIndex;

    private TutorialManager() {
        reset();
    }

    public void startTutorial(GameView gameView) {
        reset();

        Time.setTimeScale(0f);

        updateTutorialEntry(gameView);

        // Call game views update method to initialize UI elements
        gameView.update();
    }

    public void onClickEvent(GameView gameView) {
        if (m_ImgViewArrow == null) {
            return;
        }

        if (m_TxtViewTutorialText == null) {
            return;
        }

        m_CurrentTutorialIndex++;

        if (m_CurrentTutorialIndex >= m_TutorialEntries.length) {
            m_CurrentTutorialIndex = m_TutorialEntries.length - 1;
            return;
        }

        updateTutorialEntry(gameView);

        return;
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

        Util.setLeftAndTopMargin(m_ImgViewArrow, tutorialEntry.ArrowPositionX, tutorialEntry.ArrowPositionY);
        Util.setLeftAndTopMargin(m_TxtViewTutorialText, tutorialEntry.TextPositionX, tutorialEntry.TextPositionY);

        Util.setViewWidth(m_TxtViewTutorialText, tutorialEntry.TextBoxWidth);
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
}
