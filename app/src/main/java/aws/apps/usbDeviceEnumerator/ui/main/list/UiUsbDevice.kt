package aws.apps.usbDeviceEnumerator.ui.main.list

import uk.co.alt236.androidusbmanager.model.AndroidUsbDevice
import uk.co.alt236.usbdeviceenumerator.sysbususb.SysBusUsbDevice

sealed interface UiUsbDevice {
    val key: String

    data class AndroidUsb(override val key: String, val device: AndroidUsbDevice) : UiUsbDevice
    data class SysUsb(override val key: String, val device: SysBusUsbDevice) : UiUsbDevice
}