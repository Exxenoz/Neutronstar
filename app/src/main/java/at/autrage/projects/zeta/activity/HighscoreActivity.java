package at.autrage.projects.zeta.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import at.autrage.projects.zeta.R;
import at.autrage.projects.zeta.module.Database;
import at.autrage.projects.zeta.module.SoundManager;
import at.autrage.projects.zeta.persistence.HighscoreTable;
import at.autrage.projects.zeta.persistence.HighscoreTableEntry;

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

        updateHighscoreEntries();

        scaleChildViewsToCurrentResolution((ViewGroup)findViewById(R.id.activity_highscore));
    }

    private void updateHighscoreEntries() {

        TextView[] textViewEntries = new TextView[] {
                (TextView)findViewById(R.id.txtViewEntry1),
                (TextView)findViewById(R.id.txtViewEntry2),
                (TextView)findViewById(R.id.txtViewEntry3),
                (TextView)findViewById(R.id.txtViewEntry4),
                (TextView)findViewById(R.id.txtViewEntry5),
                (TextView)findViewById(R.id.txtViewEntry6),
                (TextView)findViewById(R.id.txtViewEntry7),
                (TextView)findViewById(R.id.txtViewEntry8),
                (TextView)findViewById(R.id.txtViewEntry9),
                (TextView)findViewById(R.id.txtViewEntry10)
        };

        Database.getInstance().open();

        HighscoreTable table = (HighscoreTable)Database.getTable(Database.Tables.HighscoreTable);

        // Add test entries
        /*HighscoreTableEntry entry1 = new HighscoreTableEntry(table);
        entry1.Level = 10;
        entry1.Score = 123123;
        entry1.Date = "11.11.16";
        Database.getInstance().insertTableEntry(entry1);

        HighscoreTableEntry entry2 = new HighscoreTableEntry(table);
        entry2.Level = 4;
        entry2.Score = 1337;
        entry2.Date = "20.12.16";
        Database.getInstance().insertTableEntry(entry2);

        HighscoreTableEntry entry3 = new HighscoreTableEntry(table);
        entry3.Level = 8;
        entry3.Score = 54321;
        entry3.Date = "30.11.16";
        Database.getInstance().insertTableEntry(entry3);*/

        List<HighscoreTableEntry> highscoreTableEntries = Database.getInstance().selectTableEntriesOrdered(table, "Level DESC");

        Database.getInstance().close();

        int i = 0;
        for (; i < textViewEntries.length && i < highscoreTableEntries.size(); i++) {
            TextView textViewEntry = textViewEntries[i];
            if (textViewEntry == null) {
                continue;
            }

            HighscoreTableEntry highscoreEntry = highscoreTableEntries.get(i);
            if (highscoreEntry == null) {
                continue;
            }

            textViewEntry.setText(String.format("%02d | Lvl. %02d | %,07d | %s", i + 1, highscoreEntry.Level, highscoreEntry.Score, highscoreEntry.Date));
        }

        String noentry = getString(R.string.hs_noentry);

        for (; i < textViewEntries.length; i++) {
            TextView textViewEntry = textViewEntries[i];
            if (textViewEntry == null) {
                continue;
            }

            textViewEntry.setText(String.format("%02d | %s", i + 1, noentry));
        }
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
