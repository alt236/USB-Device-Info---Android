package aws.apps.usbDeviceEnumerator.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.util.NotifyUser;

/*package*/ class StorageUtils {
    private static final String TAG = StorageUtils.class.getSimpleName();

    public static File getExternalStorageLocation(final Context context) {
        final File[] dirs = ContextCompat.getExternalFilesDirs(context, null);
        final File dir;

        if (dirs == null || dirs.length == 0) {
            dir = null;
        } else {
            dir = dirs[0];
        }

        return dir;
    }

    public static Cursor executeQuery(final Context context,
                                      final String dbPath,
                                      final String table,
                                      final String[] fields,
                                      final String selection,
                                      final String[] selectionArgs,
                                      final String order) {

        SQLiteDatabase db = null;

        try {
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);

            if (!db.isOpen()) {
                Log.e(TAG, "^ DB was not opened!");
                NotifyUser.notify(context, R.string.error_could_not_open_db);
                return null;
            }

            return db.query(table, fields, selection, selectionArgs, null, null, order);
        } catch (Exception e) {
            Log.e(TAG, "^ executeQuery(): " + e.getMessage());
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return null;
    }

    public static String getStringAndClose(final Cursor cursor,
                                           final String colName) {
        final String result;

        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                result = cursor.getString(cursor.getColumnIndex(colName));
                cursor.close();
            } else {
                result = null;
            }

            if (!cursor.isClosed()) {
                cursor.close();
            }
        } else {
            result = null;
        }

        return result;
    }
}
