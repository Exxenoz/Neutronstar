package at.autrage.projects.zeta.persistence;

import android.provider.BaseColumns;

public class HighscoreTableEntry extends TableEntry {

    public int Level;
    public int Score;
    public String Date;

    @Override
    public String getSQLString() {
        return String.format("'%d', '%d', '%s'", Level, Score, Date);
    }
}
