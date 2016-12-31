package at.autrage.projects.zeta.persistence;

import android.content.ContentValues;
import android.database.Cursor;

public class HighscoreTableEntry extends TableEntry<HighscoreTable> {
    public int Level;
    public int Score;
    public int Date;

    public HighscoreTableEntry(HighscoreTable table) {
        super(table);
    }

    @Override
    public ContentValues write() {
        ContentValues values = new ContentValues();
        String[] columnNames = m_Table.getColumnNames();

        // Don't insert ID column
        values.put(columnNames[1], Level);
        values.put(columnNames[2], Score);
        values.put(columnNames[3], Date);

        return values;
    }

    @Override
    public void read(Cursor cursor) {
        ID = cursor.getInt(0);
        Level = cursor.getInt(1);
        Score = cursor.getInt(2);
        Date  = cursor.getInt(3);
    }
}
