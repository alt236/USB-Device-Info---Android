package uk.co.alt236.usbinfo.database.ext

import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import uk.co.alt236.usbinfo.database.ext.ClosableExt.closeSafe
import uk.co.alt236.usbinfo.database.model.DbResult
import uk.co.alt236.usbinfo.database.model.DbResult.DbFailedToOpen
import uk.co.alt236.usbinfo.database.model.DbResult.ErrorGeneric

internal object SqliteDatabaseExt {
    private const val TAG = "SqliteDatabaseExt"

    fun SQLiteDatabase.executeQuery(
        table: String,
        fields: Array<String?>,
        selection: String,
        selectionArgs: Array<String?>,
        order: String
    ): DbResult<Cursor> {
        Log.d(
            TAG,
            """ ^ executeQuery():
             table: $table
             fields:${fields.contentToString()}
             selection: $selection
             selectionArgs: ${selectionArgs.contentToString()}
             order:$order
             """.trimIndent()
        )

        return try {
            return if (this.isOpen) {
                val cursor =
                    this.query(table, fields, selection, selectionArgs, null, null, order)
                cursor.moveToFirst()
                DbResult.Success(cursor)
            } else {
                Log.e(TAG, "^ executeQuery(): DB was not opened!")
                DbFailedToOpen
            }
        } catch (e: Exception) {
            Log.e(TAG, "^ executeQuery(): Error: ${e.message}")
            this.closeSafe()
            ErrorGeneric(e)
        }
    }
}