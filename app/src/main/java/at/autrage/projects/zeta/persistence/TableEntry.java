package at.autrage.projects.zeta.persistence;


import android.content.ContentValues;
import android.database.Cursor;

public abstract class TableEntry<T extends Table> {
    public long ID;

    protected T m_Table;

    public TableEntry(T table) {
        m_Table = table;
    }

    public String getTableName() {
        return m_Table.getTableName();
    }

    public String[] getColumnNames() {
        return m_Table.getColumnNames();
    }

    public abstract ContentValues write();
    public abstract void read(Cursor cursor);
}
