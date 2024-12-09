package uk.co.alt236.usbinfo.database.ext

import android.database.Cursor
import java.io.Closeable
import java.io.IOException

internal object ClosableExt {
    fun Closeable?.closeSafe() {
        when {
            this == null -> return
            this is Cursor && this.isClosed -> return
            else -> {
                try {
                    this.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
}