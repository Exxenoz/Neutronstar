package at.autrage.projects.zeta.persistence;


public abstract class Table<TEntry> {
    protected String[] m_ColumnNames;

    public Table() {
        initializeColumnNames();
    }

    private void initializeColumnNames() {
        String[][] columns = getColumns();
        m_ColumnNames = new String[columns.length];

        for (int i = 0, size = columns.length; i < size; i++) {
            m_ColumnNames[i] = columns[i][0];
        }
    }

    public abstract TEntry createEntry();

    public abstract String getTableName();
    public abstract String[][] getColumns();

    public String[] getColumnNames() {
        return m_ColumnNames;
    }

    public String createTableSQLString() {
        String sql = "";
        String[][] columns = getColumns();

        for (int i = 0, size = columns.length; i < size; i++) {
            sql += columns[i][0] + " " + columns[i][1];
            if (i < size - 1) {
                sql += ", ";
            }
        }

        return "CREATE TABLE IF NOT EXISTS " + getTableName() + " (" + sql + ");";
    }

    public String deleteTableSQLString() {
        return String.format("DROP TABLE %s;", getTableName());
    }
}
