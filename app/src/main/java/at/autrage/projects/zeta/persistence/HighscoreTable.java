package at.autrage.projects.zeta.persistence;


public class HighscoreTable extends Table {

    @Override
    public String getTableName() {
        return "highscores";
    }

    @Override
    public String createTableSQLString() {
        return String.format("CREATE TABLE IF NOT EXISTS %s (Level INTEGER, Score INTEGER, Date TEXT);", getTableName());
    }

    @Override
    public String deleteTableSQLString() {
        return String.format("DROP TABLE %s;", getTableName());
    }
}
