package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base

import android.content.res.Resources
import android.graphics.Bitmap
import uk.co.alt236.usbinfo.database.model.DbResult
import uk.co.alt236.usbinfo.database.providers.DataProviderCompanyInfo
import uk.co.alt236.usbinfo.database.providers.DataProviderCompanyLogo
import uk.co.alt236.usbinfo.database.providers.DataProviderUsbInfo

class DataFetcher(
    private val resources: Resources,
    private val dbComp: DataProviderCompanyInfo,
    private val dbUsb: DataProviderUsbInfo,
    private val zipComp: DataProviderCompanyLogo
) {
    fun fetchData(
        vid: String,
        pid: String,
        reportedVendorName: String?,
        callback: Callback
    ) {
        val runnable = Runnable {
            val vendorFromDb = dbUsb.getVendorName(vid)
            val productFromDb = dbUsb.getProductName(vid, pid)
            val bitmap: Bitmap?

            val logoVendorName = if (!vendorFromDb.getValueOrNull().isNullOrEmpty()) {
                vendorFromDb.getValueOrNull()
            } else if (!reportedVendorName.isNullOrEmpty()) {
                reportedVendorName
            } else {
                null
            }

            bitmap = getLogo(logoVendorName)
            callback.onSuccess(
                vendorFromDb.getUiString(),
                productFromDb.getUiString(),
                bitmap
            )
        }

        val thread = Thread(runnable)
        thread.start()
    }


    private fun getLogo(vendorName: String?): Bitmap? = if (vendorName.isNullOrEmpty()) {
        null
    } else {
        val logoName: String? = dbComp.getLogoName(vendorName).getValueOrNull()
        zipComp.getLogoBitmap(logoName)
    }


    private fun DbResult<String?>.getUiString(): String {
        return when (this) {
            DbResult.DbFailedToOpen -> "Error: Failed to open DB"
            DbResult.DbNotPresent -> "DB not present"
            is DbResult.ErrorGeneric -> "Error: ${this.error.message ?: this.error::class.java.simpleName}"
            is DbResult.Success -> this.result ?: "Not found"
        }
    }

    interface Callback {
        fun onSuccess(vendorFromDb: String?, productFromDb: String?, bitmap: Bitmap?)
    }
}
