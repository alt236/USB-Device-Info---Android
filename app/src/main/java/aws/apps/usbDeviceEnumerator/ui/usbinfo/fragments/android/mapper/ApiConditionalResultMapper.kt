package aws.apps.usbDeviceEnumerator.ui.usbinfo.fragments.android.mapper

import android.content.res.Resources
import aws.apps.usbDeviceEnumerator.R
import uk.co.alt236.androidusbmanager.result.ApiConditionalResult
import javax.inject.Inject

class ApiConditionalResultMapper @Inject constructor(private val resources: Resources) {

    fun map(input: ApiConditionalResult<*>): String {
        return when (input) {
            ApiConditionalResult.ApiTooLow -> resources.getString(R.string.error_device_api_too_low)
            is ApiConditionalResult.Error<*> -> input.error::class.java.simpleName
            is ApiConditionalResult.Success -> input.result.toString()
        }
    }

}