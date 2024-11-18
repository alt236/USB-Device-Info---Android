package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.table

import android.annotation.SuppressLint
import android.content.res.Resources
import aws.apps.usbDeviceEnumerator.R
import aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.base.TableWriter
import uk.co.alt236.androidusbmanager.model.AndroidUsbConfiguration
import uk.co.alt236.androidusbmanager.result.ApiConditionalResult

internal class ConfigurationTableBuilder(resources: Resources) : TableBuilder(resources) {

    fun addConfigurations(
        tableWriter: TableWriter,
        result: ApiConditionalResult<List<AndroidUsbConfiguration>>
    ) {
        val title = R.string.configuration_
        when (result) {
            ApiConditionalResult.ApiTooLow -> tableWriter.addDataRow(
                title,
                getString(R.string.error_device_api_too_low)
            )

            is ApiConditionalResult.Error<*> -> tableWriter.addDataRow(
                title,
                result.error::class.java.simpleName
            )

            is ApiConditionalResult.Success -> {
                addConfigurations(tableWriter, result.result)
            }
        }
    }

    @SuppressLint("NewApi") // the ApiConditionalResult handles the API level check
    private fun addConfigurations(tableWriter: TableWriter, result: List<AndroidUsbConfiguration>) {
        val title = R.string.configuration_

        if (result.isEmpty()) {
            tableWriter.addDataRow(title, "no configurations")
        } else {
            for ((index, config) in result.withIndex()) {
                tableWriter.addTitleRow(getString(R.string.configuration_) + index)
                tableWriter.addDataRow(R.string.id_, config.id.toString())
                tableWriter.addDataRow(R.string.name_, config.name)
                tableWriter.addDataRow(R.string.max_power_, config.maxPower.toString())
                tableWriter.addDataRow("Self Powered:", config.isSelfPowered.toString())
                tableWriter.addDataRow("Remote Wakeup:", config.isRemoteWakeup.toString())
                //tableWriter.addDataRow("Interfaces:", config.interfaces.map { it.name }.toString())
            }
        }
    }
}