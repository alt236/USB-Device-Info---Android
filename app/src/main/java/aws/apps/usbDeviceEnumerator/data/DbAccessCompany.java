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

import aws.apps.usbDeviceEnumerator.R;
import aws.apps.usbDeviceEnumerator.ui.common.DialogFactory;
import aws.apps.usbDeviceEnumerator.util.NotifyUser;

public class DbAccessCompany {
    public final static String UNKNOWN_RESULT = "???";
    private final String TAG = this.getClass().getName();
    private Context context;

    private String localDbLocation = "";
    private String localDbFullPath = "";

    private SQLiteDatabase db;

    public DbAccessCompany(Context context) {
        this.context = context;
        doDbPathStuff();
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

        if (!new File(localDbFullPath).exists()) {
            DialogFactory.createOkDialog(context,
                    R.string.alert_db_not_found_title,
                    R.string.alert_db_not_found_instructions)
                    .show();
            Log.e(TAG, "^ Database not found: " + localDbFullPath);
            return false;
        }

        return true;
    }

    private void doDbPathStuff() {
        localDbLocation = Environment.getExternalStorageDirectory() + context.getString(R.string.sd_db_location_company);
        localDbFullPath = localDbLocation + context.getString(R.string.sd_db_name_company);
    }

    private Cursor executeQuery(String table, String[] fields, String where, String order) {

        try {
            db = SQLiteDatabase.openDatabase(localDbFullPath, null, SQLiteDatabase.OPEN_READONLY);

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

    public String getLocalDbFullPath() {
        return localDbFullPath;
    }

    public String getLocalDbLocation() {
        return localDbLocation;
    }

    public String getLogo(String CompanyNameString) {
        String result = "";
        Cursor cur = executeQuery("companies, company_name_spellings",
                new String[]{"companies.logo"},
                "company_name_spellings.company_name='" + CompanyNameString +
                        "' AND company_name_spellings.companyId=companies._id",
                "companies.logo ASC");

        if (cur != null) {
            if (cur.getCount() > 0) {
                cur.moveToFirst();
                result = cur.getString(cur.getColumnIndex("logo"));
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
