package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.SoundManager;

public class HighscoreActivity extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        m_CurrentActivity = Activities.HighscoreActivity;

        HighscoreActivity.BackButtonAreaListener backButtonAreaListener = new HighscoreActivity.BackButtonAreaListener(this);
        HighscoreActivity.NewGameButtonAreaListener newGameButtonAreaListener = new HighscoreActivity.NewGameButtonAreaListener(this);

        Button btnAreaBack = (Button)findViewById(R.id.btnAreaBack);
        Button btnAreaBackIcon = (Button)findViewById(R.id.btnAreaBackIcon);
        Button btnAreaNewGame = (Button)findViewById(R.id.btnAreaNewGame);
        Button btnAreaNewGameIcon = (Button)findViewById(R.id.btnAreaNewGameIcon);

        btnAreaBack.setOnClickListener(backButtonAreaListener);
        btnAreaBackIcon.setOnClickListener(backButtonAreaListener);
        btnAreaNewGame.setOnClickListener(newGameButtonAreaListener);
        btnAreaNewGameIcon.setOnClickListener(newGameButtonAreaListener);

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_highscore));
    }

    private class BackButtonAreaListener implements View.OnClickListener {

        private HighscoreActivity m_OwnerActivity;

        public BackButtonAreaListener(HighscoreActivity ownerActivity) {
            m_OwnerActivity = ownerActivity;
        }

        @Override
        public void onClick(View v) {
            Log.d("PNE::Debug", "Clicked Back Button...");

            SoundManager.getInstance().PlaySFX(R.raw.sfx_button_back);

            // Close current activity
            finish();

            // Start slide animation
            overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
        }
    }

    private class NewGameButtonAreaListener implements View.OnClickListener {

        private HighscoreActivity m_OwnerActivity;

        public NewGameButtonAreaListener(HighscoreActivity ownerActivity) {
            m_OwnerActivity = ownerActivity;
        }

        @Override
        public void onClick(View v) {
            Log.d("PNE::Debug", "Clicked New Game Button...");

            SoundManager.getInstance().PlaySFX(R.raw.sfx_button_select);

            Intent redirectIntent = new Intent(m_OwnerActivity, GameActivity.class);
            startActivity(redirectIntent);

            // Start slide animation
            m_OwnerActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    }
}
