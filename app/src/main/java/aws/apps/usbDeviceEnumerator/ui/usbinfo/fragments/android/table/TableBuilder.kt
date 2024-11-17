package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.table

import android.content.res.Resources
import androidx.annotation.StringRes
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.TableWriter

abstract class TableBuilder(protected val resources: Resources) {
    protected fun TableWriter.addDataRow(@StringRes title: Int, value: String?) {
        this.addDataRow(getString(title), value)
    }

    protected fun getString(@StringRes id: Int): String {
        return resources.getString(id)
    }
}