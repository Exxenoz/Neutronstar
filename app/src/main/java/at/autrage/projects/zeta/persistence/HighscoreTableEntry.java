package at.autrage.projects.zeta.persistence;

import android.content.ContentValues;
import android.provider.BaseColumns;

public class HighscoreTableEntry extends TableEntry {
    public int Level;
    public int Score;
    public String Date;

    public HighscoreTableEntry(Table table) {
        super(table);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        String[] columnNames = m_Table.getColumnNames();

        // Don't insert ID column
        values.put(columnNames[1], Level);
        values.put(columnNames[2], Score);
        values.put(columnNames[3], Date);

        return values;
    }
}
