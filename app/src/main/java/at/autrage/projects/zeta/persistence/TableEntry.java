package at.autrage.projects.zeta.persistence;


import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public abstract class TableEntry {
    public long ID;

    protected Table m_Table;

    public TableEntry(Table table) {
        m_Table = table;
    }

    public String getTableName() {
        return m_Table.getTableName();
    }

    public String[] getColumnNames() {
        return m_Table.getColumnNames();
    }

    public abstract ContentValues getContentValues();
}
