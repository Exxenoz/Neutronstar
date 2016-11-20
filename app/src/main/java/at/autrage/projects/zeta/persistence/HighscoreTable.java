package at.autrage.projects.zeta.persistence;


import android.util.Pair;

public class HighscoreTable extends Table {

    public static final String TABLE_NAME = "highscores";

    public static final String[][] COLUMNS = new String[][] {
            {"ID", "INTEGER PRIMARY KEY AUTOINCREMENT"},
            {"Level", "INTEGER NOT NULL"},
            {"Score", "INTEGER NOT NULL"},
            {"Date", "TEXT NOT NULL"}
    };

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[][] getColumns() {
        return COLUMNS;
    }
}
