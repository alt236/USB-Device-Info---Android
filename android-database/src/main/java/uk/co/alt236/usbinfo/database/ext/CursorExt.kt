package uk.co.alt236.usbinfo.database.ext

import android.database.Cursor
import android.util.Log

internal object CursorExt {
    private const val TAG = "CursorExt"

    fun Cursor.getString(colName: String, close: Boolean = false): String? {
        Log.d(
            TAG,
            "^ getStringAndClose(): Column: '$colName', Cursor size:$count, cols:" + columnNames
                .contentToString()
        )

        val colIndex = this.getColumnIndex(colName)
        require(colIndex >= 0) { "No column with name '$colName' in cursor" }

        return if (this.count == 0) {
            null
        } else {
            val result = this.getString(colIndex)
            if (close and !this.isClosed) {
                this.close()
            }
            result
        }
    }
}