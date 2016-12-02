package at.autrage.projects.zeta.module;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import at.autrage.projects.zeta.persistence.HighscoreTable;
import at.autrage.projects.zeta.persistence.SQLiteHelper;
import at.autrage.projects.zeta.persistence.Table;
import at.autrage.projects.zeta.persistence.TableEntry;

/**
 * This class creates a direct connection to a SQLite Database.
 */
public class Database {
    public enum Tables {
        HighscoreTable,
    }

    private static final Map<Tables, Table> tables = new HashMap<Tables, Table>() {
        {
            put(Tables.HighscoreTable, new HighscoreTable());
        }
    };

    public static Map<Tables, Table> getTables() {
        return tables;
    }

    public static Table getTable(Tables tableId) {
        return tables.get(tableId);
    }

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

    /**
     * Opens a writable database connection
     * @throws SQLException
     */
    public void open() throws SQLException {
        m_Database = m_DBHelper.getWritableDatabase();

        Logger.D("Database connection opened.");
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
        entry.ID = m_Database.insert(entry.getTableName(), null, entry.write());

        Logger.D(String.format("Inserted new table entry (id: %d) to table %s.", entry.ID, entry.getTableName()));
    }

    /**
     * Deletes an entry from the database
     * @param entry has to be a value which extends TableEntry
     */
    public void deleteTableEntry(TableEntry entry) {
        m_Database.delete(entry.getTableName(), entry.getColumnNames()[0] + " = " + entry.ID, null);

        Logger.D(String.format("Removed table entry (id: %d) from table %s.", entry.ID, entry.getTableName()));
    }

    /**
     * Selects all entries from a specified database table.
     *
     * @param table the object to the database table.
     * @return a list of all entries from the specified table.
     */
    public <T extends Table<TEntry>, TEntry extends TableEntry<T>>
    List<TEntry> selectTableEntries(Table<TEntry> table, String selection, String[] selectionArgs, String orderBy) {
        List<TEntry> tableEntries = new ArrayList<>();

        Cursor cursor = m_Database.query(table.getTableName(), table.getColumnNames(), selection, selectionArgs, null, null, orderBy);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            TEntry tableEntry = table.createEntry();
            tableEntry.read(cursor);
            tableEntries.add(tableEntry);

            cursor.moveToNext();
        }

        cursor.close();

        return tableEntries;
    }

    /**
     * Selects all entries from a specified database table ordered by the specified parameter.
     *
     * @param table the object to the database table.
     * @param orderBy how to order the rows.
     * @return a list of all entries from the specified table.
     */
    public <T extends Table<TEntry>, TEntry extends TableEntry<T>>
    List<TEntry> selectTableEntriesOrdered(Table<TEntry> table, String orderBy) {
        return selectTableEntries(table, null, null, orderBy);
    }
}
