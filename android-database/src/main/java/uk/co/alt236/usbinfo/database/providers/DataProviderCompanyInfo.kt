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
package uk.co.alt236.usbinfo.database.providers

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import uk.co.alt236.usbinfo.database.BuildConfig
import uk.co.alt236.usbinfo.database.ext.ClosableExt.closeSafe
import uk.co.alt236.usbinfo.database.ext.ContextExt.getDatabaseRoot
import uk.co.alt236.usbinfo.database.ext.SqliteDatabaseExt.executeQuery
import uk.co.alt236.usbinfo.database.model.DbResult
import java.io.File

class DataProviderCompanyInfo(context: Context) : AbstractDataProvider(context),
    DataProvider {
    override var dataFilePath: String = ""

    init {
        val baseDir: File = context.getDatabaseRoot()
        this.dataFilePath = File(baseDir, BuildConfig.COMPANY_DB_FILE_NAME).absolutePath
    }

    override val url: String
        get() = BuildConfig.COMPANY_DB_URL

    fun getLogoName(companyNameString: String): DbResult<String?> {
        return when (val dbResult = openDatabase(dataFilePath)) {
            DbResult.DbFailedToOpen -> DbResult.DbFailedToOpen
            DbResult.DbNotPresent -> DbResult.DbNotPresent
            is DbResult.ErrorGeneric -> DbResult.ErrorGeneric(dbResult.error)
            is DbResult.Success -> getLogoNameFromDatabase(dbResult.result, companyNameString)
        }
    }

    private fun getLogoNameFromDatabase(
        db: SQLiteDatabase,
        companyNameString: String
    ): DbResult<String?> {
        val cursorResult = db.executeQuery(
            table = "companies, company_name_spellings",
            fields = arrayOf("companies.logo"),
            selection = "company_name_spellings.company_name=? AND company_name_spellings.companyId=companies._id",
            selectionArgs = arrayOf(companyNameString),
            order = "companies.logo ASC"
        )
        val result = cursorResult.getStringResult("logo", closeAfter = true)
        db.closeSafe()
        return result
    }
}
