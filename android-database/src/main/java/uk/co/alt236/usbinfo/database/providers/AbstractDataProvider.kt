package uk.co.alt236.usbinfo.database.providers

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import uk.co.alt236.usbinfo.database.ext.CursorExt.getString
import uk.co.alt236.usbinfo.database.model.DbResult
import java.io.File

abstract class AbstractDataProvider(context: Context) : DataProvider {
    protected val context: Context = context.applicationContext;

    protected val logTag: String = this::class.java.simpleName

    override fun isDataAvailable(): Boolean {
        val path = dataFilePath
        return if (!File(path).exists()) {
            Log.e(logTag, "^ Cannot access: $path")
            false
        } else {
            true
        }
    }

    protected fun openDatabase(dbPath: String): DbResult<SQLiteDatabase> {
        if (!isDataAvailable()) {
            return DbResult.DbNotPresent
        }
        return try {
            DbResult.Success(
                SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY)
            )
        } catch (e: Exception) {
            DbResult.ErrorGeneric(e)
        }
    }

    protected fun DbResult<Cursor>.getStringResult(
        colName: String,
        closeAfter: Boolean = true
    ): DbResult<String?> = when (this) {
        DbResult.DbFailedToOpen -> DbResult.DbFailedToOpen
        DbResult.DbNotPresent -> DbResult.DbNotPresent
        is DbResult.ErrorGeneric -> DbResult.ErrorGeneric(this.error)
        is DbResult.Success -> DbResult.Success(this.result.getString(colName, closeAfter))
    }
}