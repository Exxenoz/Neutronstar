package at.autrage.projects.zeta.persistence;


public abstract class Table {
    public abstract String getTableName();
    public abstract String createTableSQLString();
    public abstract String deleteTableSQLString();
}
