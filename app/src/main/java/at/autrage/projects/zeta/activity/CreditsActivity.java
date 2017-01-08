package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.Logger;

public class CreditsActivity extends SuperActivity {
    private boolean m_Skipped;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.activity_credits);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeCredits();
            }
        });

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_credits));
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CurrentActivity = Activities.CreditsActivity;
    }

    /**
     * This method closes the credits activity
     * by starting the {@link CreditsActivity}.
     */
    private void closeCredits() {
        if (!m_Skipped) {
            m_Skipped = true;

            // Redirect to main menu activity
            startActivity(new Intent(this, MainMenuActivity.class));

            // Close current activity
            finish();

            Logger.D("Closed credits view...");
        }
    }
}
