package at.autrage.projects.zeta.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.view.GameView;
import at.autrage.projects.zeta.view.GameViewUI;

/**
 * This activity represents the game view and holds the {@link GameView} object.
 */
public class GameActivity extends SuperActivity implements View.OnClickListener {
    /** Reference to our {@link GameView} object. */
    GameView m_GameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        m_CurrentActivity = Activities.GameActivity;

        GameViewUI gameViewUI = new GameViewUI();
        gameViewUI.TxtViewFPS = (TextView)findViewById(R.id.txtViewFPS);

        m_GameView = (GameView)findViewById(R.id.gameView);
        m_GameView.setGameViewUI(gameViewUI);

        Button btnFinishGame = (Button)findViewById(R.id.btnFinishGame);
        btnFinishGame.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Close current activity
        finish();

        // Start slide animation
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
