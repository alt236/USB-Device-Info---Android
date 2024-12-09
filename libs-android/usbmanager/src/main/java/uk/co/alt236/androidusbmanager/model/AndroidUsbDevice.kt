package uk.co.alt236.androidusbmanager.model

import android.hardware.usb.UsbDevice
import android.os.Build
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uk.co.alt236.androidusbmanager.result.ApiConditionalResult

@Parcelize
class AndroidUsbDevice(
    @Suppress("MemberVisibilityCanBePrivate") val rawDevice: UsbDevice
) : Parcelable {

    val deviceName: String get() = rawDevice.deviceName

    val manufacturerName: ApiConditionalResult<String?>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ApiConditionalResult.Success(rawDevice.manufacturerName)
        } else {
            ApiConditionalResult.ApiTooLow
        }

    val productName: ApiConditionalResult<String?>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ApiConditionalResult.Success(rawDevice.productName)
        } else {
            ApiConditionalResult.ApiTooLow
        }

    val version: ApiConditionalResult<String>
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ApiConditionalResult.Success(rawDevice.version)
        } else {
            ApiConditionalResult.ApiTooLow
        }

    val vendorId: Int get() = rawDevice.vendorId
    val deviceId: Int get() = rawDevice.deviceId
    val productId: Int get() = rawDevice.productId

    val deviceClass: Int get() = rawDevice.deviceClass
    val deviceSubClass: Int get() = rawDevice.deviceSubclass
    val deviceProtocol: Int get() = rawDevice.deviceProtocol

    val configurations: ApiConditionalResult<List<AndroidUsbConfiguration>> by lazy {
        mapConfigurations()
    }

    val interfaces: List<AndroidUsbInterface>
        get() {
            val result = ArrayList<AndroidUsbInterface>(rawDevice.interfaceCount)
            for (i in 0 until rawDevice.interfaceCount) {
                result.add(AndroidUsbInterface(rawDevice.getInterface(i)))
            }
            return result
        }

    private fun mapConfigurations(): ApiConditionalResult<List<AndroidUsbConfiguration>> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val result = ArrayList<AndroidUsbConfiguration>(rawDevice.configurationCount)
            for (i in 0 until rawDevice.configurationCount) {
                result.add(AndroidUsbConfiguration(rawDevice.getConfiguration(i)))
            }
            ApiConditionalResult.Success(result)
        } else {
            ApiConditionalResult.ApiTooLow
        }
    }
}