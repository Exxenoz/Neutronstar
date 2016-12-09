package at.autrage.projects.zeta.activity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_game));
    }

    @Override
    public void onClick(View v) {
        // Close current activity
        finish();

        // Start slide animation
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
    }
}
