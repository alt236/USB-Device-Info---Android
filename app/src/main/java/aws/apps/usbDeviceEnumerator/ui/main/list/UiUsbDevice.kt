package aws.apps.usbDeviceEnumerator.ui.main.list

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice

@Parcelize
sealed interface UiUsbDevice : Parcelable {
    val key: String

    @Parcelize
    data class AndroidUsb(override val key: String, val device: AndroidUsbDevice) : UiUsbDevice

    @Parcelize
    data class SysUsb(override val key: String, val device: SysBusUsbDevice) : UiUsbDevice
}