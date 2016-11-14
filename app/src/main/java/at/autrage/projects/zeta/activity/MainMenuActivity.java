package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import at.autrage.projects.zeta.R;

public class MainMenuActivity extends SuperActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        NewGameButtonAreaListener newGameButtonAreaListener = new NewGameButtonAreaListener(this);

        Button btnAreaNewGame = (Button)findViewById(R.id.btnAreaNewGame);
        Button btnAreaNewGameIcon = (Button)findViewById(R.id.btnAreaNewGameIcon);

        btnAreaNewGame.setOnClickListener(newGameButtonAreaListener);
        btnAreaNewGameIcon.setOnClickListener(newGameButtonAreaListener);
    }

    private class NewGameButtonAreaListener implements View.OnClickListener {

        private MainMenuActivity m_OwnerActivity;

        public NewGameButtonAreaListener(MainMenuActivity ownerActivity) {
            m_OwnerActivity = ownerActivity;
        }

        @Override
        public void onClick(View v) {
            Intent redirectIntent = new Intent(m_OwnerActivity, GameActivity.class);
            startActivity(redirectIntent);

            m_OwnerActivity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
        }
    }
}
