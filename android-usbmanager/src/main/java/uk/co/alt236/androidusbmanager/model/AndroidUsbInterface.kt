package uk.co.alt236.androidusbmanager.model

import android.hardware.usb.UsbInterface
import android.os.Build
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uk.co.alt236.androidusbmanager.result.ApiConditionalResult

@Parcelize
data class AndroidUsbInterface(val rawInterface: UsbInterface) : Parcelable {

    val id: Int get() = rawInterface.id

    val alternateSetting: ApiConditionalResult<Int>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ApiConditionalResult.Success(rawInterface.alternateSetting)
        } else {
            ApiConditionalResult.ApiTooLow
        }

    val name: ApiConditionalResult<String?>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ApiConditionalResult.Success(rawInterface.name)
        } else {
            ApiConditionalResult.ApiTooLow
        }

    val interfaceClass: Int get() = rawInterface.interfaceClass
    val interfaceSubclass: Int get() = rawInterface.interfaceSubclass
    val interfaceProtocol: Int get() = rawInterface.interfaceProtocol

    val endpoints: List<AndroidUsbEndpoint> by lazy {
        val result = ArrayList<AndroidUsbEndpoint>(rawInterface.endpointCount)

        for (i in 0 until rawInterface.endpointCount) {
            result.add(AndroidUsbEndpoint(rawInterface.getEndpoint(i)))
        }

        result
    }
}