package aws.apps.usbDeviceEnumerator.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.util.NotifyUser;

/*package*/ class StorageUtils {
    private static final String TAG = StorageUtils.class.getSimpleName();

    @NonNull
    public static File getStorageRoot(final Context context) {
        return new File(context.getFilesDir(), "usbdata/");
    }

    public static Cursor executeQuery(final Context context,
                                      final String dbPath,
                                      final String table,
                                      final String[] fields,
                                      final String selection,
                                      final String[] selectionArgs,
                                      final String order) {

        SQLiteDatabase db = null;

        Log.d(TAG, "^ executeQuery(): Path: " + dbPath + "\n" +
                "table: " + table + "\n" +
                "fields:" + Arrays.toString(fields) + "\n" +
                "selection: " + selection + "\n" +
                "selectionArgs: " + Arrays.toString(selectionArgs) + "\n" +
                "order:" + order);

        try {
            db = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);

            if (!db.isOpen()) {
                Log.e(TAG, "^ executeQuery(): DB was not opened!");
                NotifyUser.notify(context, R.string.error_could_not_open_db);
                return null;
            }

            return db.query(table, fields, selection, selectionArgs, null, null, order);
        } catch (Exception e) {
            Log.e(TAG, "^ executeQuery(): Error: " + e.getMessage());
            if (db != null && db.isOpen()) {
                db.close();
            }
        }
        return null;
    }

    @Nullable
    public static String getStringAndClose(final Cursor cursor,
                                           final String colName) {
        final String result;

        if (cursor == null) {
            return null;
        }

        Log.d(TAG, "^ getStringAndClose(): Column: '" + colName + "', Cursor size:" + cursor.getCount() + ", cols:" + Arrays.toString(cursor.getColumnNames()));

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            result = getStringFromCursor(cursor, colName);
        } else {
            result = null;
        }

        if (!cursor.isClosed()) {
            cursor.close();
        }

        return result;
    }

    private static String getStringFromCursor(Cursor cursor, String colName) {
        final int colIndex = cursor.getColumnIndex(colName);
        if (colIndex < 0) {
            throw new IllegalArgumentException("No column with name '" + colName + "' in cursor");
        }
        return cursor.getString(colIndex);
    }
}
