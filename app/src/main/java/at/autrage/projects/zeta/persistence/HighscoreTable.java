package at.autrage.projects.zeta.persistence;

public class HighscoreTable extends Table<HighscoreTableEntry> {

    public static final String TABLE_NAME = "highscores";

    public static final String[][] COLUMNS = new String[][] {
            {"ID",    "INTEGER PRIMARY KEY AUTOINCREMENT"},
            {"Level", "INTEGER NOT NULL"},
            {"Score", "INTEGER NOT NULL"},
            {"Date",  "TEXT NOT NULL"}
    };

    @Override
    public HighscoreTableEntry createEntry() {
        return new HighscoreTableEntry(this);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String[][] getColumns() {
        return COLUMNS;
    }
}
