package at.autrage.projects.zeta.module;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import at.autrage.projects.zeta.persistence.HighscoreTable;
import at.autrage.projects.zeta.persistence.HighscoreTableEntry;
import at.autrage.projects.zeta.persistence.SQLiteHelper;
import at.autrage.projects.zeta.persistence.Table;
import at.autrage.projects.zeta.persistence.TableEntry;

/**
 * This class creates a direct connection to a SQLite Database.
 */
public class Database {
    public static final int HighscoreTable = 0;

    private static Table[] tables = new Table[]{
            new HighscoreTable()
    };

    // Database fields
    private SQLiteDatabase m_Database;
    private SQLiteHelper m_DBHelper;

    private static Database m_Instance = null;

    public static void initialize(Context c) {
        if (m_Instance == null) {
            m_Instance = new Database(c);
        }
    }

    public static Database getInstance() {
        return m_Instance;
    }

    private Database(Context context) {
        m_DBHelper = new SQLiteHelper(context);
    }

    public static Table[] getTables() {
        return tables;
    }

    public static Table getTable(int tableIdx) {
        return tables[tableIdx];
    }

    /**
     * Opens a writable database connection
     * @throws SQLException
     */
    public void open() throws SQLException {
        m_Database = m_DBHelper.getWritableDatabase();
        Log.d("PNE::Debug", "Database connection opened.");
    }

    /**
     * Closes tha database connection
     */
    public void close() {
        m_DBHelper.close();
    }

    /**
     * Inserts an entry into the database
     * @param entry has to be a value which extends TableEntry
     */
    public void insertTableEntry(TableEntry entry) {

        long insertId = m_Database.insert(entry.getTableName(), null,
                entry.getContentValues());
        entry.ID = insertId;

        Log.d("PNE::Debug", String.format("Inserted new table entry (id: %d) to table %s.", insertId, entry.getTableName()));
    }

    /**
     * Deletes an entry from the database
     * @param entry has to be a value which extends TableEntry
     */
    public void deleteTableEntry(TableEntry entry) {
        long id = entry.ID;

        m_Database.delete(entry.getTableName(), entry.getColumnNames()[0]
                + " = " + id, null);

        Log.d("PNE::Debug", String.format("Removed table entry (id: %d) from table %s.", id, entry.getTableName()));
    }

    /**
     * Selects all entries from table highscores
     * @param table the object ob the highscore table
     * @return a list of all entries from the highscores table
     */
    public List<HighscoreTableEntry> selectAllHighscoreTableEntries(HighscoreTable table) {
        List<HighscoreTableEntry> tableEntries = new ArrayList<HighscoreTableEntry>();

        Cursor cursor = m_Database.query(table.getTableName(),
                table.getColumnNames(), null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            HighscoreTableEntry highscoreTableEntry = getHighscoreTableEntryFromCursor(cursor, table);
            tableEntries.add(highscoreTableEntry);

            cursor.moveToNext();

            Log.d("PNE::Debug",
                    String.format("Loaded highscore entry (ID: %d, Level: %d, Score: %d, Date: %s)",
                            highscoreTableEntry.ID,
                            highscoreTableEntry.Level,
                            highscoreTableEntry.Score,
                            highscoreTableEntry.Date
                    )
            );
        }

        // make sure to close the cursor
        cursor.close();
        return tableEntries;
    }

    /**
     * Converts the curser object to an valid table entry of the highscore table
     * @param cursor object which contains the data read from the SQLite database
     * @param table object wich contains the hichscore table
     * @return a valid highscore table entry
     */
    private HighscoreTableEntry getHighscoreTableEntryFromCursor(Cursor cursor, HighscoreTable table) {
        HighscoreTableEntry highscoreTableEntry = new HighscoreTableEntry(table);
        highscoreTableEntry.ID = cursor.getInt(0);
        highscoreTableEntry.Level = cursor.getInt(1);
        highscoreTableEntry.Score = cursor.getInt(2);
        highscoreTableEntry.Date  = cursor.getString(3);
        return highscoreTableEntry;
    }
}
