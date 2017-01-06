package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.GameManager;
import at.autrage.projects.zeta.module.Logger;
import at.autrage.projects.zeta.module.SoundManager;

/**
 * This activity contains various buttons to navigate through the main menu.
 */
public class MainMenuActivity extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        NewGameButtonAreaListener newGameButtonAreaListener = new NewGameButtonAreaListener(this);
        HighscoreButtonAreaListener highscoreButtonAreaListener = new HighscoreButtonAreaListener(this);
        TutorialGameButtonAreaListener tutorialGameButtonAreaListener = new TutorialGameButtonAreaListener(this);

        Button btnAreaNewGame = (Button)findViewById(R.id.btnAreaNewGame);
        Button btnAreaNewGameIcon = (Button)findViewById(R.id.btnAreaNewGameIcon);
        Button btnAreaHighscore = (Button)findViewById(R.id.btnAreaHighscore);
        Button btnAreaHighscoreIcon = (Button)findViewById(R.id.btnAreaHighscoreIcon);
        Button btnAreaTutorialGame = (Button)findViewById(R.id.btnAreaHelp);
        Button btnAreaTutorialGameIcon = (Button)findViewById(R.id.btnAreaHelpIcon);

        btnAreaNewGame.setOnClickListener(newGameButtonAreaListener);
        btnAreaNewGameIcon.setOnClickListener(newGameButtonAreaListener);
        btnAreaHighscore.setOnClickListener(highscoreButtonAreaListener);
        btnAreaHighscoreIcon.setOnClickListener(highscoreButtonAreaListener);
        btnAreaTutorialGame.setOnClickListener(tutorialGameButtonAreaListener);
        btnAreaTutorialGameIcon.setOnClickListener(tutorialGameButtonAreaListener);

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_main_menu));
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CurrentActivity = Activities.MainMenuActivity;
    }

    private class NewGameButtonAreaListener implements View.OnClickListener {

        private MainMenuActivity m_OwnerActivity;

        public NewGameButtonAreaListener(MainMenuActivity ownerActivity) {
            m_OwnerActivity = ownerActivity;
        }

        @Override
        public void onClick(View v) {
            Logger.D("Clicked New Game Button...");

            GameManager.getInstance().onStartGame();

            Intent redirectIntent = new Intent(m_OwnerActivity, GameActivity.class);
            startActivity(redirectIntent);

            // Start slide animation
            m_OwnerActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    }

    private class HighscoreButtonAreaListener implements View.OnClickListener {

        private MainMenuActivity m_OwnerActivity;

        public HighscoreButtonAreaListener(MainMenuActivity ownerActivity) {
            m_OwnerActivity = ownerActivity;
        }

        @Override
        public void onClick(View v) {
            Logger.D("Clicked Highscore Button...");

            SoundManager.getInstance().PlaySFX(R.raw.sfx_button_select);

            Intent redirectIntent = new Intent(m_OwnerActivity, HighscoreActivity.class);
            startActivity(redirectIntent);

            // Start slide animation
            m_OwnerActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    }

    private class TutorialGameButtonAreaListener implements View.OnClickListener {

        private MainMenuActivity m_OwnerActivity;

        public TutorialGameButtonAreaListener(MainMenuActivity ownerActivity) {
            m_OwnerActivity = ownerActivity;
        }

        @Override
        public void onClick(View v) {
            Logger.D("Clicked Tutorial Game Button...");

            GameManager.getInstance().onStartTutorialGame();

            Intent redirectIntent = new Intent(m_OwnerActivity, GameActivity.class);
            startActivity(redirectIntent);

            // Start slide animation
            m_OwnerActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    }
}
