package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.sharing

import android.util.Log
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import aws.apps.usbDeviceEnumerator.BuildConfig
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.ViewHolder
import javax.inject.Inject

class SharePayloadFactory @Inject constructor() {

    fun getSharePayload(holder: ViewHolder): String {
        val sb = StringBuilder()
        sb.append(tableToString(holder.headerTable))
        sb.append(tableToString(holder.topTable))
        sb.append('\n')
        sb.append(tableToString(holder.firstBottomTable))
        sb.append('\n')
        sb.append(tableToString(holder.secondBottomTable))

        if (BuildConfig.DEBUG) {
            Log.d("SharePayloadFactory", "----\n$sb")
        }

        return sb.toString()
    }

    private fun tableToString(table: TableLayout?): String {
        val sb = StringBuilder()

        if (table == null) {
            return sb.toString()
        }

        for (i in 0..<table.childCount) {
            val row = table.getChildAt(i) as TableRow

            for (j in 0..<row.childCount) {
                val v = row.getChildAt(j)

                try {
                    if (v is TextView) {
                        sb.append(v.text)

                        if (j == 0) {
                            sb.append(" ")
                        }
                    }
                } catch (e: Exception) {
                    sb.append(e.toString())
                    Log.e(TAG, "^ ERROR: tableToString: $e")
                }
            }
            sb.append('\n')
        }

        return sb.toString()
    }

    private companion object {
        val TAG: String = ShareUtils::class.java.simpleName
    }
}
