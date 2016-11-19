package at.autrage.projects.zeta.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.view.GameView;

/**
 * This activity represents the game view and holds the {@link GameView} object.
 */
public class GameActivity extends SuperActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

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
