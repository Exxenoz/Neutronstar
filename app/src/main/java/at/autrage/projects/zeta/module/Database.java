package at.autrage.projects.zeta.module;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import at.autrage.projects.zeta.persistence.HighscoreTable;

public class Database {
    private static final String DATABASE_NAME = "highscores.db";
    private static Database m_Instance = null;

    public static Database getInstance() {
        if (m_Instance == null) {
            m_Instance = new Database();
        }

        return m_Instance;
    }

    private SQLiteDatabase m_Database;
    private HighscoreTable m_HighscoreTable;

    private Database() {
        m_HighscoreTable = new HighscoreTable();

        m_Database = SQLiteDatabase.openOrCreateDatabase("/data/data/at.autrage.project.zeta/databases/" + DATABASE_NAME, null);
        m_Database.execSQL(m_HighscoreTable.createTableSQLString());
    }
}
