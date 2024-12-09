package uk.co.alt236.usbinfo.database.ext

import android.content.Context
import java.io.File

internal object ContextExt {
    fun Context.getDatabaseRoot() = File(this.filesDir, "databases2/");
}