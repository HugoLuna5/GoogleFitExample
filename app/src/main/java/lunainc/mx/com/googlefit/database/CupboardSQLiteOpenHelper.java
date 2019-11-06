package lunainc.mx.com.googlefit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import lunainc.mx.com.googlefit.model.Workout;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class CupboardSQLiteOpenHelper extends MultiThreadSQLiteOpenHelper {
    private static final String DATABASE_NAME = "googlefitexample.db";
    private static final int DATABASE_VERSION = 2;

    static {
        // register our models
        cupboard().register(Workout.class);
    }

    public CupboardSQLiteOpenHelper(Context context) {
        super(context.getApplicationContext(), DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // this will ensure that all tables are created
        cupboard().withDatabase(db).createTables();
        // add indexes and other database tweaks
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this will upgrade tables, adding columns and new tables.
        // Note that existing columns will not be converted
        cupboard().withDatabase(db).upgradeTables();
        // do migration work
    }
}