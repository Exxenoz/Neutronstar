package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.Database;
import at.autrage.projects.zeta.module.SoundManager;

/**
 * This activity represents the starting point of the application.
 */
public class MainActivity extends SuperActivity {
    private static String m_SeparatorForNumbers = ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set separator for numbers from strings.xml
        m_SeparatorForNumbers = getString(R.string.separator_for_numbers);

        // Initialize database
        Database.initialize(this);

        // Initialize sound manager
        SoundManager.getInstance().initialize(this);

        Intent redirectIntent = new Intent(this, IntroActivity.class);
        startActivity(redirectIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        m_CurrentActivity = Activities.MainActivity;
    }

    public static String getSeparatorForNumbers() {
        return m_SeparatorForNumbers;
    }
}
