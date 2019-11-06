package lunainc.mx.com.googlefit.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * DB helper.
 */
public class SimpleDBHelper {

    private final static String TAG = "MULTI-THREAD-DB-HELPER";

    private MultiThreadSQLiteOpenHelper dbHelper;

    public static final SimpleDBHelper INSTANCE = new SimpleDBHelper();

    private SimpleDBHelper() {

    }

    public SQLiteDatabase open(Context context) {
        synchronized(this) {
            Log.d(TAG, "asking for opening");
            if (dbHelper == null) {
                dbHelper = new CupboardSQLiteOpenHelper(context);
            }
            return dbHelper.getWritableDatabase(); // getting a cached database
        }
    }

    public void close() {
        synchronized(this) {
            Log.d(TAG, "asking for closing");
            if (dbHelper != null) {
                // Ask for closing database
                if (dbHelper.closeIfNeeded()) {
                    dbHelper = null; // database closed: free resource for GC
                }
            }
        }
    }
}
