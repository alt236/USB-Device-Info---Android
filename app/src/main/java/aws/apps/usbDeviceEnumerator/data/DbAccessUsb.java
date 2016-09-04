/*******************************************************************************
 * Copyright 2011 Alexandros Schillings
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package aws.apps.usbDeviceEnumerator.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import aws.apps.usbDeviceEnumerator.BuildConfig;
import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.common.DialogFactory;
import aws.apps.usbDeviceEnumerator.util.NotifyUser;

public class DbAccessUsb implements DataAccess {

    public final static String UNKNOWN_RESULT = "not in database";
    private final String TAG = this.getClass().getName();
    private Context context;

    private String fileFullPath = "";

    private SQLiteDatabase db;

    public DbAccessUsb(Context context) {
        this.context = context;
        doPathStuff();
    }

    public boolean doDBChecks() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            Log.d(TAG, "^ SD card not available.");
            DialogFactory.createOkDialog(context,
                    R.string.sd_error,
                    R.string.sd_not_available)
                    .show();
            return false;
        }

        if (!new File(fileFullPath).exists()) {
            DialogFactory.createOkDialog(context,
                    R.string.alert_db_not_found_title,
                    R.string.alert_db_not_found_instructions)
                    .show();
            Log.e(TAG, "^ Database not found: " + fileFullPath);
            return false;
        }

        return true;
    }

    private void doPathStuff() {
        final File baseDir = StorageUtils.getExternalStorageLocation(context);

        if (baseDir == null) {
            fileFullPath = "";
        } else {
            fileFullPath = new File(baseDir, BuildConfig.USB_DB_FILE_NAME).getAbsolutePath();
        }
    }

    private Cursor executeQuery(String table, String[] fields, String where, String order) {

        try {
            db = SQLiteDatabase.openDatabase(fileFullPath, null, SQLiteDatabase.OPEN_READONLY);

            if (!db.isOpen()) {
                Log.e(TAG, "^ DB was not opened!");
                NotifyUser.notify(context, context.getString(R.string.error_could_not_open_db));
                return null;
            }

            return db.query(table, fields, where, null, null, null, order);
        } catch (Exception e) {
            Log.e(TAG, "^ executeQuery(): " + e.getMessage());
            if (db.isOpen()) {
                db.close();
            }
        }
        return null;
    }

    @Override
    public String getFilePath() {
        return fileFullPath;
    }

    @Override
    public String getUrl() {
        return BuildConfig.USB_DB_URL;
    }

    public String getProduct(String VID, String PID) {
        String result = "";
        Cursor cur = executeQuery("usb",
                new String[]{"vid", "vendor_name", "did", "device_name", "ifid", "interface_name"},
                "did='" + PID + "' AND vid='" + VID + "'",
                "vid, did, ifid ASC");

        if (cur != null) {
            //Log.d(TAG, "^ getProduct(" + PID + "): " + cur.getCount());
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                result = cur.getString(cur.getColumnIndex("device_name"));
                cur.close();
                db.close();
            } else {
                result = "not in db";
            }
            if (!cur.isClosed()) {
                cur.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }

        return tryNull(result, UNKNOWN_RESULT);
    }

    public String getVendor(String VID) {
        String result = "";
        Cursor cur = executeQuery("usb",
                new String[]{"vid", "vendor_name", "did", "device_name", "ifid", "interface_name"},
                "vid='" + VID + "' AND did=''",
                "vid, did, ifid ASC");

        if (cur != null) {
            //Log.d(TAG, "^ getVendor(" + VID + "): " + cur.getCount());
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                result = cur.getString(cur.getColumnIndex("vendor_name"));
                cur.close();
                db.close();
            } else {
                result = UNKNOWN_RESULT;
            }

            if (!cur.isClosed()) {
                cur.close();
            }
            if (db.isOpen()) {
                db.close();
            }
        }

        return tryNull(result, UNKNOWN_RESULT);
    }


    private String tryNull(String suspect, String defaultString) {
        if (suspect == null) {
            return defaultString;
        }
        return suspect;
    }
}
