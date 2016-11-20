package at.autrage.projects.zeta.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import at.autrage.projects.zeta.module.Database;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "at.autrage.projects.zeta.db";
    private static final int DATABASE_VERSION = 1;

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String sql = "";

        for (Table table : Database.getTables()) {
            sql += table.createTableSQLString();
        }

        database.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO update with new logger
        Log.w(SQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");

        String sql = "";

        for (Table table : Database.getTables()) {
            sql += table.deleteTableSQLString();
        }

        db.execSQL(sql);
        onCreate(db);
    }
}
