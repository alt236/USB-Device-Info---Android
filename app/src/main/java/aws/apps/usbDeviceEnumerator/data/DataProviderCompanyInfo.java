/*
 Copyright 2011 Alexandros Schillings
 <p/>
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 <p/>
 http://www.apache.org/licenses/LICENSE-2.0
 <p/>
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package aws.apps.usbDeviceEnumerator.data;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.io.File;

import aws.apps.usbDeviceEnumerator.BuildConfig;

public class DataProviderCompanyInfo implements DataProvider {
    public static final String UNKNOWN_RESULT = "not found";
    private final String TAG = this.getClass().getName();
    private final Context context;

    private String fileFullPath = "";

    public DataProviderCompanyInfo(Context context) {
        final File baseDir = StorageUtils.getStorageRoot(context);
        this.context = context.getApplicationContext();
        this.fileFullPath = new File(baseDir, BuildConfig.COMPANY_DB_FILE_NAME).getAbsolutePath();
    }

    @Override
    public boolean isDataAvailable() {
        final boolean okToAccessData;

        if (!new File(getDataFilePath()).exists()) {
            Log.e(TAG, "^ Cannot access: " + fileFullPath);
            okToAccessData = false;
        } else {
            okToAccessData = true;
        }

        return okToAccessData;
    }

    @Override
    public String getDataFilePath() {
        return fileFullPath;
    }

    @Override
    public String getUrl() {
        return BuildConfig.COMPANY_DB_URL;
    }

    public String getLogoName(String companyNameString) {

        final Cursor cur = StorageUtils.executeQuery(
                context,
                getDataFilePath(),
                "companies, company_name_spellings",
                new String[]{"companies.logo"},
                "company_name_spellings.company_name=? AND company_name_spellings.companyId=companies._id",
                new String[]{companyNameString},
                "companies.logo ASC");

        final String result = StorageUtils.getStringAndClose(cur, "logo");

        return tryNull(result, UNKNOWN_RESULT);
    }

    private static String tryNull(final String suspect,
                                  final String defaultString) {
        if (suspect == null) {
            return defaultString;
        }
        return suspect;
    }
}
